package ru.nsu.sshadrin2.expressions;


import java.util.HashMap;

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
    public float eval(String interpretation) {
        HashMap<String, Integer> parsedInterpretation = new HashMap<>();

        for (String pair : interpretation.split("\\s*;\\s*")) {
            String[] kv = pair.split("\\s*=\\s*", 2);
            if (kv.length == 2) {
                parsedInterpretation.put(kv[0], Integer.parseInt(kv[1]));
            }

        }
        return eval(parsedInterpretation);
    }

    protected abstract float eval(HashMap<String, Integer> parsedInterpretation);

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
