package ru.nsu.sshadrin2.graph;

import java.util.List;

public interface TopologicalSort {

    <V> List<V> sort(Graph<V> graph);
}
