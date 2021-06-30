package ru.hse.java.functional;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Function2<ARG1, ARG2, R> {

    R apply(ARG1 arg1, ARG2 arg2);

    default <T> @NotNull Function2<ARG1, ARG2, T> compose(@NotNull FunctionOne<? super R, T> g) {
        return (a, b) -> g.apply(apply(a, b));
    }

    default @NotNull FunctionOne<ARG2, R> bind1(ARG1 arg1) {
        return b -> apply(arg1, b);
    }

    default @NotNull FunctionOne<ARG1, R> bind2(ARG2 arg2) {
        return a -> apply(a, arg2);
    }

    default @NotNull FunctionOne<ARG1, FunctionOne<ARG2, R>> curry() {
        return a -> (b -> apply(a, b));
    }

}
