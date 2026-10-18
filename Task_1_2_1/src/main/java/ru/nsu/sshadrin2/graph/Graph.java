package ru.nsu.sshadrin2.graph;

import java.util.ArrayList;
import java.nio.file.Path;
import java.io.IOException;
import java.util.function.Function;

public interface Graph<V> {
    void addVertex(V vertex);
    void removeVertex(V vertex);
    ArrayList<V> adjacentVertices(V vertex);

    void addEdge(V from, V to);
    void removeEdge(V from, V to);
    void readFromFile(Path path, Function<String, V> Parser) throws IOException;
}
