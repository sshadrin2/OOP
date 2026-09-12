package ru.nsu.sshadrin2.heapsort;

public class Sort {

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
