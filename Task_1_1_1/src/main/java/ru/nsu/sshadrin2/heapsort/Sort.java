package ru.nsu.sshadrin2.heapsort;

/**
 * Heap sort for integer arrays
 */
public class Sort {

    /**
     * Sorts an integer array using heap sort
     * @param array sortable array
     * @return sorted array
     */
    public static int[] sort(int[] array) {
        HeapInt heap = new HeapInt(array.length);
        for (int i = 0; i < array.length; i++) {
            heap.insert(array[i]);
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = heap.extract();
        }
        return array;
    }
}
