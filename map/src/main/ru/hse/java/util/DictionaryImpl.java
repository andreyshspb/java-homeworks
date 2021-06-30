package ru.hse.java.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The data struct for storage of mappings.
 * The key can not be null. The value can not be null.
 * @param <K> -- the type of keys.
 * @param <V> -- the type of values.
 */
public class DictionaryImpl<K, V> extends AbstractMap<K, V> implements Dictionary<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final double DEFAULT_MIN_LOAD_FACTOR = 0.25;
    private static final double DEFAULT_MAX_LOAD_FACTOR = 0.75;

    private int size;
    private int cntTaken;
    private final int minCapacity;
    private final double minLoadFactor;
    private final double maxLoadFactor;
    private ArrayList<Entry<K, V>> array;

    private class DictionaryEntrySet extends AbstractSet<Entry<K, V>> {

        private class DictionaryEntryIterator implements Iterator<Entry<K, V>> {
            private int curIndex = 0;

            @Override
            public boolean hasNext() {
                for (int i = curIndex; i < array.size(); i++) {
                    if (isTaken(i) && !isDeleted(i)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Entry<K, V> next() throws NoSuchElementException {
                for (; curIndex < array.size(); curIndex++) {
                    if (isTaken(curIndex) && !isDeleted(curIndex)) {
                        return array.get(curIndex++);
                    }
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() throws IllegalStateException {
                if (curIndex - 1 < 0 || isDeleted(curIndex - 1)) {
                    throw new IllegalStateException();
                }
                size--;
                array.get(curIndex - 1).setValue(null);
            }
        }

        @Override
        public @NotNull Iterator<Entry<K, V>> iterator() {
            return new DictionaryEntryIterator();
        }

        @Override
        public int size() {
            return size;
        }

    }

    private class DictionaryKeySet extends AbstractSet<K> {
        private final Set<Entry<K, V>> delegate;

        private class DictionaryKeyIterator implements Iterator<K> {
            private final Iterator<Entry<K, V>> delegateIterator;

            public DictionaryKeyIterator(Iterator<Entry<K, V>> delegateIterator) {
                this.delegateIterator = delegateIterator;
            }

            @Override
            public boolean hasNext() {
                return delegateIterator.hasNext();
            }

            @Override
            public K next() {
                return delegateIterator.next().getKey();
            }

            @Override
            public void remove() {
                delegateIterator.remove();
            }
        }

        public DictionaryKeySet(Set<Entry<K, V>> delegate) {
            this.delegate = delegate;
        }

        @Override
        public @NotNull Iterator<K> iterator() {
            return new DictionaryKeyIterator(delegate.iterator());
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    private class DictionaryValueCollection extends AbstractCollection<V> {
        private final Set<Entry<K, V>> delegate;

        private class DictionaryValueIterator implements Iterator<V> {
            private final Iterator<Entry<K, V>> delegateIterator;

            public DictionaryValueIterator(Iterator<Entry<K, V>> delegateIterator) {
                this.delegateIterator = delegateIterator;
            }

            @Override
            public boolean hasNext() {
                return delegateIterator.hasNext();
            }

            @Override
            public V next() {
                return delegateIterator.next().getValue();
            }

            @Override
            public void remove() {
                delegateIterator.remove();
            }
        }

        public DictionaryValueCollection(Set<Entry<K, V>> delegate) {
            this.delegate = delegate;
        }

        @Override
        public @NotNull Iterator<V> iterator() {
            return new DictionaryValueIterator(delegate.iterator());
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    private int getIndex(Object key) {
        return Math.abs(key.hashCode()) % array.size();
    }

    private void build(int capacity) {
        size = 0;
        cntTaken = 0;
        array = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            array.add(null);
        }
    }

    private void rehash() {
        int newCapacity;
        if (size <= minLoadFactor * array.size() && array.size() != minCapacity) {
            newCapacity = array.size() / 2;
        } else if (maxLoadFactor * array.size() <= cntTaken) {
            newCapacity = 2 * array.size();
        } else {
            return;
        }
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(array);
        build(newCapacity);
        for (var pair : buffer) {
            if (pair != null && pair.getValue() != null) {
                forcePut(pair.getKey(), pair.getValue());
            }
        }
    }

    private void forcePut(K key, V value) {
        size++;
        cntTaken++;
        int position = findPosition(key);
        array.set(position, new SimpleEntry<>(key, value));
    }

    private boolean isTaken(int index) {
        return array.get(index) != null;
    }

    private boolean equalByKeys(Object key, int index) {
        return array.get(index).getKey().equals(key);
    }

    private boolean isDeleted(int index) {
        return array.get(index).getValue() == null;
    }

    private int findPosition(Object key) {
        int index = getIndex(key);
        while (isTaken(index) && (!equalByKeys(key, index) || isDeleted(index))) {
            index = (index + 1) % array.size();
        }
        return index;
    }

    /**
     * Constructs an empty dictionary with the default initial capacity (16),
     * the default initial min load factor (0.25) and the default initial max load factor (0.75).
     */
    public DictionaryImpl() {
        this(DEFAULT_CAPACITY, DEFAULT_MIN_LOAD_FACTOR, DEFAULT_MAX_LOAD_FACTOR);
    }

    /**
     * Constructs an empty dictionary with the specified parameters.
     * @param initCapacity -- the initial capacity.
     * @param initMinLoadFactor -- the initial min load factor.
     * @param initMaxLoadFactor -- the initial max load factor.
     * @throws IllegalArgumentException -- if the initial capacity is negative or
     *                                     the min initial load factor is not in [0, 0.5] or
     *                                     the max initial load factor in not in (0, 1].
     */
    public DictionaryImpl(int initCapacity, double initMinLoadFactor, double initMaxLoadFactor) throws IllegalArgumentException {
        if (initCapacity < 0) {
            throw new IllegalArgumentException("the initial capacity is negative: " + initCapacity);
        }
        if (initMinLoadFactor < 0.0 || 0.5 < initMinLoadFactor) {
            throw new IllegalArgumentException("the initial min load factor is not in [0, 0.5]");
        }
        if (initMaxLoadFactor <= 0.0 || 1 < initMaxLoadFactor) {
            throw new IllegalArgumentException("the initial max load factor is not in (0, 1]");
        }
        minCapacity = Math.min(initCapacity, MAXIMUM_CAPACITY);
        minLoadFactor = initMinLoadFactor;
        maxLoadFactor = initMaxLoadFactor;
        build(initCapacity);
    }

    /**
     * @return the number of mappings in this dictionary.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @param key -- the key whose existence we want to determine.
     * @return true if this dictionary contains a mapping with the specified key.
     */
    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        int position = findPosition(key);
        return array.get(position) != null;
    }

    /**
     * @param key -- the key whose value we are looking for.
     * @return the value by the specified key,
     *         or null if this dictionary contains no mapping with specified key.
     */
    @Override
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        int position = findPosition(key);
        if (array.get(position) == null) {
            return null;
        }
        return array.get(position).getValue();
    }

    /**
     * Puts the mapping in this dictionary.
     * @param key -- the key of the mapping.
     * @param value -- the key of the mapping.
     * @return previous value if this dictionary contained the mapping with the specified key, or null.
     * @throws IllegalArgumentException if the specified key is null or the specified value is null.
     */
    @Override
    public V put(@NotNull K key, @NotNull V value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("the key is null");
        }
        if (value == null) {
            throw new IllegalArgumentException("the value is null");
        }
        int position = findPosition(key);
        if (array.get(position) == null) {
            size++;
            cntTaken++;
            array.set(position, new SimpleEntry<>(key, value));
            rehash();
            return null;
        }
        return array.get(position).setValue(value);
    }

    /**
     * Removes the mapping with the specified key.
     * @param key -- the key whose mapping we want to delete.
     * @return the value of the deleted mapping if this dictionary contained it, or null.
     */
    @Override
    public V remove(Object key) {
        if (key == null) {
            return null;
        }
        int position = findPosition(key);
        if (array.get(position) != null) {
            size--;
            V result = array.get(position).setValue(null);
            rehash();
            return result;
        }
        return null;
    }


    /**
     * Removes all of the mappings from this dictionary.
     */
    @Override
    public void clear() {
        build(minCapacity);
    }

    /**
     * @return a set view of the keys contained in this dictionary.
     */
    @Override
    public @NotNull Set<K> keySet() {
        return new DictionaryKeySet(entrySet());
    }

    /**
     * @return a collection view of the values contained in this dictionary.
     */
    @Override
    public @NotNull Collection<V> values() {
        return new DictionaryValueCollection(entrySet());
    }

    /**
     * @return a set view of the mappings contained in this dictionary.
     */
    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return new DictionaryEntrySet();
    }
}
