package ru.nsu.sshadrin2.heapsort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SortTest {

    @Test
    void emptyArray() {
        int[] array = {};
        var result = Sort.sort(array);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void singleElementArray() {
        int[] array = {10};
        var result = Sort.sort(array);
        assertArrayEquals(new int[]{10}, result);
    }

    @Test
    void descendingArray() {
        int[] array = {5, 4, 3, 2, 1};
        var result = Sort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void minMaxValuesArray() {
        int[] array = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        var result = Sort.sort(array);
        assertArrayEquals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, result);
    }

    @Test
    void randomArray() {
        Random rnd = new Random(44);
        int[] array = new int[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = rnd.nextInt();
        }
        var result = Sort.sort(array);
        Arrays.sort(array);
        assertArrayEquals(array, result);
    }
}