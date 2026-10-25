package ru.nsu.sshadrin2.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KahnSort implements TopologicalSort {

    @Override
    public <V> List<V> sort(Graph<V> graph) {
        ArrayList<V> vertices = graph.vertices();

        Map<V, Integer> inDegree = new HashMap<>();
        for (V v : vertices) {
            inDegree.put(v, 0);
        }

        for (V from : vertices) {
            for (V to : graph.adjacentVertices(from)) {
                inDegree.compute(to, (k, v) -> v + 1);
            }
        }

        Deque<V> ready = new ArrayDeque<>();
        for (Map.Entry<V, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                ready.add(e.getKey());
            }
        }

        List<V> order = new ArrayList<>(vertices.size());
        while (!ready.isEmpty()) {
            V v = ready.poll();
            order.add(v);
            for (V to : graph.adjacentVertices(v)) {
                if (inDegree.compute(to, (k, val) -> val - 1) == 0) {
                    ready.add(to);
                }
            }
        }

        if (order.size() != vertices.size()) {
            throw new CyclicGraphException(
                    "graph contains a cycle: only " + order.size()
                            + " of " + vertices.size() + " vertices were ordered");
        }
        return order;
    }
}
