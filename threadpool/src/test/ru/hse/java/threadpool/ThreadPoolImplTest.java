package ru.hse.java.threadpool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.java.threadpool.exceptions.LightExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolImplTest {
    private static final int NUMBER_OF_THREADS = 8;
    private static ThreadPool threadPool;

    @BeforeEach
    void initThreadPool() {
        threadPool = new ThreadPoolImpl(NUMBER_OF_THREADS);
    }

    @AfterEach
    void shutdown() {
        threadPool.shutdown();
    }

    @Test
    void testIsReady() throws LightExecutionException {
        LightFuture<String> future = threadPool.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
            return "finished";
        });
        assertFalse(future.isReady());
        assertEquals("finished", future.get());
        assertTrue(future.isReady());
    }

    @Test
    void testThenApply() throws LightExecutionException {
        LightFuture<String> string = threadPool.submit(() -> "finished");
        LightFuture<Integer> length = string.thenApply(String::length);
        LightFuture<String> attention = string.thenApply(str -> str + "!");
        assertEquals("finished", string.get());
        assertEquals(8, length.get());
        assertEquals("finished!", attention.get());
    }

    @Test
    void testExceptions() {
        LightFuture<String> first = threadPool.submit(() -> {
            throw new RuntimeException();
        });
        LightFuture<Integer> second = first.thenApply(String::length);
        assertThrows(LightExecutionException.class, first::get);
        assertThrows(LightExecutionException.class, second::get);
    }

}
