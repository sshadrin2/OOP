package ru.nsu.sshadrin2.expressions;

/**
 * Multiplication operation class.
 */
public class Mul extends Operation {

    /**
     * Creates multiplication of two expressions.
     *
     * @param left left expression
     * @param right right expression
     */
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
