package ru.nsu.sshadrin2.expressions;

public class Sub extends Operation {

    Sub(Expression left, Expression right) {
        super(left, right, '-');
    }

    @Override
    public Expression derivative(String var) {
        Expression deLeft = left.derivative(var);
        Expression deRight = right.derivative(var);
        return new Sub(deLeft, deRight);
    }
}
