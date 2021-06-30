package ru.hse.java.streams;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// data class Track(var name: String, var rating: Int)
// data class Artist(var name: String)
// data class Album(val name: String, val tracks: List<Track>, val artist: Artist)

public final class FirstPartTasks {

    private FirstPartTasks() {
    }

    // Список названий альбомов
    @NotNull
    public static List<String> allNames(@NotNull Stream<Album> albums) {
        return albums.map(Album::getName).
                collect(Collectors.toList());
    }

    // Список названий альбомов, отсортированный лексикографически по названию
    @NotNull
    public static List<String> allNamesSorted(@NotNull Stream<Album> albums) {
        return albums.map(Album::getName).
                sorted().
                collect(Collectors.toList());
    }

    // Список треков, отсортированный лексикографически по названию, включающий все треки альбомов из 'albums'
    @NotNull
    public static List<String> allTracksSorted(@NotNull Stream<Album> albums) {
        return albums.flatMap(album -> album.getTracks().stream()).
                map(Track::getName).
                sorted().
                collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    @NotNull
    public static List<Album> sortedFavorites(@NotNull Stream<Album> albums) {
        return albums.filter(album -> album.getTracks().stream().anyMatch(track -> track.getRating() > 95)).
                sorted(Comparator.comparing(Album::getName)).
                collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    @NotNull
    public static Map<Artist, List<Album>> groupByArtist(@NotNull Stream<Album> albums) {
        return albums.collect(
                Collectors.groupingBy(
                        Album::getArtist
                )
        );
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Album' использовать его имя)
    @NotNull
    public static Map<Artist, List<String>> groupByArtistMapName(@NotNull Stream<Album> albums) {
        return albums.collect(
                Collectors.groupingBy(
                        Album::getArtist,
                        Collectors.mapping(
                                Album::getName,
                                Collectors.toList()
                        )
                )
        );
    }

    // Число повторяющихся альбомов в потоке (суммарное число повторений)
    public static long countAlbumDuplicates(@NotNull Stream<Album> albums) {
        Map<Album, Long> frequencyTable = albums.collect(
                Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                )
        );
        return frequencyTable.values().stream().
                mapToLong(value -> value - 1).
                sum();
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    @NotNull
    public static Optional<Album> minMaxRating(@NotNull Stream<Album> albums) {
        return albums.min(Comparator.comparingInt(
                album -> album.getTracks().stream().
                        mapToInt(Track::getRating).
                        max().orElse(0)
        ));
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    @NotNull
    public static List<Album> sortByAverageRating(@NotNull Stream<Album> albums) {
        return albums.
                sorted(Collections.reverseOrder(Comparator.comparingDouble(
                        album -> album.getTracks().stream().
                                collect(Collectors.averagingInt(
                                        Track::getRating
                                ))
                ))).
                collect(Collectors.toList());
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(@NotNull IntStream stream, int modulo) {
        return stream.reduce(1, (a, b) -> (a * b) % modulo);
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    @NotNull
    public static String joinTo(@NotNull String... strings) {
        return Arrays.stream(strings).
                collect(
                        Collectors.joining(", ", "<", ">")
                );
    }

    // Вернуть поток из объектов класса 'clazz'
    @NotNull
    public static <R> Stream<R> filterIsInstance(@NotNull Stream<?> s, @NotNull Class<R> clazz) {
        return s.filter(clazz::isInstance)
                .map(clazz::cast);
    }
}