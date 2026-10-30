package ru.nsu.sshadrin2.hashtable;

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
        void putNode(K key, V value) {
            Node<K, V> iter = this;
            while (iter.next != null) {
                if (key.equals(iter.key)) {
                    V prev = iter.value;
                    iter.value = value;
                    return;
                }
                iter = iter.next;
            }
            if (key.equals(iter.key)) {
                V prev = iter.value;
                iter.value = value;
                return;
            }
            iter.addNode(key, value);
            return;
        }

        V getValue(K key) {
            Node<K, V> iter = this;
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
        return key.hashCode() & (size - 1);
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
            table[hash] = new Node<>(key, val);
            return;
        }
        table[hash].putNode(key, val);
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
}
