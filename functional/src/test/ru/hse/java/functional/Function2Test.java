package ru.hse.java.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Function2Test {

    private final Function2<Integer, Integer, Integer> sum = Integer::sum;
    private final FunctionOne<Integer, Integer> square = a -> a * a;
    private final Function2<Integer, Integer, Integer> squareOfSum = sum.compose(square);
    private final Function2<Integer, Integer, Integer> div = (a, b) -> a / b;
    private final FunctionOne<Integer, Integer> inc = sum.bind1(1);
    private final FunctionOne<Integer, Integer> id = div.bind2(1);
    private final FunctionOne<Integer, FunctionOne<Integer, Integer>> currySum = sum.curry();

    @Test
    public void testCorrectness() {
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                Assertions.assertEquals(x + y, sum.apply(x, y));
                Assertions.assertEquals((x + y) * (x + y), squareOfSum.apply(x, y));
                Assertions.assertEquals(x + 1, inc.apply(x));
                Assertions.assertEquals(x, id.apply(x));
                Assertions.assertEquals(x + y, currySum.apply(x).apply(y));
            }
        }
    }

}
