package ru.hse.java.streams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws IOException {
        File firstFile = File.createTempFile("input1", "txt");
        FileWriter firstFileWriter = new FileWriter(firstFile);
        firstFileWriter.write("hello my friend\n" +
                "my name is andrey\n" +
                "i am testing the program now");
        firstFileWriter.flush();

        File secondFile = File.createTempFile("input2", "txt");
        FileWriter secondFileWriter = new FileWriter(secondFile);
        secondFileWriter.write("02.03 i wrote my first control work\n" +
                "it was a complete suction");
        secondFileWriter.flush();

        List<String> expected = new ArrayList<>();
        expected.add("hello my friend");
        expected.add("my name is andrey");
        expected.add("02.03 i wrote my first control work");

        Assertions.assertEquals(expected,
                SecondPartTasks.findQuotes(
                        List.of(firstFile.getAbsolutePath(),
                                secondFile.getAbsolutePath()),
                        "my")
        );

    }

    @Test
    public void testPiDividedBy4() {
        double expected = Math.PI / 4;
        double tested = SecondPartTasks.piDividedBy4();
        Assertions.assertTrue(Math.abs(expected - tested) < 0.01);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        List<String> chekhov = new ArrayList<>();
        chekhov.add("The Cherry Orchard");
        chekhov.add("Three Sisters");
        chekhov.add("Uncle Vanya");
        List<String> remark = new ArrayList<>();
        remark.add("All Quiet on the Western Front");
        remark.add("The Black Obelisk");
        remark.add("Shadows in Paradise");
        compositions.put("Chekhov", chekhov);
        compositions.put("Remark", remark);

        String expected = "Remark";
        Assertions.assertEquals(expected,
                SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        List<Map<String, Integer>> orders = new ArrayList<>();
        Map<String, Integer> firstOrder = new HashMap<>();
        firstOrder.put("bread", 100);
        firstOrder.put("apple", 500);
        firstOrder.put("orange", 1000);
        Map<String, Integer> secondOrder = new HashMap<>();
        secondOrder.put("apple", 5000);
        secondOrder.put("orange", 1000);
        secondOrder.put("pork", 50);
        orders.add(firstOrder);
        orders.add(secondOrder);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("bread", 100);
        expected.put("apple", 5500);
        expected.put("orange", 2000);
        expected.put("pork", 50);

        Assertions.assertEquals(expected,
                SecondPartTasks.calculateGlobalOrder(orders));
    }

}