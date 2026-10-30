package ru.nsu.sshadrin2.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {


    @Nested
    class BasicOpeartions {
        HashTable<String, Integer> table;

        @BeforeEach
        void setUp() {
            table = new HashTable<>();
        }

        @Test
        void getMappings() {
            table.put("a", 1);
            table.put("b", 10);
            table.put("abba", 1221);
            int a = table.get("a");
            int b = table.get("b");
            int c = table.get("abba");
            assertEquals(1, a);
            assertEquals(10, b);
            assertEquals(1221, c);
        }

        @Test
        void overwriteMappings() {
            table.put("a", 0);
            assertEquals(0, table.get("a"));
            table.put("a", 1221);
            assertEquals(1221, table.get("a"));
            table.put("a", 12211221);
            assertEquals(12211221, table.get("a"));
        }

        @Test
        void emptyTable() {
            assertNull(table.get("ababa"));
            assertThrows(IllegalArgumentException.class, () -> table.get(null));
            assertNull(table.get("ababababab"));
            assertNull(table.get("aaaaaaaaaaaaaa"));
            assertNull(table.get(""));
        }

        @Test
        void getNonExistingMappings() {
            table.put("a", 0);
            table.put("b", 1);
            assertNull(table.get("abababa"));
        }


    }
}