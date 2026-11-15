package ru.nsu.sshadrin2.findstring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class OccurrenceFinderTest {

    @TempDir
    Path tempDir;

    @Nested
    public class SimpleTests {

        private String write(String content) throws IOException {
            Path file = tempDir.resolve("input.txt");
            Files.writeString(file, content, StandardCharsets.UTF_8);
            return file.toString();
        }

        @Test
        void overLapping() throws IOException {
            String file = write("aaaaAbcabc");
            assertEquals(List.of(0, 1, 2), OccurrenceFinder.find(file, "aa"));
            assertEquals(List.of(4), OccurrenceFinder.find(file, "Abc"));
            assertEquals(List.of(7), OccurrenceFinder.find(file, "abc"));
        }

        @Test
        void mixedAlphabets() throws IOException {
            String file = write("абракадабраαβγδγδשלוםשלוםمرحبامرحبا你好你好");

            assertEquals(List.of(1, 8),   OccurrenceFinder.find(file, "бра"));
            assertEquals(List.of(13, 15), OccurrenceFinder.find(file, "γδ"));
            assertEquals(List.of(17, 21), OccurrenceFinder.find(file, "שלום"));
            assertEquals(List.of(25, 30), OccurrenceFinder.find(file, "مرحبا"));
            assertEquals(List.of(35, 37), OccurrenceFinder.find(file, "你好"));
        }

        @Test
        void emptyPattern() throws IOException {
            String file = write("нечто");
            assertEquals(List.of(), OccurrenceFinder.find(file, ""));
        }

        @Test
        void emptyFileAndNotFound() throws IOException {
            assertEquals(List.of(), OccurrenceFinder.find(write(""), "x"));
            assertEquals(List.of(), OccurrenceFinder.find(write("привет"), "пока"));
        }

        @Test
        void patternEqualsWholeFile() throws IOException {
            String file = write("точносовпадает");
            assertEquals(List.of(0), OccurrenceFinder.find(file, "точносовпадает"));
        }

        @Test
        void occurrenceOnBoundary() throws IOException {
            int n = 8192;
            String file = write("x".repeat(n - 3) + "бапбапбап");
            assertEquals(List.of(n - 3), OccurrenceFinder.find(file, "бапбапбап"));
        }
    }

    @Nested
    public class BigTests {
        private static Path generateLargeFile(
                Path dir, int charsLen, String pattern,
                String fillerAlphabet, long seed,
                List<Integer> expectedIndices) throws IOException {

            Path file = dir.resolve("large.txt");
            Random rnd = new Random(seed);
            try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                int pos = 0;
                while (pos < charsLen) {
                    int fillerLen = 500 + rnd.nextInt(1000);
                    for (int i = 0; i < fillerLen && pos < charsLen; i++, pos++) {
                        w.write(fillerAlphabet.charAt(rnd.nextInt(fillerAlphabet.length())));
                    }
                    if (pos + pattern.length() < charsLen) {
                        w.write(pattern);
                        expectedIndices.add(pos);
                        pos += pattern.length();
                    }
                }
            }
            return file;
        }

        @Test
        void largeFile() throws IOException {
            String pattern = "абракадабра";
            String filler = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int targetChars = 1_000_000;

            List<Integer> expected = new ArrayList<>();
            Path file = generateLargeFile(tempDir, targetChars, pattern, filler, 122L, expected);

            assertEquals(expected, OccurrenceFinder.find(file.toString(), pattern));
        }
    }
}