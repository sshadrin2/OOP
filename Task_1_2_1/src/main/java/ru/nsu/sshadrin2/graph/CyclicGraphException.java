package ru.nsu.sshadrin2.graph;

/**
 * Throw to indicate there is a cycle in a graph.
 */
public class CyclicGraphException extends RuntimeException {
    public CyclicGraphException(String message) {
        super(message);
    }
}
