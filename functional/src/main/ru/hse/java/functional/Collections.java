package ru.hse.java.functional;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Collections {

    public static <T, R> @NotNull List<R> map(@NotNull FunctionOne<? super T, ? extends R> function,
                                              @NotNull Iterable<T> collection
    ) {
        List<R> result = new ArrayList<>();
        for (T element : collection) {
            result.add(function.apply(element));
        }
        return result;
    }

    public static <T> @NotNull List<T> filter(@NotNull Predicate<? super T> predicate,
                                              @NotNull Iterable<T> collection
    ) {
        List<T> result = new ArrayList<>();
        for (T element : collection) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static <T> @NotNull List<T> takeWhile(@NotNull Predicate<? super T> predicate,
                                                 @NotNull Iterable<T> collection
    ) {
        List<T> result = new ArrayList<>();
        for (T element : collection) {
            if (!predicate.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    public static <T> @NotNull List<T> takeUnless(@NotNull Predicate<? super T> predicate,
                                                  @NotNull Iterable<T> collection
    ) {
        return takeWhile(predicate.not(), collection);
    }

    public static <T, E> E foldl(@NotNull Function2<? super E, ? super T, ? extends E> function,
                                 E initialValue,
                                 @NotNull Iterable<T> collection
    ) {
        E result = initialValue;
        for (T element : collection) {
            result = function.apply(result, element);
        }
        return result;
    }

    public static <T, E> E foldr(@NotNull Function2<? super T, ? super E, ? extends E> function,
                                 E initialValue,
                                 @NotNull Iterable<T> collection
    ) {
        LinkedList<T> buffer = new LinkedList<>();
        collection.forEach(buffer::add);
        ListIterator<T> iterator = buffer.listIterator(buffer.size());
        E result = initialValue;
        while (iterator.hasPrevious()) {
            result = function.apply(iterator.previous(), result);
        }
        return result;
    }

}
