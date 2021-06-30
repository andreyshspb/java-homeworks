package ru.hse.java.streams;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(@NotNull List<String> paths, CharSequence sequence) throws IOException {
        return paths.stream().
                flatMap(path -> {
                    try {
                        return Files.lines(Paths.get(path));
                    } catch (IOException exception) {
                        throw new UncheckedIOException(exception);
                    }
                }).
                filter(line -> line.contains(sequence)).
                collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        return piDividedBy4(10000);
    }

    public static double piDividedBy4(int n) {
        final int seed = 112;
        Random random = new Random(seed);
        Point2D center = new Point2D.Double(0.5, 0.5);
        Stream<Point2D> randomPoints = Stream.generate(
                () -> new Point2D.Double(
                        random.nextDouble(), random.nextDouble()
                )
        );
        return randomPoints.limit(n).
                collect(
                        Collectors.averagingInt(
                                point -> point.distance(center) < 0.5 ? 1 : 0
                        )
                );
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(@NotNull Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().
                max(Comparator.comparing(
                        entry -> entry.getValue().stream().
                                mapToInt(String::length).
                                sum()
                )).
                map(Entry::getKey).
                orElse(null);
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(@NotNull List<Map<String, Integer>> orders) {
        return orders.stream()
                .flatMap(x -> x.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingInt(Map.Entry::getValue)
                ));
    }
}