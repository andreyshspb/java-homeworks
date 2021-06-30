package ru.hse.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map.Entry;

public class DictionaryImplSimpleTest {

    @Test
    public void testEmptyDictionary() {
        Dictionary<Integer, String> dict = new DictionaryImpl<>();
        Assertions.assertEquals(0, dict.size());
        Assertions.assertFalse(dict.containsKey(1));
        Assertions.assertFalse(dict.containsKey(2));
        Assertions.assertFalse(dict.containsKey(3));
        Assertions.assertFalse(dict.containsKey(0));
        Assertions.assertNull(dict.remove(1));
        Assertions.assertNull(dict.remove(2));
        Assertions.assertNull(dict.remove(3));
        Assertions.assertNull(dict.remove(0));
        Assertions.assertTrue(dict.keySet().isEmpty());
        Assertions.assertTrue(dict.values().isEmpty());
        Assertions.assertTrue(dict.entrySet().isEmpty());
    }

    @Test
    public void testExtremeCasesForConstructor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DictionaryImpl<Integer, Integer>(-1, 0.25, 0.75);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DictionaryImpl<Integer, Integer>(16, 0.75, 0.75);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DictionaryImpl<Integer, Integer>(16, 0.25, 0.0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DictionaryImpl<Integer, Integer>(16, 0.25, 1.25);
        });
        Assertions.assertDoesNotThrow(() -> {
            new DictionaryImpl<Integer, Integer>(16, 0.0, 0.75);
        });
        Assertions.assertDoesNotThrow(() -> {
            new DictionaryImpl<Integer, Integer>(16, 0.5, 0.75);
        });
        Assertions.assertDoesNotThrow(() -> {
            new DictionaryImpl<Integer, Integer>(16, 0.25, 1.0);
        });
    }

    @Test
    public void textExtremeCasesForMethods() {
        Dictionary<Integer, Integer> dict = new DictionaryImpl<>();
        Assertions.assertDoesNotThrow(() -> dict.containsKey(null));
        Assertions.assertDoesNotThrow(() -> dict.get(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dict.put(null, null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dict.put(0, null);
        });
        Assertions.assertDoesNotThrow(() -> dict.remove(null));
    }

    @Test
    public void testClearMethod() {
        Dictionary<Integer, String> dict = new DictionaryImpl<>();
        dict.put(1, "hello");
        dict.put(2, "my");
        dict.put(3, "dear");
        dict.put(4, "friend");
        dict.put(5, "Dick");
        dict.clear();
        Assertions.assertEquals(0, dict.size());
        Assertions.assertFalse(dict.containsKey(1));
        Assertions.assertFalse(dict.containsKey(2));
        Assertions.assertFalse(dict.containsKey(3));
        Assertions.assertFalse(dict.containsKey(4));
        Assertions.assertFalse(dict.containsKey(5));
        Assertions.assertNull(dict.get(1));
        Assertions.assertNull(dict.get(2));
        Assertions.assertNull(dict.get(3));
        Assertions.assertNull(dict.get(4));
        Assertions.assertNull(dict.get(5));
    }

    @Test
    public void testKeySetMethod() {
        Dictionary<Integer, String> dict = new DictionaryImpl<>();
        dict.put(1, "hello");
        dict.put(2, "my");
        Set<Integer> set = dict.keySet();
        Iterator<Integer> iterator = set.iterator();
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(1, set.size());
        Assertions.assertEquals(1, dict.size());
        Assertions.assertFalse(dict.containsKey(1));
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(0, set.size());
        Assertions.assertEquals(0, dict.size());
        Assertions.assertFalse(dict.containsKey(2));
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testValuesMethod() {
        Dictionary<Integer, String> dict = new DictionaryImpl<>();
        dict.put(1, "hello");
        dict.put(2, "my");
        dict.put(3, "hello");
        Collection<String> set = dict.values();
        Iterator<String> iterator = set.iterator();
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(2, set.size());
        Assertions.assertEquals(2, dict.size());
        Assertions.assertFalse(dict.containsKey(1));
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(1, set.size());
        Assertions.assertEquals(1, dict.size());
        Assertions.assertFalse(dict.containsKey(2));
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(0, set.size());
        Assertions.assertEquals(0, dict.size());
        Assertions.assertFalse(dict.containsKey(3));
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testEntrySetMethod() {
        Dictionary<Integer, String> dict = new DictionaryImpl<>();
        dict.put(1, "hello");
        dict.put(2, "my");
        Set<Entry<Integer, String>> set = dict.entrySet();
        Iterator<Entry<Integer, String>> iterator = set.iterator();
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(1, set.size());
        Assertions.assertEquals(1, dict.size());
        Assertions.assertFalse(dict.containsKey(1));
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        Assertions.assertEquals(0, set.size());
        Assertions.assertEquals(0, dict.size());
        Assertions.assertFalse(dict.containsKey(2));
        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }

}
