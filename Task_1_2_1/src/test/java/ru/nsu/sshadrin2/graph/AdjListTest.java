package ru.nsu.sshadrin2.graph;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

class AdjListTest {

    @Nested
    @DisplayName("addVertex")
    class AddVertex {

        private AdjList<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<>(10);
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

        private AdjList<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<>(10);
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

        private AdjList<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<>(10);
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

        private AdjList<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<>(10);
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

        private AdjList<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<>(10);
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

        private AdjList<String> graph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<>(1);
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

            AdjList<Integer> intGraph = new AdjList<>(1);
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

    @Nested
    @DisplayName("equalsAndHashCode")
    class EqualsAndHashCode {
        private Graph<Integer> graph;
        private Graph<Integer> secondGraph;

        @BeforeEach
        void setUp() {
            graph = new AdjList<Integer>(32);
            secondGraph = new AdjList<Integer>(64);

            graph.addVertex(1);
            graph.addVertex(2);
            graph.addVertex(3);

            secondGraph.addVertex(1);
            secondGraph.addVertex(2);
            secondGraph.addVertex(3);
        }

        @Test void reflexive() {
            graph.addEdge(1, 2);
            graph.addEdge(1, 3);
            graph.addEdge(1, 1);
            assertEquals(graph, graph);
            assertEquals(graph.hashCode(), graph.hashCode());
        }

        @Test
        void symmetricEquals() {
            graph.addEdge(1, 2);
            graph.addEdge(1, 3);
            graph.addEdge(1, 1);

            secondGraph.addEdge(1, 2);
            secondGraph.addEdge(1, 3);
            secondGraph.addEdge(1, 1);

            assertEquals(graph.equals(secondGraph), secondGraph.equals(graph));
            assertEquals(graph.hashCode(), secondGraph.hashCode());
        }

        @Test
        void symmetricNotEqual() {
            graph.addEdge(1, 2);
            graph.addEdge(1, 3);
            graph.addEdge(1, 1);

            secondGraph.addVertex(0);
            secondGraph.addEdge(1, 2);
            secondGraph.addEdge(1, 3);
            secondGraph.addEdge(1, 1);

            assertEquals(graph.equals(secondGraph), secondGraph.equals(graph));
        }

        @Test
        void nullNotEqual() {
            assertFalse(graph.equals(null));
        }

        @Test
        void differentClasses() {
            Graph<String> stringGraph = new AdjMatrix<>(32);
            stringGraph.addVertex("1");
            stringGraph.addVertex("2");
            stringGraph.addVertex("3");
            assertFalse(graph.equals(stringGraph));
        }

        @Test
        void equalAfterVertexDeletion() {
            graph.addVertex(20);
            graph.addVertex(21);
            graph.addVertex(22);
            graph.addEdge(20, 21);
            graph.addEdge(20, 22);
            graph.addEdge(20, 1);
            graph.addEdge(20, 2);
            graph.addEdge(20, 3);
            graph.addEdge(1, 20);
            graph.addEdge(1, 21);
            graph.addEdge(1, 22);

            graph.removeVertex(20);
            graph.removeVertex(21);
            graph.addVertex(20);
            graph.removeVertex(20);
            graph.removeVertex(22);
            assertEquals(graph, secondGraph);
            assertEquals(graph.hashCode(), secondGraph.hashCode());
        }

        @Test
        void equalAfterEdgeDeletion() {
            graph.addEdge(1, 2);
            graph.addEdge(1, 3);
            graph.addEdge(1, 1);
            graph.addEdge(3, 1);
            graph.removeEdge(2, 1);
            graph.removeEdge(1, 2);
            graph.removeEdge(1, 3);
            graph.removeEdge(3, 1);
            graph.removeEdge(1, 1);
            assertEquals(graph, secondGraph);
            assertEquals(graph.hashCode(), secondGraph.hashCode());
        }

        @Test
        void missingEdgesNotEqual() {
            graph.addEdge(1, 2);
            graph.addEdge(1, 3);
            graph.addEdge(2, 3);

            secondGraph.addEdge(1, 2);
            secondGraph.addEdge(1, 3);
            secondGraph.addEdge(1, 2);

            assertNotEquals(graph, secondGraph);
        }
    }
}