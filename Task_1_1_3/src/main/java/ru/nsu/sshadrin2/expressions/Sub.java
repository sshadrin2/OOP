package ru.nsu.sshadrin2.expressions;

/**
 * Substraction operation class.
 */
public class Sub extends Operation {

    /**
     * Creates substraction of two expressions.
     *
     * @param left left expression
     * @param right right expression
     */
    public Sub(Expression left, Expression right) {
        super(left, right, '-');
    }

    @Override
    public Expression derivative(String var) {
        Expression deLeft = left.derivative(var);
        Expression deRight = right.derivative(var);
        return new Sub(deLeft, deRight);
    }
}
