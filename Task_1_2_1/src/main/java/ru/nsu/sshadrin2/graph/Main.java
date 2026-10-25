package ru.nsu.sshadrin2.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        HashMap<String, ArrayList<Integer>> huh = new HashMap<>();
        huh.put("huh", new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3})));
        huh.put("buh", new ArrayList<>(Arrays.asList(new Integer[]{4, 5, 6})));
        System.out.println(huh);
    }
}
