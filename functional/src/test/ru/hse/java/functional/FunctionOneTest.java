package ru.hse.java.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunctionOneTest {

    private final FunctionOne<Integer, Integer> inc = (a -> a + 1);
    private final FunctionOne<Integer, Integer> square = (a -> a * a);
    private final FunctionOne<Integer, Integer> squareOfInc = inc.compose(square);

    @Test
    public void test() {
        for (int x = 0; x < 1000; x++) {
            Assertions.assertEquals(x + 1, inc.apply(x));
            Assertions.assertEquals(x * x, square.apply(x));
            Assertions.assertEquals((x + 1) * (x + 1), squareOfInc.apply(x));
        }
    }

}
