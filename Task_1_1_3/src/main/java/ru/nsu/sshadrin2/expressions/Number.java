package ru.nsu.sshadrin2.expressions;

public class Number extends Expression {

    private final int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public float eval(String interpretation) {
        return value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public Expression derivative(String var) {
        Expression de = new Number(0);
        return de;
    }
}
