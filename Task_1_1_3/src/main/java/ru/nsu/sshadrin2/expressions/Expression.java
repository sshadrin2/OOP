package ru.nsu.sshadrin2.expressions;


/**
 * Public class for mathematical expressions.
 * Supports addition, substraction, multiplication and division
 * with integer constants and variables.
 */
public abstract class Expression {

    /**
     * Evaluates an expression considering given variables interpretation.
     *
     * @param interpretation an interpretation of all variables in an expression
     *                       in form of "x = 10, y = 20 etc."
     *
     * @return the result of evaluation
     */
    public abstract float eval(String interpretation);

    /**
     * Prints an expression in the terminal.
     */
    public abstract void print();

    /**
     * Calculates a derivative with respect to a single variable.
     *
     * @param var a differentiable variable
     *
     * @return a new expression which is derivative of a given expression
     */
    public abstract Expression derivative(String var);

}
