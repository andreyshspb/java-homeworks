package ru.hse.java.functional;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FunctionOne<ARG, R> {

    R apply(ARG arg);

    default <T> @NotNull FunctionOne<ARG, T> compose(@NotNull FunctionOne<? super R, T> g) {
        return a -> g.apply(apply(a));
    }

}
