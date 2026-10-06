package ru.nsu.sshadrin2.expressions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParserTest {

    private static final float EPS = 1e-6f;

    // ---------- Простые атомы ----------

    @Test
    void parseNumber() {
        Expression e = Parser.parse("42");
        assertEquals(42.0f, e.eval(""), EPS);
    }

    @Test
    void parseVariable() {
        Expression e = Parser.parse("x");
        assertEquals(5.0f, e.eval("x = 5"), EPS);
    }

    @Test
    void parseMultiLetterVariable() {
        Expression e = Parser.parse("counter");
        assertEquals(7.0f, e.eval("counter = 7; x = 1"), EPS);
    }

    @Test
    void parseVariableWithUnderscoreAndDigits() {
        Expression e = Parser.parse("my_var2");
        assertEquals(3.0f, e.eval("my_var2 = 3"), EPS);
    }

    // ---------- Операции в скобках ----------

    @Test
    void parseAdd() {
        Expression e = Parser.parse("(3+4)");
        assertEquals(7.0f, e.eval(""), EPS);
    }

    @Test
    void parseSub() {
        Expression e = Parser.parse("(10-3)");
        assertEquals(7.0f, e.eval(""), EPS);
    }

    @Test
    void parseMul() {
        Expression e = Parser.parse("(6*7)");
        assertEquals(42.0f, e.eval(""), EPS);
    }

    @Test
    void parseDiv() {
        Expression e = Parser.parse("(20/4)");
        assertEquals(5.0f, e.eval(""), EPS);
    }

    // ---------- Вложенные выражения ----------

    @Test
    void parseNestedFromPdf() {
        // (3+(2*x)) при x=10 → 23
        Expression e = Parser.parse("(3+(2*x))");
        assertEquals(23.0f, e.eval("x = 10; y = 13"), EPS);
    }

    @Test
    void parseDeeplyNested() {
        // ((1+2)*(3+(4*5))) = 3 * 23 = 69
        Expression e = Parser.parse("((1+2)*(3+(4*5)))");
        assertEquals(69.0f, e.eval(""), EPS);
    }

    @Test
    void parseNestedWithVariables() {
        // ((x+y)*(x-y)) при x=5, y=2 → 7*3 = 21
        Expression e = Parser.parse("((x+y)*(x-y))");
        assertEquals(21.0f, e.eval("x = 5; y = 2"), EPS);
    }

    @Test
    void parseDivisionProducesFloat() {
        Expression e = Parser.parse("(1/4)");
        assertEquals(0.25f, e.eval(""), EPS);
    }

    // ---------- Пробелы ----------

    @Test
    void parseWithSpaces() {
        Expression e = Parser.parse("( 3 + ( 2 * x ) )");
        assertEquals(23.0f, e.eval("x = 10"), EPS);
    }

    @Test
    void parseWithTabsAndNewlines() {
        Expression e = Parser.parse("(\n\t3\n+\t(2*x)\n)");
        assertEquals(23.0f, e.eval("x = 10"), EPS);
    }

    @Test
    void parseLeadingAndTrailingSpaces() {
        Expression e = Parser.parse("   (3+4)   ");
        assertEquals(7.0f, e.eval(""), EPS);
    }

    // ---------- Связка с derivative ----------

    @Test
    void parsedExpressionCanBeDifferentiated() {
        Expression e = Parser.parse("(3+(2*x))");
        Expression de = e.derivative("x");
        assertEquals(2.0f, de.eval("x = 10"), EPS);
    }

    @Test
    void parsedComplexDerivative() {
        // d/dx (x*x) = 1*x + x*1 = 2x, при x=5 → 10
        Expression e = Parser.parse("(x*x)");
        Expression de = e.derivative("x");
        assertEquals(10.0f, de.eval("x = 5"), EPS);
    }

}