package ru.hse.java.threadpool;

import org.jetbrains.annotations.NotNull;
import ru.hse.java.threadpool.exceptions.LightExecutionException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;


public class ThreadPoolImpl implements ThreadPool {
    private final Queue<Task<?>> tasks = new LinkedList<>();
    private final List<Thread> threads = new ArrayList<>();

    public ThreadPoolImpl(int n) {
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(new Worker());
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public @NotNull <R> LightFuture<R> submit(Supplier<@NotNull R> supplier) {
        synchronized (tasks) {
            Task<R> task = new Task<>(supplier);
            tasks.add(task);
            tasks.notify();
            return task;
        }
    }

    @Override
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            while (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) {}
            }
        }
    }

    @Override
    public int getNumberOfThreads() {
        return threads.size();
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Task<?> task;
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }
                        task = tasks.remove();
                    }
                    task.execute();
                }
            } catch (InterruptedException ignored) {}
        }
    }

    private class Task<R> implements LightFuture<R> {
        private final Supplier<R> supplier;
        private R result;
        private Exception cause = null;
        private boolean isReady = false;
        private final Object readinessMonitor = new Object();
        private final List<Task<?>> nextTasks = new LinkedList<>();

        public Task(Supplier<R> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public @NotNull R get() throws LightExecutionException {
            try {
                if (!isReady) {
                    synchronized (readinessMonitor) {
                        while (!isReady) {
                            readinessMonitor.wait();
                        }
                    }
                }
                if (cause != null) {
                    throw new LightExecutionException(cause);
                }
            } catch (InterruptedException ignored) {
                throw new RuntimeException("someone tried to interrupt us");
            }
            return result;
        }

        @Override
        public @NotNull <R1> LightFuture<R1> thenApply(Function<R, R1> function) {
            Supplier<R1> nextSupplier = () -> function.apply(result);
            Task<R1> nextTask = new Task<>(nextSupplier);
            if (isReady) {
                if (cause == null) {
                    return submit(nextSupplier);
                } else {
                    return nextTask.setCause(cause);
                }
            }
            synchronized (nextTasks) {
                nextTasks.add(nextTask);
            }
            return nextTask;
        }

        public void execute() {
            try {
                result = supplier.get();
            } catch (Exception exception) {
                cause = exception;
            } finally {
                isReady = true;
                synchronized (readinessMonitor) {
                    readinessMonitor.notifyAll();
                }
                if (cause == null) {
                    synchronized (tasks) {
                        tasks.addAll(nextTasks);
                        tasks.notify();
                    }
                } else {
                    for (var nextTask : nextTasks) {
                        nextTask.setCause(cause);
                    }
                }
            }
        }

        public Task<R> setCause(Exception cause) {
            this.isReady = true;
            this.cause = cause;
            return this;
        }
    }
}
