package ru.nsu.sshadrin2.Task_1_1_1;

class HeapInt {
    private int[] heap;
    private int size;
    private int cap;
    public HeapInt(int cap) {
        this.cap = cap;
        this.size = 0;
        this.heap = new int[cap];
        for (int i = 0; i < cap; i++) {
            heap[i] = Integer.MAX_VALUE;
        }
    }
    private void siftUp(int index) {
        while (index > 0) {
            int v = (index - 1) / 2;
            if (heap[v] <= heap[index]) {
                return;
            }
            int temp = heap[v];
            heap[v] = heap[index];
            heap[index] = temp;
            index = v;
        }
    }
    private void siftDown(int index) {
        while (index < size) {
            int l = 2 * index + 1;
            int r = 2 * index + 2;
            int min_index = index;
            if (l < size && heap[l] < heap[min_index]) {
                min_index = l;
            }
            if (r < size && heap[r] < heap[min_index]) {
                min_index = r;
            }
            if (index == min_index) return;
            int temp = heap[min_index];
            heap[min_index] = heap[index];
            heap[index] = temp;
            index = min_index;
        }
    }
    public void insert(int value) {
        heap[size++] = value;
        siftUp(size - 1);
    }
    public int extract() {
        int res = heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = Integer.MAX_VALUE;
        siftDown(0);
        return res;
    }

}
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
