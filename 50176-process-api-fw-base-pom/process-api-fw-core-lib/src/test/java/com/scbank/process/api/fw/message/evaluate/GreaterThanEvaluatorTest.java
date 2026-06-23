package com.scbank.process.api.fw.message.evaluate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * {@link GreaterThanEvaluator} 단위 테스트
 */
@DisplayName("GreaterThanEvaluator 테스트")
class GreaterThanEvaluatorTest {

    private GreaterThanEvaluator evaluator;
    private MessageContext context;

    @BeforeEach
    void setUp() {
        evaluator = new GreaterThanEvaluator();
        context = new MessageContext();
    }

    @Nested
    @DisplayName("supports 테스트")
    class SupportsTests {

        @Test
        @DisplayName("> 연산자가 포함된 표현식을 지원한다")
        void supportsGreaterThanExpression() {
            // when
            boolean result = evaluator.supports("count > 10");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("> 연산자가 없는 표현식은 지원하지 않는다")
        void doesNotSupportExpressionWithoutGreaterThan() {
            // when
            boolean result = evaluator.supports("count == 10");

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("contains 연산자 표현식은 지원하지 않는다")
        void doesNotSupportContainsExpression() {
            // when
            boolean result = evaluator.supports("field contains 'value'");

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("evaluate 테스트")
    class EvaluateTests {

        @Test
        @DisplayName("실제 값이 기대 값보다 크면 true를 반환한다")
        void returnsTrueWhenActualIsGreater() {
            // given
            context.addPathValue("count", 15);
            String expression = "count > 10";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("실제 값이 기대 값보다 작으면 false를 반환한다")
        void returnsFalseWhenActualIsLess() {
            // given
            context.addPathValue("count", 5);
            String expression = "count > 10";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("실제 값이 기대 값과 같으면 false를 반환한다")
        void returnsFalseWhenValuesAreEqual() {
            // given
            context.addPathValue("count", 10);
            String expression = "count > 10";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("소수점 값 비교가 가능하다")
        void canCompareDoubleValues() {
            // given
            context.addPathValue("price", 99.99);
            String expression = "price > 50.00";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("음수 값 비교가 가능하다")
        void canCompareNegativeValues() {
            // given
            context.addPathValue("temperature", -5);
            String expression = "temperature > -10";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("문자열 숫자 값도 비교할 수 있다")
        void canCompareStringNumbers() {
            // given
            context.addPathValue("count", "15");
            String expression = "count > 10";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("0과의 비교가 가능하다")
        void canCompareWithZero() {
            // given
            context.addPathValue("count", 1);
            String expression = "count > 0";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }
    }
}
