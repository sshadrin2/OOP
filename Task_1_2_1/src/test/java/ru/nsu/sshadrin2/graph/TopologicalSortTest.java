package ru.nsu.sshadrin2.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TopologicalSortTest {

    static List<TopologicalSort> sorters() {
        return List.of(
                new KahnSort(),
                new DfsSort()
        );
    }

    @SafeVarargs
    static <V> Graph<V> graphOf(V... vertices) {
        Graph<V> g = new AdjMatrix<>(100);
        for (V v : vertices) {
            g.addVertex(v);
        }
        return g;
    }

    static <V> void assertValidTopologicalOrder(
            TopologicalSort sorter, Graph<V> graph, List<V> order) {
        String label = sorter.getClass().getSimpleName();
        List<V> vertices = graph.vertices();
        assertEquals(vertices.size(), order.size(),
                label + ": order must have one entry per vertex");
        assertEquals(new HashSet<>(vertices), new HashSet<>(order),
                label + ": order must contain exactly the graph's vertices");

        Map<V, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }
        for (V from : vertices) {
            for (V to : graph.adjacentVertices(from)) {
                assertTrue(position.get(from) < position.get(to),
                        label + ": edge " + from + " -> " + to
                                + " violates the order " + order);
            }
        }
    }

    @Nested
    class EmptyGraph {

        @Test
        void returnsEmptyList() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf();
                assertTrue(sorter.sort(g).isEmpty(),
                        sorter.getClass().getSimpleName());
            }
        }
    }

    @Nested
    class SingleVertex {

        @Test
        void returnsSingleton() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a");
                assertEquals(List.of("a"), sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }
    }

    @Nested
    class LinearChain {

        @Test
        void returnsExactOrder() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c");
                g.addEdge("a", "b");
                g.addEdge("b", "c");
                assertEquals(List.of("a", "b", "c"), sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }
    }

    @Nested
    class Diamond {

        @Test
        void returnsValidOrder() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c", "d");
                g.addEdge("a", "b");
                g.addEdge("a", "c");
                g.addEdge("b", "d");
                g.addEdge("c", "d");
                assertValidTopologicalOrder(sorter, g, sorter.sort(g));
            }
        }
    }

    @Nested
    class Disconnected {

        @Test
        void twoComponentsBothOrdered() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c", "d");
                g.addEdge("a", "b");
                g.addEdge("c", "d");
                assertValidTopologicalOrder(sorter, g, sorter.sort(g));
            }
        }

        @Test
        void isolatedVerticesAreIncluded() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c", "lonely");
                g.addEdge("a", "b");
                g.addEdge("b", "c");
                List<String> order = sorter.sort(g);
                assertValidTopologicalOrder(sorter, g, order);
                assertTrue(order.contains("lonely"),
                        sorter.getClass().getSimpleName());
            }
        }
    }

    @Nested
    class MultipleSources {

        @Test
        void allSourcesAppearBeforeSinks() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c");
                g.addEdge("a", "c");
                g.addEdge("b", "c");
                List<String> order = sorter.sort(g);
                assertValidTopologicalOrder(sorter, g, order);
                assertTrue(order.indexOf("a") < order.indexOf("c"),
                        sorter.getClass().getSimpleName());
                assertTrue(order.indexOf("b") < order.indexOf("c"),
                        sorter.getClass().getSimpleName());
            }
        }
    }

    @Nested
    class LargerDag {

        @Test
        void returnsValidOrder() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c", "d", "e", "f");
                g.addEdge("a", "b");
                g.addEdge("a", "c");
                g.addEdge("b", "d");
                g.addEdge("c", "d");
                g.addEdge("d", "e");
                g.addEdge("e", "f");
                assertValidTopologicalOrder(sorter, g, sorter.sort(g));
            }
        }
    }

    @Nested
    class Cycles {

        @Test
        void twoCycleThrows() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b");
                g.addEdge("a", "b");
                g.addEdge("b", "a");
                assertThrows(CyclicGraphException.class,
                        () -> sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }

        @Test
        void threeCycleThrows() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c");
                g.addEdge("a", "b");
                g.addEdge("b", "c");
                g.addEdge("c", "a");
                assertThrows(CyclicGraphException.class,
                        () -> sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }

        @Test
        void selfLoopThrows() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a");
                g.addEdge("a", "a");
                assertThrows(CyclicGraphException.class,
                        () -> sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }

        @Test
        void cycleWithTailThrows() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c", "d");
                g.addEdge("a", "b");
                g.addEdge("b", "c");
                g.addEdge("c", "b");
                g.addEdge("d", "a");
                assertThrows(CyclicGraphException.class,
                        () -> sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }
    }

    @Nested
    class Statelessness {

        @Test
        void sameSorterHandlesDifferentVertexTypes() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> strings = graphOf("a", "b");
                strings.addEdge("a", "b");

                Graph<Integer> ints = graphOf(1, 2);
                ints.addEdge(1, 2);

                assertEquals(List.of("a", "b"), sorter.sort(strings),
                        sorter.getClass().getSimpleName());
                assertEquals(List.of(1, 2), sorter.sort(ints),
                        sorter.getClass().getSimpleName());
            }
        }

        @Test
        void repeatedCallsOnSameGraphGiveSameResult() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b", "c");
                g.addEdge("a", "b");
                g.addEdge("b", "c");
                assertEquals(sorter.sort(g), sorter.sort(g),
                        sorter.getClass().getSimpleName());
            }
        }

        @Test
        void doesNotMutateGraph() {
            for (TopologicalSort sorter : sorters()) {
                Graph<String> g = graphOf("a", "b");
                g.addEdge("a", "b");
                sorter.sort(g);
                assertEquals(List.of("b"), g.adjacentVertices("a"),
                        sorter.getClass().getSimpleName());
                assertEquals(2, g.vertices().size(),
                        sorter.getClass().getSimpleName());
            }
        }
    }
}