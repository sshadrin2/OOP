package ru.nsu.sshadrin2.graph;

public class CyclicGraphException extends RuntimeException {
    public CyclicGraphException(String message) {
        super(message);
    }
}
