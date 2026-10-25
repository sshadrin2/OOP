package ru.nsu.sshadrin2.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * A simple directed graph interface.
 *
 * @param <V> the type of values stored in nodes
 */
public interface Graph<V> {



    void addVertex(V vertex);

    void removeVertex(V vertex);

    ArrayList<V> adjacentVertices(V vertex);

    void addEdge(V from, V to);

    void removeEdge(V from, V to);

    /**
     * Reads graph from a file.
     * First line must start with a max vertices number.
     * Next line must be a list of not more than this number of different vertices.
     * Each subsequent line must be a pair of vertices which represents an oriented edge.
     *
     * @param path path to a file
     * @param parser mapping from String to a vertex class object.
     */
    void readFromFile(Path path, Function<String, V> parser) throws IOException;



    @Override
    boolean equals(Object obj);

    @Override
    String toString();
}
