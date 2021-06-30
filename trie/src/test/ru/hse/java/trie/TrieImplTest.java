package ru.hse.java.trie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.TreeSet;

public class TrieImplTest {

    @Test
    public void testIncorrectArguments() {
        Trie trie = new TrieImpl();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.add(null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.contains(null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.remove(null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.howManyStartsWithPrefix(null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.nextString(null, 5);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.add("John Wick");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.contains("GTA5");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.remove("hello\n");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.howManyStartsWithPrefix("tik-tak-toe");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            trie.nextString("=)", 5);
        });
    }

    @Test
    public void testEmptyTrie() {
        Trie trie = new TrieImpl();
        Assertions.assertFalse(trie.contains(""));
        Assertions.assertFalse(trie.contains("aaa"));
        Assertions.assertFalse(trie.contains("abc"));
        Assertions.assertFalse(trie.remove(""));
        Assertions.assertFalse(trie.remove("aaa"));
        Assertions.assertFalse(trie.remove("abc"));
        Assertions.assertEquals(0, trie.size());
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix(""));
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertNull(trie.nextString("", 0));
        Assertions.assertNull(trie.nextString("", 1));
        Assertions.assertNull(trie.nextString("aaa", 3));
    }

    @Test
    public void testAddMethod() {
        Trie trie = new TrieImpl();
        Assertions.assertTrue(trie.add(""));
        Assertions.assertTrue(trie.add("cpp"));
        Assertions.assertTrue(trie.add("is"));
        Assertions.assertTrue(trie.add("the"));
        Assertions.assertTrue(trie.add("best"));
        Assertions.assertFalse(trie.add(""));
        Assertions.assertFalse(trie.add("best"));
        Assertions.assertFalse(trie.add("cpp"));
    }

    @Test
    public void testContainsMethod() {
        Trie trie = new TrieImpl();
        trie.add("");
        trie.add("python");
        trie.add("is");
        trie.add("my");
        trie.add("love");
        Assertions.assertTrue(trie.contains(""));
        Assertions.assertTrue(trie.contains("python"));
        Assertions.assertTrue(trie.contains("my"));
        Assertions.assertFalse(trie.contains("cpp"));
        Assertions.assertFalse(trie.contains("the"));
        Assertions.assertFalse(trie.contains("best"));
    }

    @Test
    public void testRemoveMethod() {
        Trie trie = new TrieImpl();
        trie.add("");
        trie.add("haskell");
        trie.add("is");
        trie.add("for");
        trie.add("freaks");
        Assertions.assertTrue(trie.remove(""));
        Assertions.assertTrue(trie.remove("haskell"));
        Assertions.assertFalse(trie.remove("cpp"));
        Assertions.assertTrue(trie.remove("for"));
        Assertions.assertFalse(trie.remove(""));
        Assertions.assertFalse(trie.remove("for"));
    }

    @Test
    public void testHowManyStartsWithPrefixMethod() {
        Trie trie = new TrieImpl();

        Assertions.assertEquals(0, trie.size());
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("a"));

        trie.add("aaa");
        Assertions.assertEquals(1, trie.size());
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.add("aa");
        Assertions.assertEquals(2, trie.size());
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.add("ab");
        Assertions.assertEquals(3, trie.size());
        Assertions.assertEquals(3, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.add("aa");
        Assertions.assertEquals(3, trie.size());
        Assertions.assertEquals(3, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.remove("aa");
        Assertions.assertEquals(2, trie.size());
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.remove("aaaa");
        Assertions.assertEquals(2, trie.size());
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.remove("a");
        Assertions.assertEquals(2, trie.size());
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.add("");
        Assertions.assertEquals(3, trie.size());
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aa"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("ab"));
        Assertions.assertEquals(1, trie.howManyStartsWithPrefix("aaa"));
    }

    @Test
    public void testNextStringMethod() {
        Trie trie = new TrieImpl();
        trie.add("a");
        trie.add("b");
        trie.add("gz");
        trie.add("i");
        trie.add("k");
        trie.add("z");
        Assertions.assertNull(trie.nextString("gu", 0));
        Assertions.assertEquals("gz", trie.nextString("gu", 1));
        Assertions.assertEquals("i", trie.nextString("gu", 2));
        Assertions.assertEquals("k", trie.nextString("gu", 3));
        Assertions.assertEquals("z", trie.nextString("gu", 4));
        Assertions.assertNull(trie.nextString("gu", 5));
    }

    @Test
    public void testStressAddMethod() {
        Trie testedTrie = new TrieImpl();
        TreeSet<String> correctTrie = new TreeSet<>();

        int seed = 314;
        Random random = new Random(seed);
        for (int i = 0; i < 100_000; i++) {
            String element = makeString(random);
            boolean tested = testedTrie.add(element);
            boolean correct = correctTrie.add(element);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressContainsMethod() {
        Trie testedTrie = new TrieImpl();
        TreeSet<String> correctTrie = new TreeSet<>();

        int seed = 314;
        Random random = new Random(seed);
        for (int i = 0; i < 50_000; i++) {
            String element = makeString(random);
            testedTrie.add(element);
            correctTrie.add(element);
        }

        for (int i = 0; i < 50_000; i++) {
            String element = makeString(random);
            boolean tested = testedTrie.contains(element);
            boolean correct = correctTrie.contains(element);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressRemoveMethod() {
        Trie testedTrie = new TrieImpl();
        TreeSet<String> correctTrie = new TreeSet<>();

        int seed = 314;
        Random random = new Random(seed);
        for (int i = 0; i < 50_000; i++) {
            String element = makeString(random);
            testedTrie.add(element);
            correctTrie.add(element);
        }

        for (int i = 0; i < 50_000; i++) {
            String element = makeString(random);
            boolean tested = testedTrie.remove(element);
            boolean correct = correctTrie.remove(element);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressBasicMethods() {
        Trie testedTrie = new TrieImpl();
        TreeSet<String> correctTrie = new TreeSet<>();

        int seed = 314;
        Random random = new Random(seed);
        for (int i = 0; i < 100_000; i++) {
            String element = makeString(random);
            int type = random.nextInt(4);
            if (type == 0) {
                boolean tested = testedTrie.add(element);
                boolean correct = correctTrie.add(element);
                Assertions.assertEquals(correct, tested);
            } else if (type == 1) {
                boolean tested = testedTrie.contains(element);
                boolean correct = correctTrie.contains(element);
                Assertions.assertEquals(correct, tested);
            } else if (type == 2) {
                boolean tested = testedTrie.remove(element);
                boolean correct = correctTrie.remove(element);
                Assertions.assertEquals(correct, tested);
            } else {
                int tested = testedTrie.size();
                int correct = correctTrie.size();
                Assertions.assertEquals(correct, tested);
            }
        }
    }

    @Test
    public void testStressHowManyStartsWithPrefixMethod() {
        Trie testedTrie = new TrieImpl();
        TreeSet<String> correctTrie = new TreeSet<>();

        int seed = 314;
        Random random = new Random(seed);
        for (int i = 0; i < 1000; i++) {
            String element = makeString(random);
            testedTrie.add(element);
            correctTrie.add(element);
        }

        for (int i = 0; i < 1000; i++) {
            String element = makeString(random);
            int correct = 0;
            for (String string : correctTrie) {
                correct += (string.startsWith(element) ? 1 : 0);
            }
            int tested = testedTrie.howManyStartsWithPrefix(element);
            Assertions.assertEquals(correct, tested);
        }
    }

    @Test
    public void testStressNextStringMethod() {
        Trie testedTrie = new TrieImpl();
        TreeSet<String> correctTrie = new TreeSet<>();

        int seed = 314;
        Random random = new Random(seed);
        for (int i = 0; i < 1000; i++) {
            String element = makeString(random);
            testedTrie.add(element);
            correctTrie.add(element);
        }

        for (int i = 0; i < 1000; i++) {
            String element = makeString(random);
            int k = random.nextInt(1000);
            String tested = testedTrie.nextString(element, k);
            String correct = null;
            if (k == 0 && correctTrie.contains(element)) {
                correct = element;
            } else if (k != 0) {
                for (String string : correctTrie) {
                    if (string.compareTo(element) > 0) {
                        k--;
                    }
                    if (k == 0) {
                        correct = string;
                        break;
                    }
                }
            }
            Assertions.assertEquals(correct, tested);
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

}
