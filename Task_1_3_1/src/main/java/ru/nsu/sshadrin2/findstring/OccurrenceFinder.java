package ru.nsu.sshadrin2.findstring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for finding substring occurrences in a file.
 * Uses KMP algorithm.
 */
public class OccurrenceFinder {
    private static final int READ_BUFFER_SIZE = 8192;


    /**
     * Finds all occurences of a substring in a file.
     * Uses READ_BUFFER_SIZE as BufferedReader buffer size
     *
     * @param path path to a file
     * @param substring substring which occurrences has to be found
     * @return list of all substring occurrences indices in a file
     */
    public static List<Integer> find(String path, String substring) {
        return find(path, substring, READ_BUFFER_SIZE);
    }

    /**
     * Finds all occurrences of a substring in a file.
     *
     * @param path      path to a file
     * @param substring substring which occurrences has to be found
     * @param bufSize size of a BufferedReader buffer
     * @return list of all substring occurrences indices in a file
     */
    public static List<Integer> find(String path, String substring, int bufSize) {
        if (substring == null || substring.isEmpty()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(Path.of(path)),
                        StandardCharsets.UTF_8),
                bufSize)) {

            List<Integer> list = new ArrayList<>();
            char[] pattern = substring.toCharArray();
            int m = pattern.length;
            int[] lps = buildLps(pattern);

            int matched = 0;
            int index = 0;
            char[] buffer = new char[bufSize];
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
            throw new FindException("Failed to read file: " + path, e);
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

    /**
     * File not found exception.
     */
    public static class FindException extends RuntimeException {
        public FindException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
