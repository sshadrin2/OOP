package ru.nsu.sshadrin2.hashtable;

import java.util.Arrays;
import java.util.Objects;

/**
 * Hash table that allows various operations with key/value mappings with constant time.
 * Key must be not null
 *
 * @param <K>
 * @param <V>
 */
public class HashTable<K, V> {
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }

        void addNode(K key, V value) {
            next = new Node<>(key, value);
        }


        boolean putNode(K key, V value) { // returns true if the key wasn't present
            Node<K, V> iter = this;
            while (iter.next != null) {
                if (key.equals(iter.key)) {
                    iter.value = value;
                    return false;
                }
                iter = iter.next;
            }
            if (key.equals(iter.key)) {
                V prev = iter.value;
                iter.value = value;
                return false;
            }
            iter.addNode(key, value);
            return true;
        }

        boolean removeNode(K key) { // returns true if the key was present.
            Node<K, V> iter = this;
            while (iter.next != null) {
                if (key.equals(iter.next.key)) {
                    iter.next = iter.next.next;
                    return true;
                }
                iter = iter.next;
            }

            return false;
        }

        boolean containsNode(K key) {
            Node<K, V> iter = this.next;
            while (iter != null) {
                if (key.equals(iter.key)) {
                    return true;
                }
                iter = iter.next;
            }
            return false;
        }

        V getValue(K key) {
            Node<K, V> iter = this.next;
            while (iter != null) {
                if (iter.key.equals(key)) {
                    return iter.value;
                }
                iter = iter.next;
            }
            return null;
        }


    }

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE = 16;

    private Node<K, V>[] table;
    private final float loadFactor;
    private int size = DEFAULT_SIZE;
    private int nodeCount = 0;
    private int threshold;

    private int getHash(K key) {
        int hash = key.hashCode() & (size - 1);
        return hash ^ (hash >>> 16);
    }

    /**
     * Constructs hashtable with default load factor.
     */
    public HashTable() {
        this(DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs hashtable with given load factor.
     *
     * @param loadFactor load factor of a table
     */
    @SuppressWarnings("unchecked")
    public HashTable(float loadFactor) {
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        threshold = (int) (loadFactor * size);
        table = (Node<K, V>[]) new Node[size];
    }

    /**
     * Puts key/value mapping in the table.
     * Overwrites mapping value if it was already present.
     *
     * @param key key of a mapping
     * @param val value of a mapping
     */
    public void put(K key, V val) {
        if (key == null) {
            return;
        }
        int hash = getHash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(null, null);
            table[hash].addNode(key, val);
            nodeCount++;
            return;
        }
        if (table[hash].putNode(key, val)) {
            nodeCount++;
        }
    }

    /**
     * Gets value mapped by a key.
     * Returns null if there is no mapping with such key.
     *
     * @param key key of a mapping
     * @return value of a mapping or null if there is none
     * @throws IllegalArgumentException if key is null
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be non null.");
        }
        int hash = getHash(key);
        if (table[hash] == null) {
            return null;
        }
        return table[hash].getValue(key);
    }

    /**
     * Checks if mapping is present in the table.
     *
     * @param key key of a mapping
     * @return true if there is a mapping with a given key and false otherwise
     */
    public boolean contains(K key) {
        if (key == null) {
            return false;
        }
        int hash = getHash(key);
        if (table[hash] == null) {
            return false;
        }
        return table[hash].containsNode(key);
    }

    /**
     * Removes a mapping from the table.
     * Doesn't remove anything if there is no such mapping.
     *
     * @param key key of a mapping
     */
    public void remove(K key) {
        if (key == null) {
            return;
        }
        int hash = getHash(key);
        if (table[hash] == null) {
            return;
        }
        if (table[hash].removeNode(key)) {
            nodeCount--;
        }
    }

    /**
     * Updates value of an existing mapping.
     *
     * @param key key of a mapping
     * @param value new value of a mapping
     * @return true if a mapping with given key was present and false otherwise
     */
    public boolean update(K key, V value) {
        if (contains(key)) {
            put(key, value);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashTable<K, V> t = (HashTable<K, V>) o;
        if (nodeCount != t.nodeCount) {
            return false;
        }
        for (Node<K, V> head : table) {
            if (head == null) {
                continue;
            }
            for (Node<K, V> iter = head.next; iter != null; iter = iter.next) {
                K key = iter.key;
                if (!t.contains(key)) {
                    return false;
                }
                V value = iter.value;
                V otherValue = t.get(key);
                if (value == null) {
                    if (t.get(key) != null) {
                        return false;
                    }
                } else {
                    if (!value.equals(t.get(key))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Node<K, V> head : table) {
            if (head == null) {
                continue;
            }
            for (Node<K, V> iter = head.next; iter != null; iter = iter.next) {
                K key = iter.key;
                V value = iter.value;
                hash += key.hashCode() ^ (value == null ? 0 : value.hashCode());
            }
        }
        return hash;
    }
}
