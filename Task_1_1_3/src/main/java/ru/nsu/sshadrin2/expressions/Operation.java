package ru.nsu.sshadrin2.expressions;


abstract class Operation extends Expression {
    protected final Expression left;
    protected final Expression right;
    protected final char op;

    Operation(Expression left, Expression right, char op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public float eval(String interpretation) {
        return switch (op) {
            case '+' -> left.eval(interpretation) + right.eval(interpretation);
            case '-' -> left.eval(interpretation) - right.eval(interpretation);
            case '*' -> left.eval(interpretation) * right.eval(interpretation);
            case '/' -> left.eval(interpretation) / right.eval(interpretation);
            default -> Float.NaN;
        };
    }

    @Override
    public void print() {
        System.out.print('(');
        left.print();
        System.out.print(op);
        right.print();
        System.out.print(')');
    }
}
