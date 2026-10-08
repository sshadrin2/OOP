package ru.nsu.sshadrin2.expressions;

import java.util.HashMap;

/**
 * Single variable expression.
 */
public class Variable extends Expression {

    private final String name;

    /**
     * Creates variable with given name.
     *
     * @param name name of a variable.
     */
    public Variable(String name) {
        this.name = name;
    }

    @Override
    public float eval(HashMap<String, Integer> interpretation) {
        if (interpretation.containsKey(name)) {
            return interpretation.get(name).floatValue();
        }
        return Float.NaN;
//        for (String pair : interpretation.split("\\s*;\\s*")) {
//            String[] kv = pair.split("\\s*=\\s*", 2);
//            if (kv[0].equals(name)) {
//                return Float.parseFloat(kv[1]);
//            }
//        }
//        return Float.NaN;
    }

    @Override
    public void print() {
        System.out.print(name);
    }

    @Override
    public Expression derivative(String var) {
        Expression de;
        if (name.equals(var)) {
            de = new Number(1);
        } else {
            de = new Variable(name);
        }

        return de;
    }
}
