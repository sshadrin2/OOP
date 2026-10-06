package ru.nsu.sshadrin2.expressions;

public class Mul extends Operation {

    public Mul(Expression left, Expression right) {
        super(left, right, '*');
    }

    @Override
    public Expression derivative(String var) {
        Expression deLeft = left.derivative(var);
        Expression deRight = right.derivative(var);

        Expression monomeLeft = new Mul(deLeft, right);
        Expression monomeRight = new Mul(left, deRight);

        return new Add(monomeLeft, monomeRight);
    }
}
