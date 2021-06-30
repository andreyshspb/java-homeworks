package ru.hse.java.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionsTest {

    private final static int SEED = 112;

    private final Predicate<Integer> isOdd = (a -> a % 2 == 1);
    private final Predicate<Integer> isDivisibleBySix = (a -> a % 6 == 0);
    private final FunctionOne<Integer, Integer> square = a -> a * a;
    private final Function2<Integer, Integer, Integer> max = Integer::max;

    @Test
    public void testMap() {
        Random random = new Random(SEED);
        for (int i = 0; i < 1000; i++) {
            List<Integer> list = makeList(random);
            List<Integer> tested = Collections.map(square, list);
            List<Integer> correct = list.stream().map(a -> a * a).collect(Collectors.toList());
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testFilter() {
        Random random = new Random(SEED);
        for (int i = 0; i < 1000; i++) {
            List<Integer> list = makeList(random);
            List<Integer> tested = Collections.filter(isOdd, list);
            List<Integer> correct = list.stream().filter(a -> a % 2 == 1).collect(Collectors.toList());
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testTakeWhile() {
        Random random = new Random(SEED);
        for (int i = 0; i < 1000; i++) {
            List<Integer> list = makeList(random);
            List<Integer> tested = Collections.takeWhile(isDivisibleBySix, list);
            List<Integer> correct = list.stream().takeWhile(a -> a % 6 == 0).collect(Collectors.toList());
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testTakeUnless() {
        Random random = new Random(SEED);
        for (int i = 0; i < 1000; i++) {
            List<Integer> list = makeList(random);
            List<Integer> tested = Collections.takeUnless(isDivisibleBySix, list);
            List<Integer> correct = list.stream().takeWhile(a -> a % 6 != 0).collect(Collectors.toList());
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testFold() {
        Random random = new Random(SEED);
        for (int i = 0; i < 1000; i++) {
            List<Integer> list = makeList(random);
            Integer tested1 = Collections.foldl(max, 0, list);
            Integer tested2 = Collections.foldr(max, 0, list);
            Integer correct = list.stream().mapToInt(a -> a).max().orElse(0);
            Assertions.assertEquals(correct, tested1);
            Assertions.assertEquals(correct, tested2);
        }
    }

    @Test
    void testInvariantCheck() {
        FunctionOne<String, Integer> lengthFunction = String::length;

        List<String> list = Arrays.asList("a", "cd", "efg", "hi", "j");
        List<Number> listOfLength = Collections.map(lengthFunction, list);

        List<Number> expected = Arrays.asList(1, 2, 3, 2, 1);
        Assertions.assertEquals(expected, listOfLength);
    }

    private List<Integer> makeList(Random random) {
        int length = random.nextInt(100_000);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(random.nextInt(1000));
        }
        return result;
    }

}
