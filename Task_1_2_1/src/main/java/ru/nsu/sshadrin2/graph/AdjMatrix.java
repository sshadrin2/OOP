package ru.nsu.sshadrin2.graph;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Adjacency matrix representation for an oriented graph.
 *
 * @param <V> the type of value stored in nodes.
 */
public class AdjMatrix<V> implements Graph<V> {
    private ArrayList<V> vertices;
    private HashMap<V, Integer> indices;
    private boolean[][] matrix;
    private int size;


    /**
     * Makes an adjacency matrix.
     *
     * @param cap maximum matrix capacity
     */
    public AdjMatrix(int cap) {
        this.vertices = new ArrayList<V>();
        indices = new HashMap<>();
        matrix = new boolean[cap][cap];
        size = 0;
    }

    private boolean hasVertex(V vertex) {
        return indices.containsKey(vertex);
    }

    @Override
    public void addVertex(V vertex) {
        if (hasVertex(vertex)) {
            return;
        }
        vertices.add(vertex);
        indices.put(vertex, size);
        size++;
    }

    @Override
    public void removeVertex(V vertex) {
        if (hasVertex(vertex)) {
            int index = indices.get(vertex);
            for (int v = 0; v < size; v++) {
                matrix[v][index] = false;
            }
            indices.remove(vertex);
        }
    }

    @Override
    public ArrayList<V> adjacentVertices(V vertex) {
        ArrayList<V> result = new ArrayList<>();
        if (!hasVertex(vertex)) {
            throw new IllegalArgumentException("unknown vertex: " + vertex);
        }
        int index = indices.get(vertex);
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[index][i]) {
                result.add(vertices.get(i));
            }
        }
        return result;
    }

    @Override
    public void addEdge(V from, V to) {
        if (!hasVertex(from)) {
            throw new IllegalArgumentException("unknown vertex: " + from);
        }
        if (!hasVertex(to)) {
            throw new IllegalArgumentException("unknown vertex: " + to);
        }
        matrix[indices.get(from)][indices.get(to)] = true;
    }

    @Override
    public void removeEdge(V from, V to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return;
        }
        matrix[indices.get(from)][indices.get(to)] = false;
    }

    @Override
    public void readFromFile(Path path, Function<String, V> parser) throws IOException {
        try (Scanner scanner = new Scanner(path)) {
            int capacity = Integer.parseInt(scanner.nextLine().trim());

            this.matrix = new boolean[capacity][capacity];
            this.vertices = new ArrayList<>();
            this.indices = new HashMap<>();
            this.size = 0;

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
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AdjMatrix<?> other = (AdjMatrix<?>) obj;
        if (!indices.keySet().equals(other.indices.keySet())) {
            return false;
        }
        for (V from : indices.keySet()) {
            int indexFrom = indices.get(from);
            int indexFromOther = other.indices.get(from);
            for (V to : indices.keySet()) {
                int indexTo = indices.get(to);
                int indexToOther = other.indices.get(to);
                if (matrix[indexFrom][indexTo] != other.matrix[indexFromOther][indexToOther]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean firstVertex = true;
        for (V from : indices.keySet()) {
            if (!firstVertex) {
                sb.append(", ");
            }
            firstVertex = false;
            sb.append(from).append("=[");
            int i = indices.get(from);
            boolean firstNeighbour = true;
            for (V to : indices.keySet()) {
                int j = indices.get(to);
                if (matrix[i][j]) {
                    if (!firstNeighbour) {
                        sb.append(", ");
                    }
                    firstNeighbour = false;
                    sb.append(to);
                }
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
}
