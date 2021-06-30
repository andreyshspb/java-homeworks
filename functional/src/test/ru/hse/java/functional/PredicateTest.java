package ru.hse.java.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PredicateTest {

    private final Predicate<Integer> isOdd = (a -> a % 2 == 1);
    private final Predicate<Integer> isEven = isOdd.not();
    private final Predicate<Integer> isEvenOrOne = isEven.or(a -> a == 1);
    private final Predicate<Integer> isDivisibleByThree = (a -> a % 3 == 0);
    private final Predicate<Integer> isDivisibleBySix = isEven.and(isDivisibleByThree);

    @Test
    public void test() {
        for (int i = 0; i < 1000; i++) {
            Assertions.assertEquals(i % 2 == 1, isOdd.apply(i));
            Assertions.assertEquals(i % 2 == 0, isEven.apply(i));
            Assertions.assertEquals(i % 2 == 0 || i == 1, isEvenOrOne.apply(i));
            Assertions.assertEquals(i % 3 == 0, isDivisibleByThree.apply(i));
            Assertions.assertEquals(i % 6 == 0, isDivisibleBySix.apply(i));
            Assertions.assertTrue(Predicate.ALWAYS_TRUE().apply(i));
            Assertions.assertFalse(Predicate.ALWAYS_FALSE().apply(i));
        }
    }

}
