package ru.hse.java.trie;

import java.util.Map;
import java.util.TreeMap;

public class TrieImpl implements Trie {

    private static class Node {
        private boolean isTerminal = false;
        private int howManyStartsWithPrefix = 0;
        private final Map<Character, Node> next = new TreeMap<>();

        /**
         * @return <tt>true</tt> if this node does not have a child
         *         by specified symbol
         */
        public boolean hasNoChild(char symbol) {
            return !next.containsKey(symbol);
        }

        /**
         * @return a child by specified symbol or
         *         null if this child does not exist
         */
        public Node getChild(char symbol) {
            return next.get(symbol);
        }

        /**
         * Add a child by specified symbol if this child does not exist
         */
        public void addChild(char symbol) {
            if (hasNoChild(symbol)) {
                next.put(symbol, new Node());
            }
        }

        /**
         * Delete a child by specified symbol
         */
        public void deleteChild(char symbol) {
            next.remove(symbol);
        }
    }

    private final Node root = new Node();

    private static boolean isIncorrectString(String element) {
        for (char s : element.toCharArray()) {
            if (!('a' <= s && s <= 'z') && !('A' <= s && s <= 'Z')) {
                return true;
            }
        }
        return false;
    }

    private static void checkString(String element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Got a null");
        } else if (isIncorrectString(element)) {
            throw new IllegalArgumentException("Got a bad string");
        }
    }

    @Override
    public boolean add(String element) throws IllegalArgumentException {
        checkString(element);
        if (contains(element)) {
            return false;
        }
        Node curNode = root;
        for (char symbol : element.toCharArray()) {
            curNode.addChild(symbol);
            curNode.howManyStartsWithPrefix++;
            curNode = curNode.getChild(symbol);
        }
        curNode.howManyStartsWithPrefix++;
        curNode.isTerminal = true;
        return true;
    }

    @Override
    public boolean contains(String element) throws IllegalArgumentException {
        checkString(element);
        Node curNode = root;
        for (char symbol : element.toCharArray()) {
            if (curNode.hasNoChild(symbol)) {
                return false;
            }
            curNode = curNode.getChild(symbol);
        }
        return curNode.isTerminal;
    }

    @Override
    public boolean remove(String element) throws IllegalArgumentException {
        checkString(element);
        if (!contains(element)) {
            return false;
        }
        Node curNode = root;
        for (char symbol : element.toCharArray()) {
            curNode.howManyStartsWithPrefix--;
            if (curNode.getChild(symbol).howManyStartsWithPrefix == 1) {
                curNode.deleteChild(symbol);
                return true;
            }
            curNode = curNode.getChild(symbol);
        }
        curNode.howManyStartsWithPrefix--;
        curNode.isTerminal = false;
        return true;
    }

    @Override
    public int size() {
        return root.howManyStartsWithPrefix;
    }

    @Override
    public int howManyStartsWithPrefix(String element) throws IllegalArgumentException {
        checkString(element);
        Node curNode = root;
        for (char symbol : element.toCharArray()) {
            if (curNode.hasNoChild(symbol)) {
                return 0;
            }
            curNode = curNode.getChild(symbol);
        }
        return curNode.howManyStartsWithPrefix;
    }

    @Override
    public String nextString(String element, int k) throws IllegalArgumentException {
        checkString(element);
        if (k == 0) {
            return contains(element) ? element : null;
        }

        Node curNode = root;
        k += (curNode.isTerminal ? 1 : 0);
        for (char symbol : element.toCharArray()) {
            for (Map.Entry<Character, Node> pair : curNode.next.entrySet()) {
                if (pair.getKey() < symbol) {
                    k += pair.getValue().howManyStartsWithPrefix;
                }
            }
            if (curNode.hasNoChild(symbol)) {
                break;
            }
            curNode = curNode.getChild(symbol);
            k += (curNode.isTerminal ? 1 : 0);
        }

        if (size() < k) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        curNode = root;
        while (true) {
            k -= (curNode.isTerminal ? 1 : 0);
            if (k == 0) {
                return result.toString();
            }
            for (Map.Entry<Character, Node> pair : curNode.next.entrySet()) {
                if (k > pair.getValue().howManyStartsWithPrefix) {
                    k -= pair.getValue().howManyStartsWithPrefix;
                } else {
                    result.append(pair.getKey());
                    curNode = curNode.getChild(pair.getKey());
                    break;
                }
            }
        }
    }

}
