package ru.nsu.sshadrin2.graph;

import java.util.List;

/**
 * Functional topological sort interface.
 */
public interface TopologicalSort {

    /**
     * Sorts graph in a topological order.
     * Mustn't mutate given graph
     *
     * @param graph graph that needs to be sorted
     * @return list of vertices in topological order
     * @param <V> vertices' type
     */
    <V> List<V> sort(Graph<V> graph);
}
