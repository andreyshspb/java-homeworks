package ru.hse.java.functional;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Predicate<ARG> {

    boolean apply(ARG arg);

    default @NotNull Predicate<ARG> or(@NotNull Predicate<? super ARG> argPredicate) {
        return a -> (apply(a) || argPredicate.apply(a));
    }

    default @NotNull Predicate<ARG> and(@NotNull Predicate<? super ARG> argPredicate) {
        return a -> (apply(a) && argPredicate.apply(a));
    }

    default @NotNull Predicate<ARG> not() {
        return a -> !apply(a);
    }

    static <T> @NotNull Predicate<T> ALWAYS_TRUE() {
        return a -> true;
    }

    static <T> @NotNull Predicate<T> ALWAYS_FALSE() {
        return a -> false;
    }

}
