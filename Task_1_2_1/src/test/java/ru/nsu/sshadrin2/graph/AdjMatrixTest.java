package ru.nsu.sshadrin2.graph;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AdjMatrixTest {

    @Nested
    @DisplayName("addVertex")
    class AddVertex {

        private AdjMatrix<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjMatrix<>(10);
        }

        @Test
        void emptyGraphNoVertices() {
            assertThrows(IllegalArgumentException.class, () -> graph.adjacentVertices("a"));
        }

        @Test
        void makesVertexKnown() {
            graph.addVertex("a");
            assertDoesNotThrow(() -> graph.adjacentVertices("a"));
        }

        @Test
        void duplicateIsIgnored() {
            graph.addVertex("a");
            graph.addVertex("a");
            assertDoesNotThrow(() -> graph.adjacentVertices("a"));
        }
    }

    @Nested
    @DisplayName("adjacentVertices")
    class AdjacentVertices {

        private AdjMatrix<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjMatrix<>(10);
        }

        @Test
        void isolatedVertexIsEmpty() {
            graph.addVertex("a");
            assertTrue(graph.adjacentVertices("a").isEmpty());
        }

        @Test
        void unknownVertexThrows() {
            assertThrows(IllegalArgumentException.class, () -> graph.adjacentVertices("b"));
        }
    }

    @Nested
    @DisplayName("addEdge")
    class AddEdge {

        private AdjMatrix<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjMatrix<>(10);
        }

        @Test
        void connectsVertices() {
            graph.addVertex("a");
            graph.addVertex("b");
            graph.addEdge("a", "b");
            assertEquals(List.of("b"), graph.adjacentVertices("a"));
        }

        @Test
        void isOriented() {
            graph.addVertex("a");
            graph.addVertex("b");
            graph.addEdge("a", "b");
            assertTrue(graph.adjacentVertices("b").isEmpty());
        }

        @Test
        void unknownFromThrows() {
            graph.addVertex("b");
            assertThrows(IllegalArgumentException.class, () -> graph.addEdge("x", "b"));
        }

        @Test
        void unknownToThrows() {
            graph.addVertex("a");
            assertThrows(IllegalArgumentException.class, () -> graph.addEdge("a", "x"));
        }

        @Test
        void selfLoopIsAllowed() {
            graph.addVertex("a");
            graph.addEdge("a", "a");
            assertEquals(List.of("a"), graph.adjacentVertices("a"));
        }

        @Test
        void multipleNeighbours() {
            graph.addVertex("a");
            graph.addVertex("b");
            graph.addVertex("c");
            graph.addEdge("a", "b");
            graph.addEdge("a", "c");
            assertEquals(List.of("b", "c"), graph.adjacentVertices("a"));
        }
    }

    @Nested
    @DisplayName("removeEdge")
    class RemoveEdge {

        private AdjMatrix<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjMatrix<>(10);
            graph.addVertex("a");
            graph.addVertex("b");
            graph.addVertex("c");
        }

        @Test
        void removesConnection() {
            graph.addEdge("a", "b");
            graph.removeEdge("a", "b");
            assertTrue(graph.adjacentVertices("a").isEmpty());
        }

        @Test
        void unknownVerticesIsNoOp() {
            graph.addEdge("a", "b");

            graph.removeEdge("a", "unknown");
            graph.removeEdge("unknown", "b");
            graph.removeEdge("unknown", "unknown");

            assertEquals(List.of("b"), graph.adjacentVertices("a"));
        }

        @Test
        void doesNotAffectOtherEdges() {
            graph.addEdge("a", "b");
            graph.addEdge("a", "c");
            graph.removeEdge("a", "b");
            assertEquals(List.of("c"), graph.adjacentVertices("a"));
        }
    }

    @Nested
    @DisplayName("removeVertex")
    class RemoveVertex {

        private AdjMatrix<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjMatrix<>(10);
            graph.addVertex("a");
            graph.addVertex("b");
        }

        @Test
        void makesItUnknown() {
            graph.removeVertex("a");
            assertThrows(IllegalArgumentException.class, () -> graph.adjacentVertices("a"));
        }

        @Test
        void clearsIncomingEdges() {
            graph.addEdge("b", "a");
            graph.removeVertex("a");
            assertTrue(graph.adjacentVertices("b").isEmpty());
        }

        @Test
        void unknownIsNoOp() {
            assertDoesNotThrow(() -> graph.removeVertex("unknown"));
        }
    }

    @Nested
    @DisplayName("readFromFile")
    class ReadFromFile {

        @TempDir
        Path tmp;

        private AdjMatrix<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjMatrix<>(1);
        }

        private Path write(String... lines) throws IOException {
            Path file = tmp.resolve("graph.txt");
            Files.writeString(file, String.join("\n", lines) + "\n");
            return file;
        }

        @Test
        void readsVerticesAndEdges() throws IOException {
            Path file = write(
                    "5",
                    "a b c",
                    "a b",
                    "b c"
            );

            graph.readFromFile(file, s -> s);

            assertEquals(List.of("b"), graph.adjacentVertices("a"));
            assertEquals(List.of("c"), graph.adjacentVertices("b"));
            assertTrue(graph.adjacentVertices("c").isEmpty());
        }

        @Test
        void handlesIntegers() throws IOException {
            Path file = write(
                    "10",
                    "1 2 3",
                    "1 2",
                    "2 3",
                    "3 1"
            );

            AdjMatrix<Integer> intGraph = new AdjMatrix<>(1);
            intGraph.readFromFile(file, Integer::parseInt);

            assertEquals(List.of(2), intGraph.adjacentVertices(1));
            assertEquals(List.of(3), intGraph.adjacentVertices(2));
            assertEquals(List.of(1), intGraph.adjacentVertices(3));
        }

        @Test
        void verticesOnly_noEdges() throws IOException {
            Path file = write(
                    "5",
                    "a b c"
            );

            graph.readFromFile(file, s -> s);

            assertTrue(graph.adjacentVertices("a").isEmpty());
            assertTrue(graph.adjacentVertices("b").isEmpty());
            assertTrue(graph.adjacentVertices("c").isEmpty());
        }

        @Test
        void replacesPreviousState() throws IOException {
            graph.addVertex("old");

            Path file = write(
                    "5",
                    "x y",
                    "x y"
            );
            graph.readFromFile(file, s -> s);

            assertThrows(IllegalArgumentException.class, () -> graph.adjacentVertices("old"));
            assertEquals(List.of("y"), graph.adjacentVertices("x"));
        }

        @Test
        void orientedEdgesNotBidirectional() throws IOException {
            Path file = write(
                    "5",
                    "a b",
                    "a b"
            );

            graph.readFromFile(file, s -> s);

            assertEquals(List.of("b"), graph.adjacentVertices("a"));
            assertTrue(graph.adjacentVertices("b").isEmpty());
        }
    }
}