package ru.nsu.sshadrin2.expressions;


/**
 * Addition operation class.
 */
public class Add extends Operation{

    /**
     * Creates addition of two expressions.
     *
     * @param left left expression
     * @param right right expression
     */
    public Add(Expression left, Expression right) {
        super(left, right, '+');
    }

    @Override
    public Expression derivative(String var) {
        Expression deLeft = left.derivative(var);
        Expression deRight = right.derivative(var);
        return new Add(deLeft, deRight);
    }
}
