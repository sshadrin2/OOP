package ru.nsu.sshadrin2.findstring;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for finding a substring in a file.
 * Uses KMP algorithm.
 */
public class OccurrenceFinder {
    private static final int READ_BUFFER_SIZE = 8192;

    /**
     * Finds all occurrences of a substring in a file.
     *
     * @param path      path to a file
     * @param substring substring which occurrences has to be found
     * @return list of all substring occurrences indices in a file
     */
    public static List<Integer> find(String path, String substring) {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(path), StandardCharsets.UTF_8)) {

            List<Integer> list = new ArrayList<>();
            char[] pattern = substring.toCharArray();
            int m = pattern.length;
            int[] lps = buildLps(pattern);

            int matched = 0;          // текущее количество совпавших символов
            int index = 0;            // индекс текущего символа в файле
            char[] buffer = new char[READ_BUFFER_SIZE];
            int read;

            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    char c = buffer[i];

                    while (matched > 0 && c != pattern[matched]) {
                        matched = lps[matched - 1];
                    }
                    if (c == pattern[matched]) {
                        matched++;
                    }
                    if (matched == m) {
                        list.add(index - m + 1);
                        matched = lps[matched - 1];
                    }
                    index++;
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[] buildLps(char[] pattern) {
        int m = pattern.length;
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            if (pattern[i] == pattern[len]) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}