package ru.nsu.sshadrin2.expressions;

public class Div extends Operation{

    Div(Expression left, Expression right) {
        super(left, right, '/');
    }

    @Override
    public Expression derivative(String var) {
        Expression deLeft = left.derivative(var);
        Expression deRight = right.derivative(var);

        Expression monomeLeft = new Mul(deLeft, right);
        Expression monomeRight = new Mul(left, deRight);

        Expression numerator = new Sub(monomeLeft, monomeRight);
        Expression denominator = new Mul(right, right);

        return new Div(numerator, denominator);
    }
}
