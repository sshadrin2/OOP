package ru.nsu.sshadrin2.expressions;

public class Parser {
    private final String s;
    private int pos;

    public Parser(String s) {
        this.s = s;
        this.pos = 0;
    }

    public static Expression parse(String s) {
        Parser parser = new Parser(s);
        Expression expr = parser.parseExpression();
        parser.skipWhitespace();

        if (parser.pos != s.length()) {
            throw new ParseException("Unexpected token at position " + parser.pos);
        }

        return expr;
    }

    private Expression parseExpression() {
        skipWhitespace();

        if (pos >= s.length()) {
            throw new ParseException("Unexpected end of input");
        }

        if (s.charAt(pos) == '(') {
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

        if (pos >= s.length()) {
            throw new ParseException("Expected number or variable");
        }

        char c = s.charAt(pos);

        if (Character.isDigit(c)) {
            int start = pos;

            while (pos < s.length() && Character.isDigit(s.charAt(pos))) {
                pos++;
            }

            int value = Integer.parseInt(s.substring(start, pos));
            return new Number(value);
        }

        if (Character.isLetter(c)) {
            int start = pos;

            while (pos < s.length()
                    && (Character.isLetterOrDigit(s.charAt(pos)) || s.charAt(pos) == '_')) {
                pos++;
            }

            String name = s.substring(start, pos);
            return new Variable(name);
        }

        throw new ParseException("Expected number or variable at position " + pos);
    }

    private char readOperator() {
        skipWhitespace();

        if (pos >= s.length()) {
            throw new ParseException("Expected operator");
        }

        char c = s.charAt(pos);

        if (c == '+' || c == '-' || c == '*' || c == '/') {
            pos++;
            return c;
        }

        throw new ParseException("Expected operator at position " + pos);
    }

    private void expect(char expected) {
        skipWhitespace();

        if (pos >= s.length() || s.charAt(pos) != expected) {
            throw new ParseException("Expected '" + expected + "' at position " + pos);
        }

        pos++;
    }

    private void skipWhitespace() {
        while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
            pos++;
        }
    }

    public static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }
    }
}