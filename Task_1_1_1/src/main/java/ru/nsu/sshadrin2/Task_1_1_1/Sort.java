package ru.nsu.sshadrin2.Task_1_1_1;

import java.util.PriorityQueue;

public class Sort {
    public static int[] sort(int[] array) {
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int i = 0; i < array.length; i++) {
            heap.add(array[i]);
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = heap.remove();
        }
        return array;
    }
}
