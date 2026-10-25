package ru.nsu.sshadrin2.graph;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * Incidence matrix representation of a simple directed graph.
 *
 * @param <V> the type of value stored in nodes.
 */
public class IncMatrix<V> implements Graph<V> {

    private ArrayList<V> vertices;
    private HashMap<V, Integer> indices;
    private ArrayList<int[]> matrix;
    private int size;
    private int cap;
    /**
     * Makes an incidence matrix.
     *
     * @param cap maximum number of vertices.
     */
    public IncMatrix(int cap) {
        this.vertices = new ArrayList<>();
        this.indices = new HashMap<>();
        this.matrix = new ArrayList<>();
        this.size = 0;
        this.cap = cap;
    }

    private boolean hasVertex(V vertex) {
        return indices.containsKey(vertex);
    }

    private boolean hasEdge(int f, int t) {
        for (int[] row : matrix) {
            if (f == t && row[f] == 2) {
                return true;
            }
            if (f != t && row[f] == 1 && row[t] == -1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addVertex(V vertex) {
        if (hasVertex(vertex)) {
            return;
        }
        vertices.add(vertex);
        indices.put(vertex, size++);
    }

    @Override
    public void removeVertex(V vertex) {
        if (!hasVertex(vertex)) {
            return;
        }
        int v = indices.get(vertex);
        for (int e = matrix.size() - 1; e >= 0; e--) {
            if (matrix.get(e)[v] != 0) {
                matrix.remove(e);
            }
        }
        indices.remove(vertex);
    }

    @Override
    public ArrayList<V> adjacentVertices(V vertex) {
        if (!hasVertex(vertex)) {
            throw new IllegalArgumentException("unknown vertex: " + vertex);
        }
        int v = indices.get(vertex);
        ArrayList<V> result = new ArrayList<>();
        for (int[] row : matrix) {
            if (row[v] == 1) {
                for (int u = 0; u < size; u++) {
                    if (row[u] == -1) {
                        result.add(vertices.get(u));
                        break;
                    }
                }
            } else if (row[v] == 2) {
                result.add(vertex);
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
        int f = indices.get(from);
        int t = indices.get(to);
        if (hasEdge(f, t)) {
            return;
        }
        int[] column = new int[cap];
        if (f == t) {
            column[f] = 2;
        } else {
            column[f] = 1;
            column[t] = -1;
        }
        matrix.add(column);
    }

    @Override
    public void removeEdge(V from, V to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return;
        }
        int f = indices.get(from);
        int t = indices.get(to);
        for (int e = 0; e < matrix.size(); e++) {
            int[] row = matrix.get(e);
            boolean match = (f == t && row[f] == 2)
                    || (f != t && row[f] == 1 && row[t] == -1);
            if (match) {
                matrix.remove(e);
                return;
            }
        }
    }

    @Override
    public void readFromFile(Path path, Function<String, V> parser) throws IOException {
        try (Scanner scanner = new Scanner(path)) {
            this.cap = Integer.parseInt(scanner.nextLine().trim());
            this.vertices = new ArrayList<>();
            this.indices = new HashMap<>();
            this.matrix = new ArrayList<>();
            this.size = 0;

            for (String token : scanner.nextLine().trim().split("\\s+")) {
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

    private record Edge<V>(V from, V to) { }

    private Set<Edge<V>> edgeSet() {
        Set<Edge<V>> result = new HashSet<>();
        for (int[] row : matrix) {
            V from = null, to = null;
            for (int v = 0; v < size; v++) {
                switch (row[v]) {
                    case 1 -> from = vertices.get(v);
                    case -1 -> to = vertices.get(v);
                    case 2 -> { from = vertices.get(v); to = vertices.get(v); }
                    default -> { }
                }
            }
            result.add(new Edge<>(from, to));
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IncMatrix<?> other = (IncMatrix<?>) obj;
        if (!indices.keySet().equals(other.indices.keySet())) {
            return false;
        }
        return indices.keySet().equals(other.indices.keySet())
                && edgeSet().equals(other.edgeSet());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IncidenceMatrix{");
        boolean firstVertex = true;
        for (V from : indices.keySet()) {
            if (!firstVertex) {
                sb.append(", ");
            }
            firstVertex = false;
            sb.append(from).append("=[");
            boolean firstNeighbour = true;
            for (V to : adjacentVertices(from)) {
                if (!firstNeighbour) {
                    sb.append(", ");
                }
                firstNeighbour = false;
                sb.append(to);
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
}
