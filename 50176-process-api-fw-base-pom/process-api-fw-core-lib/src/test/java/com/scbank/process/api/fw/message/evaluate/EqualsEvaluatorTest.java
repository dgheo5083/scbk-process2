package com.scbank.process.api.fw.message.evaluate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * {@link EqualsEvaluator} 단위 테스트
 */
@DisplayName("EqualsEvaluator 테스트")
class EqualsEvaluatorTest {

    private EqualsEvaluator evaluator;
    private MessageContext context;

    @BeforeEach
    void setUp() {
        evaluator = new EqualsEvaluator();
        context = new MessageContext();
    }

    @Nested
    @DisplayName("supports 테스트")
    class SupportsTests {

        @Test
        @DisplayName("== 연산자가 포함된 표현식을 지원한다")
        void supportsEqualsExpression() {
            // when
            boolean result = evaluator.supports("field == 'value'");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("== 연산자가 없는 표현식은 지원하지 않는다")
        void doesNotSupportExpressionWithoutEquals() {
            // when
            boolean result = evaluator.supports("field > 10");

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
        @DisplayName("값이 일치하면 true를 반환한다")
        void returnsTrueWhenValuesMatch() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "status == 'ACTIVE'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("값이 일치하지 않으면 false를 반환한다")
        void returnsFalseWhenValuesDoNotMatch() {
            // given
            context.addPathValue("status", "INACTIVE");
            String expression = "status == 'ACTIVE'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("숫자 값 비교가 가능하다")
        void canCompareNumbers() {
            // given
            context.addPathValue("count", 10);
            String expression = "count == '10'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("쌍따옴표로 감싼 값도 비교할 수 있다")
        void canCompareWithDoubleQuotes() {
            // given
            context.addPathValue("name", "test");
            String expression = "name == \"test\"";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("null 값도 문자열로 변환하여 비교한다")
        void canCompareNullValues() {
            // given
            context.addPathValue("field", null);
            String expression = "field == 'null'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("공백이 있는 표현식도 처리할 수 있다")
        void canHandleExpressionWithSpaces() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "  status  ==  'ACTIVE'  ";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("빈 문자열 비교가 가능하다")
        void canCompareEmptyString() {
            // given
            context.addPathValue("field", "");
            String expression = "field == ''";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }
    }
}
