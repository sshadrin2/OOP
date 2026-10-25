package ru.nsu.sshadrin2.graph;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Adjacency list representation of a simple directed graph.
 *
 * @param <V> the type of objects stored in vertices.
 */
public class AdjList<V> implements Graph<V> {
    HashMap<V, ArrayList<V>> matrix;

    /**
     * Makes an adjacency list.
     *
     * @param cap initial capacity
     */
    public AdjList(int cap) {
        matrix = new HashMap<>(cap);
    }

    public AdjList() {
        matrix = new HashMap<>();
    }

    private boolean hasVertex(V vertex) {
        return matrix.containsKey(vertex);
    }

    @Override
    public ArrayList<V> vertices() {
        return new ArrayList<>(matrix.keySet());
    }

    @Override
    public void addVertex(V vertex) {
        if (hasVertex(vertex)) {
            return;
        }

        matrix.put(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(V vertex) {
        if (!hasVertex(vertex)) {
            return;
        }
        matrix.remove(vertex);
        for (V from : matrix.keySet()) {
            matrix.get(from).remove(vertex);
        }
    }

    @Override
    public ArrayList<V> adjacentVertices(V vertex) {
        if (!hasVertex(vertex)) {
            throw new IllegalArgumentException("unknown vertex: " + vertex);
        }
        return new ArrayList<>(matrix.get(vertex));
    }

    @Override
    public void addEdge(V from, V to) {
        if (!hasVertex(from)) {
            throw new IllegalArgumentException("unknown vertex: " + from);
        }
        if (!hasVertex(to)) {
            throw new IllegalArgumentException("unknown vertex: " + to);
        }
        if (matrix.get(from).contains(to)) {
            return;
        }
        matrix.get(from).add(to);
    }

    @Override
    public void removeEdge(V from, V to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return;
        }
        matrix.get(from).remove(to);
    }

    @Override
    public void readFromFile(Path path, Function<String, V> parser) throws IOException {
        try (Scanner scanner = new Scanner(path)) {
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            this.matrix = new HashMap<>(capacity);

            String[] tokens = scanner.nextLine().trim().split("\\s+");
            for (String token : tokens) {
                if (!token.isEmpty()) {
                    addVertex(parser.apply(token));
                }
            }

            while (scanner.hasNextLine()) {
                String[] edge = scanner.nextLine().trim().split("\\s+");
                V from = parser.apply(edge[0]);
                V to = parser.apply(edge[1]);
                addVertex(from);
                addVertex(to);
                addEdge(from, to);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        AdjList<?> other = (AdjList<?>) obj;
        return matrix.equals(other.matrix);
    }

    @Override
    public String toString() {
        return matrix.toString();
    }
}
