package ru.nsu.sshadrin2.expressions;

import java.util.HashMap;

/**
 * Constant integer number expression.
 */
public class Number extends Expression {

    private final int value;

    /**
     * Creates constant number expression.
     *
     * @param value value of a number
     */
    public Number(int value) {
        this.value = value;
    }

    @Override
    public float eval(HashMap<String, Integer> interpretation) {
        return value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }
}
