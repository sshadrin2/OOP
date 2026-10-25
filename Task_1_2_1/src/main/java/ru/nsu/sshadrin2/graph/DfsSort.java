package ru.nsu.sshadrin2.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DfsSort implements TopologicalSort {
    private enum Color { WHITE, GREY, BLACK }

    @Override
    public <V> List<V> sort(Graph<V> graph) {
        ArrayList<V> vertices = graph.vertices();

        Map<V, Color> color = new HashMap<>();
        for (V v : vertices) {
            color.put(v, Color.WHITE);
        }

        List<V> order = new ArrayList<>(vertices.size());
        for (V v : vertices) {
            if (color.get(v) == Color.WHITE) {
                dfs(graph, v, color, order);
            }
        }

        Collections.reverse(order);
        return order;
    }

    private <V> void dfs(Graph<V> graph, V v, Map<V, Color> color, List<V> order) {
        color.put(v, Color.GREY);
        for (V to : graph.adjacentVertices(v)) {
            Color c = color.get(to);
            if (c == Color.GREY) {
                throw new CyclicGraphException(
                        "graph contains a cycle: back edge " + v + " -> " + to);
            }
            if (c == Color.WHITE) {
                dfs(graph, to, color, order);
            }
        }
        color.put(v, Color.BLACK);
        order.add(v);
    }
}
