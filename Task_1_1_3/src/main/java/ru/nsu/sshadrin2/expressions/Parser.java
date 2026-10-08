package ru.nsu.sshadrin2.expressions;


/**
 * Arithmetical expression parser.
 * Transforms input string into Expression object.
 * Each binary operation must be surrounded by brackets.
 * Each variable or constant must not be surrounded by brackets.
 */
public class Parser {
    private final String input;
    private int pos;

    private Parser(String input) {
        this.input = input;
        this.pos = 0;
    }

    /**
     * Parses an arithmetical expression.
     *
     * @param input string with arithmetical expression
     * @return Expression type object
     */
    public static Expression parse(String input) {
        Parser parser = new Parser(input);
        Expression expr = parser.parseExpression();
        parser.skipWhitespace();

        if (parser.pos != input.length()) {
            throw new ParseException("Unexpected token", parser.pos);
        }

        return expr;
    }

    private Expression parseExpression() {
        skipWhitespace();

        if (pos >= input.length()) {
            throw new ParseException("Unexpected end of input");
        }

        if (input.charAt(pos) == '(') {
            pos++;

            final Expression left = parseExpression();
            skipWhitespace();

            char op = readOperator();

            final Expression right = parseExpression();
            skipWhitespace();

            if (pos >= input.length() || input.charAt(pos) != ')') {
                throw new ParseException("Expected closing bracket",pos);
            }
            pos++;

            return switch (op) {
                case '+' -> new Add(left, right);
                case '-' -> new Sub(left, right);
                case '*' -> new Mul(left, right);
                case '/' -> new Div(left, right);
                default -> throw new ParseException("Unknown operator: " + op, pos);
            };
        }

        return parseAtom();
    }

    private Expression parseAtom() {

        if (pos >= input.length()) {
            throw new ParseException("Expected number or variable");
        }

        char c = input.charAt(pos);

        if (Character.isDigit(c)) {
            int start = pos;

            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                pos++;
            }

            int value = Integer.parseInt(input.substring(start, pos));
            return new Number(value);
        }

        if (Character.isLetter(c)) {
            int start = pos;

            while (pos < input.length()
                    && Character.isLetterOrDigit(input.charAt(pos))) {
                pos++;
            }

            String name = input.substring(start, pos);
            return new Variable(name);
        }

        throw new ParseException("Expected number or variable", pos);
    }

    private char readOperator() {
        skipWhitespace();

        if (pos >= input.length()) {
            throw new ParseException("Expected operator");
        }

        char c = input.charAt(pos);

        if (c == '+' || c == '-' || c == '*' || c == '/') {
            pos++;
            return c;
        }

        throw new ParseException("Expected operator",pos);
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }

    /**
     * Parsing exception class.
     */
    public static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, int pos) {
            super(message + " at position " + pos);
        }
    }
}