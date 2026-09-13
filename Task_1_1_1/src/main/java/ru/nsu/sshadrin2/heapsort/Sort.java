package ru.nsu.sshadrin2.heapsort;

/**
 * Heap sort for integer arrays.
 */
public class Sort {
    private static class HeapInt {
        private int[] heap;
        private int size;
        private int cap;

        /**
         * Constructs binary heap with a constant capacity.
         *
         * @param cap heap capacity
         */
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
                int minIndex = index;
                if (l < size && heap[l] < heap[minIndex]) {
                    minIndex = l;
                }
                if (r < size && heap[r] < heap[minIndex]) {
                    minIndex = r;
                }
                if (index == minIndex) {
                    return;
                }
                int temp = heap[minIndex];
                heap[minIndex] = heap[index];
                heap[index] = temp;
                index = minIndex;
            }
        }

        /**
         * Inserts an integer into a heap.
         *
         * @param value insertable value
         */
        public void insert(int value) {
            heap[size++] = value;
            siftUp(size - 1);
        }

        /**
         * Extracts min element from the heap.
         *
         * @return min element
         */
        public int extract() {
            final int res = heap[0];
            size--;
            heap[0] = heap[size];
            heap[size] = Integer.MAX_VALUE;
            siftDown(0);
            return res;
        }
    }

    /**
     * Sorts an integer array using heap sort.
     *
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
