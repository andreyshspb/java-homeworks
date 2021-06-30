package ru.hse.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map.Entry;

public class DictionaryImplStressTest {
    private static final int SEED = 314;

    @Test
    public void testStressPutMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        for (int i = 0; i < 1000_000; i++) {
            Integer key = random.nextInt(1000);
            String value = makeString(random);
            String tested = testedDict.put(key, value);
            String correct = correctDict.put(key, value);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressGetMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        fillMaps(testedDict, correctDict, random);

        for (int i = 0; i < 1000_000; i++) {
            Integer key = random.nextInt(1000);
            String tested = testedDict.get(key);
            String correct = correctDict.get(key);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressRemoveMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        fillMaps(testedDict, correctDict, random);

        for (int i = 0; i < 1000_000; i++) {
            Integer key = random.nextInt(1000);
            String tested = testedDict.remove(key);
            String correct = correctDict.remove(key);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressContainsKeyMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        fillMaps(testedDict, correctDict, random);

        for (int i = 0; i < 1000_000; i++) {
            Integer key = random.nextInt(1000);
            boolean tested = testedDict.containsKey(key);
            boolean correct = correctDict.containsKey(key);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressKeySetMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        fillMaps(testedDict, correctDict, random);

        Set<Integer> tested = new TreeSet<>(testedDict.keySet());
        Set<Integer> correct = new TreeSet<>(correctDict.keySet());
        Assertions.assertEquals(correct, tested);
    }

    @Test
    public void testStressValuesMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        fillMaps(testedDict, correctDict, random);

        Set<String> tested = new TreeSet<>(testedDict.values());
        Set<String> correct = new TreeSet<>(correctDict.values());
        Assertions.assertEquals(correct, tested);
    }

    @Test
    public void testStressEntrySetMethod() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        fillMaps(testedDict, correctDict, random);

        Set<Entry<Integer, String>> tested = new TreeSet<>(new EntryComparator());
        tested.addAll(testedDict.entrySet());
        Set<Entry<Integer, String>> correct = new TreeSet<>(new EntryComparator());
        correct.addAll(correctDict.entrySet());
        Assertions.assertEquals(correct, tested);
    }

    @Test
    public void testStressBasicMethods() {
        Dictionary<Integer, String> testedDict = new DictionaryImpl<>();
        Map<Integer, String> correctDict = new HashMap<>();

        Random random = new Random(SEED);
        for (int i = 0; i < 1000_000; i++) {
            Integer key = random.nextInt(1000);
            int type = random.nextInt(5);
            if (type == 0) {
                int tested = testedDict.size();
                int correct = correctDict.size();
                Assertions.assertEquals(correct, tested);
            } else if (type == 1) {
                boolean tested = testedDict.containsKey(key);
                boolean correct = correctDict.containsKey(key);
                Assertions.assertEquals(correct, tested);
            } else if (type == 2) {
                String tested = testedDict.get(key);
                String correct = correctDict.get(key);
                Assertions.assertEquals(correct, tested);
            } else if (type == 3) {
                String value = makeString(random);
                String tested = testedDict.put(key, value);
                String correct = correctDict.put(key, value);
                Assertions.assertEquals(correct, tested);
            } else {
                String tested = testedDict.remove(key);
                String correct = correctDict.remove(key);
                Assertions.assertEquals(correct, tested);
            }
        }
    }

    private static class EntryComparator implements Comparator<Entry<Integer, String>> {

        @Override
        public int compare(Entry<Integer, String> first, Entry<Integer, String> second) {
            if (first.getKey().compareTo(second.getKey()) < 0) {
                return -1;
            } else if (first.getKey().equals(second.getKey())) {
                return first.getValue().compareTo(second.getValue());
            }
            return 1;
        }
    }

    private String makeString(Random random) {
        StringBuilder buffer = new StringBuilder();
        int length = random.nextInt(8);
        for (int j = 0; j < length; j++) {
            buffer.append((char) ('a' + random.nextInt(26)));
        }
        return buffer.toString();
    }

    private void fillMaps(Map<Integer, String> first, Map<Integer, String> second, Random random) {
        for (int i = 0; i < 10_000; i++) {
            Integer key = random.nextInt(1000);
            String value = makeString(random);
            first.put(key, value);
            second.put(key, value);
        }
    }

}
