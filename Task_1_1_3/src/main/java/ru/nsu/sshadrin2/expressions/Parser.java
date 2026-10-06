package ru.nsu.sshadrin2.expressions;


/**
 * Arithmetical expression parser.
 * Transforms input string into Expression object.
 * Each binary operation must be surrounded by brackets.
 * Each variable or constant must not be surrounded by brackets.
 */
public class Parser {
    private final String Input;
    private int pos;

    public Parser(String Input) {
        this.Input = Input;
        this.pos = 0;
    }

    /**
     * Parses an arithmetical expression.
     *
     * @param
     * @return
     */
    public static Expression parse(String Input) {
        Parser parser = new Parser(Input);
        Expression expr = parser.parseExpression();
        parser.skipWhitespace();

        if (parser.pos != Input.length()) {
            throw new ParseException("Unexpected token at position " + parser.pos);
        }

        return expr;
    }

    private Expression parseExpression() {
        skipWhitespace();

        if (pos >= Input.length()) {
            throw new ParseException("Unexpected end of input");
        }

        if (Input.charAt(pos) == '(') {
            pos++; // '('

            Expression left = parseExpression();
            skipWhitespace();

            char op = readOperator();

            Expression right = parseExpression();
            skipWhitespace();

            expect(')');

            return switch (op) {
                case '+' -> new Add(left, right);
                case '-' -> new Sub(left, right);
                case '*' -> new Mul(left, right);
                case '/' -> new Div(left, right);
                default -> throw new ParseException("Unknown operator: " + op);
            };
        }

        return parseAtom();
    }

    private Expression parseAtom() {
        skipWhitespace();

        if (pos >= Input.length()) {
            throw new ParseException("Expected number or variable");
        }

        char c = Input.charAt(pos);

        if (Character.isDigit(c)) {
            int start = pos;

            while (pos < Input.length() && Character.isDigit(Input.charAt(pos))) {
                pos++;
            }

            int value = Integer.parseInt(Input.substring(start, pos));
            return new Number(value);
        }

        if (Character.isLetter(c)) {
            int start = pos;

            while (pos < Input.length()
                    && (Character.isLetterOrDigit(Input.charAt(pos)) || Input.charAt(pos) == '_')) {
                pos++;
            }

            String name = Input.substring(start, pos);
            return new Variable(name);
        }

        throw new ParseException("Expected number or variable at position " + pos);
    }

    private char readOperator() {
        skipWhitespace();

        if (pos >= Input.length()) {
            throw new ParseException("Expected operator");
        }

        char c = Input.charAt(pos);

        if (c == '+' || c == '-' || c == '*' || c == '/') {
            pos++;
            return c;
        }

        throw new ParseException("Expected operator at position " + pos);
    }

    private void expect(char expected) {
        skipWhitespace();

        if (pos >= Input.length() || Input.charAt(pos) != expected) {
            throw new ParseException("Expected '" + expected + "' at position " + pos);
        }

        pos++;
    }

    private void skipWhitespace() {
        while (pos < Input.length() && Character.isWhitespace(Input.charAt(pos))) {
            pos++;
        }
    }

    public static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }
    }
}