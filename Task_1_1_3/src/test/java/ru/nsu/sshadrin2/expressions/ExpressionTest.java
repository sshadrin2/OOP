package ru.nsu.sshadrin2.expressions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ExpressionTest {

    private static final float EPS = 1e-6f;

    // Atomic
    @Nested
    class AtomicTests {
        @Test
        void numberEval() {
            Expression e = new Number(5);
            assertEquals(5.0f, e.eval(""), EPS);
        }

        @Test
        void numberDerivative() {
            Expression de = new Number(5).derivative("x");
            assertEquals(0.0f, de.eval(""), EPS);
        }

        @Test
        void variableEval() {
            Expression e = new Variable("x");
            assertEquals(10.0f, e.eval("x = 10; y = 13"), EPS);
        }

        @Test
        void variableEvalMultiLetterName() {
            Expression e = new Variable("counter");
            assertEquals(7.0f, e.eval("counter = 7; x = 1"), EPS);
        }

        @Test
        void variableEvalUndefinedReturnsNaN() {
            Expression e = new Variable("z");
            assertTrue(Float.isNaN(e.eval("x = 10")));
        }

        @Test
        void variableDerivativeSame() {
            Expression de = new Variable("x").derivative("x");
            assertEquals(1.0f, de.eval("x = 10"), EPS);
        }

        @Test
        void variableDerivativeOther() {
            Expression de = new Variable("x").derivative("y");

            assertEquals(10.0f, de.eval("x = 10"), EPS);
        }
    }

    // Binary Opeartions
    @Nested
    class BinaryOperationsTests {
        @Test
        void addEval() {
            Expression e = new Add(new Number(3), new Number(4));
            assertEquals(7.0f, e.eval(""), EPS);
        }

        @Test
        void subEval() {
            Expression e = new Sub(new Number(10), new Number(3));
            assertEquals(7.0f, e.eval(""), EPS);
        }

        @Test
        void mulEval() {
            Expression e = new Mul(new Number(6), new Number(7));
            assertEquals(42.0f, e.eval(""), EPS);
        }

        @Test
        void divEval() {
            Expression e = new Div(new Number(20), new Number(4));
            assertEquals(5.0f, e.eval(""), EPS);
        }
    }

    // Derivatives
    @Nested
    class DerivativeTests {
        @Test
        void addDerivative() {
            Expression e = new Add(new Number(3), new Variable("x"));
            Expression de = e.derivative("x");
            assertEquals(1.0f, de.eval("x = 100"), EPS);
        }

        @Test
        void subDerivative() {
            Expression e = new Sub(new Variable("x"), new Number(5));
            Expression de = e.derivative("x");
            assertEquals(1.0f, de.eval("x = 42"), EPS);
        }

        @Test
        void mulByConstantDerivative() {
            // d/dx (3 * x) = 3
            Expression e = new Mul(new Number(3), new Variable("x"));
            Expression de = e.derivative("x");
            assertEquals(3.0f, de.eval("x = 100"), EPS);
        }

        @Test
        void mulDerivativeProductRule() {
            // d/dx (x * x) = 1*x + x*1 = 2x
            Expression e = new Mul(new Variable("x"), new Variable("x"));
            Expression de = e.derivative("x");
            assertEquals(10.0f, de.eval("x = 5"), EPS);
        }

        @Test
        void divDerivative() {
            // d/dx (x / 2) = (1*2 - x*0) / (2*2) = 2/4 = 0.5
            Expression e = new Div(new Variable("x"), new Number(2));
            Expression de = e.derivative("x");
            assertEquals(0.5f, de.eval("x = 100"), EPS);
        }

        @Test
        void derivativeDoesNotMutateOriginal() {
            Expression e = new Add(
                    new Number(3),
                    new Mul(new Number(2), new Variable("x"))
            );
            e.derivative("x");
            assertEquals(23.0f, e.eval("x = 10"), EPS);
        }
    }

    @Nested
    class ComplexOperationsTests {
        @Test
        void complexEval() {
            Expression e = new Add(
                    new Number(3),
                    new Mul(new Number(2), new Variable("x"))
            );
            assertEquals(23.0f, e.eval("x = 10; y = 13"), EPS);
        }

        @Test
        void complexDerivativeEval() {
            // d/dx (3 + (2*x)) = 2
            Expression e = new Add(
                    new Number(3),
                    new Mul(new Number(2), new Variable("x"))
            );
            Expression de = e.derivative("x");
            assertEquals(2.0f, de.eval("x = 10"), EPS);
        }
    }
}