package ru.nsu.sshadrin2.expressions;

public class Add extends Operation{

    public Add(Expression left, Expression right) {
        super(left, right, '+');
    }

    @Override
    public Expression derivative(String var) {
        Expression de_left = left.derivative(var);
        Expression de_right = right.derivative(var);
        return new Add(de_left, de_right);
    }
}
