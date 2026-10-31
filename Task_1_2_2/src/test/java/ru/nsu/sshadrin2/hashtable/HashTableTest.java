package ru.nsu.sshadrin2.hashtable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HashTableTest {


    @Nested
    class BasicOpeartions {
        HashTable<String, Integer> table;

        @BeforeEach
        void setUp() {
            table = new HashTable<>();
        }

        @Test
        void getExistingMappings() {
            table.put("a", 1);
            table.put("b", 10);
            table.put("abba", 1221);
            int a = table.get("a");
            int b = table.get("b");
            int c = table.get("abba");
            assertEquals(1, a);
            assertEquals(10, b);
            assertEquals(1221, c);
            assertTrue(table.contains("a"));
            assertTrue(table.contains("b"));
            assertTrue(table.contains("abba"));
        }

        @Test
        void overwriteMappings() {
            table.put("a", 0);
            table.put("b", null);
            table.put("c", null);
            table.put("d", null);

            assertEquals(0, table.get("a"));
            table.put("a", 1221);
            assertEquals(1221, table.get("a"));
            table.put("a", 12211221);
            assertEquals(12211221, table.get("a"));
        }

        @Test
        void updateMappings() {
            table.put("a", 0);
            assertEquals(0, table.get("a"));
            assertTrue(table.update("a", 1221));
            assertEquals(1221, table.get("a"));
            assertTrue(table.update("a", 12211221));
            assertEquals(12211221, table.get("a"));
            assertFalse(table.update("wiwiwi", 1221));
        }

        @Test
        void smallTableRemove() {
            table.remove("huh");
            table.put("buh", 0);
            table.put("juh", null);
            table.put("wa", -1);
            table.remove("buh");
            assertFalse(table.contains("buh"));
            table.remove("juh");
            assertFalse(table.contains("juh"));
            table.remove("wa");
            assertFalse(table.contains("wa"));
            table.put("buh", 0);
            table.put("juh", null);
            table.put("wa", -1);
            assertTrue(table.contains("buh"));
            assertTrue(table.contains("juh"));
            assertTrue(table.contains("wa"));
        }

        @Test
        void emptyTableGet() {
            assertNull(table.get("ababa"));
            assertThrows(IllegalArgumentException.class, () -> table.get(null));
            assertNull(table.get("ababababab"));
            assertNull(table.get("aaaaaaaaaaaaaa"));
            assertNull(table.get(""));
        }
        @Test
        void emptyTableContains() {
            assertFalse(table.contains(null));
            assertFalse(table.contains("buh"));
            assertFalse(table.contains("waaaaaaaa"));
            assertFalse(table.contains("a"));
            assertFalse(table.contains(""));
        }

        @Test
        void emptyTableRemove() {
            table.remove(null);
            table.remove("");
            table.remove("null");
            table.remove("wiwiwi");
        }

        @Test
        void nonExistingMappings() {
            table.put("a", 0);
            table.put("b", 1);
            table.remove("bibi");
            table.remove("44444444444444");
            assertNull(table.get("abababa"));
            assertNull(table.get(""));
            assertFalse(table.contains("abababa"));
            assertFalse(table.contains(""));
        }

        @Test
        void largeTable() {
            int size = 10000;
            String[] strArray = new String[size];
            String[] missingStrArray = new String[size];
            for (int i = 0; i < size; i++) {
                strArray[i] = String.valueOf(i);
                missingStrArray[i] =  String.valueOf(i + size);
                table.put(strArray[i], i);
            }
            for (int i = 0; i < size; i++) {
                assertEquals(i, table.get(strArray[i]));
                assertTrue(table.contains(strArray[i]));
            }
            for (int i = 0; i < size; i++) {
                assertNull(table.get(missingStrArray[i]));
                assertFalse(table.contains(missingStrArray[i]));
            }
        }

        @Test
        void largeTableRemove() {
            int size = 10000;
            String[] strArray = new String[size];
            String[] missingStrArray = new String[size];
            for (int i = 0; i < size; i++) {
                strArray[i] = String.valueOf(i);
                missingStrArray[i] =  String.valueOf(i + size);
                table.put(strArray[i], i);
            }
            for (int i = 0; i < size; i++) {
                table.remove(strArray[i]);
                assertNull(table.get(strArray[i]));
                assertFalse(table.contains(strArray[i]));
            }
        }

        @Test
        void largeTableRemoveNonExisting() {
            int size = 100;
            String[] strArray = new String[size];
            String[] missingStrArray = new String[size];
            for (int i = 0; i < size; i++) {
                strArray[i] = String.valueOf(i);
                missingStrArray[i] =  String.valueOf(i + size);
                table.put(strArray[i], i);
            }
            for (int i = 0; i < size; i++) {
                table.remove(missingStrArray[i]);
                for (int j = 0; j < size; j++) {
                    assertEquals(i, table.get(strArray[i]));
                }
            }
        }
    }
    @Nested
    class EqualsAndHashCode {
        HashTable<String, Integer> tableA;
        HashTable<String, Integer> tableB;

        @BeforeEach
        void setUp() {
            tableA = new HashTable<>(0.75f);
            tableB = new HashTable<>(0.5f);
        }

        void assertHashCodeAndEq(HashTable<String, Integer> a, HashTable<String, Integer> b) {
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void emptyEquals() {
            assertHashCodeAndEq(tableA, tableB);
        }

        @Test
        void smallEquals() {
            tableA.put("bibi", 123);
            tableA.put("bibi", 321);
            tableA.put("", 1234);
            tableB.put("", 1234);
            tableB.put("bibi", 321);
        }
        @Test
        void largeTableEquals() {
            int size = 10000;
            String[] strArray = new String[size];
            for (int i = 0; i < size; i++) {
                strArray[i] = String.valueOf(i);
                tableA.put(strArray[i], i);
            }
            for (int i = size - 1; i >= 0; i--) {
                strArray[i] = String.valueOf(i);
                tableB.put(strArray[i], i);
            }
            assertHashCodeAndEq(tableA, tableB);
        }
    }

    @Nested
    class toString {
        HashTable<String, Integer> table;

        @BeforeEach
        void setUp() {
            table = new HashTable<>();
        }

        @Test
        void emptyTable() {
            assertEquals("{}", table.toString());
        }

        @Test
        void smallTable() {
            String[] strings = {"a", "aga", "bibi", "waaaaaa", "uhh"};
            for (int i = 0; i < strings.length; i++) {
                table.put(strings[i], i);
            }
            String string = table.toString();
            for (int i = 0; i < strings.length; i++) {
                assertTrue(string.contains(strings[i] + '=' + i));
            }
        }
    }
}