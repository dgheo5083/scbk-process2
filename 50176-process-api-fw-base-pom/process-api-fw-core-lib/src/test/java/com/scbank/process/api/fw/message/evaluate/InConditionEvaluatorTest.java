package com.scbank.process.api.fw.message.evaluate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * {@link InConditionEvaluator} 단위 테스트
 */
@DisplayName("InConditionEvaluator 테스트")
class InConditionEvaluatorTest {

    private InConditionEvaluator evaluator;
    private MessageContext context;

    @BeforeEach
    void setUp() {
        evaluator = new InConditionEvaluator();
        context = new MessageContext();
    }

    @Nested
    @DisplayName("supports 테스트")
    class SupportsTests {

        @Test
        @DisplayName("in 연산자가 포함된 표현식을 지원한다")
        void supportsInExpression() {
            // when
            boolean result = evaluator.supports("status in ['A', 'B', 'C']");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("in이 없는 표현식은 지원하지 않는다")
        void doesNotSupportExpressionWithoutIn() {
            // when
            boolean result = evaluator.supports("status == 'A'");

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
        @DisplayName("값이 목록에 포함되면 true를 반환한다")
        void returnsTrueWhenValueIsInList() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "status in ['ACTIVE', 'PENDING', 'COMPLETED']";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("값이 목록에 포함되지 않으면 false를 반환한다")
        void returnsFalseWhenValueIsNotInList() {
            // given
            context.addPathValue("status", "DELETED");
            String expression = "status in ['ACTIVE', 'PENDING', 'COMPLETED']";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("쌍따옴표로 감싼 값도 처리할 수 있다")
        void canHandleDoubleQuotes() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "status in [\"ACTIVE\", \"PENDING\"]";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("숫자 값도 비교할 수 있다")
        void canCompareNumbers() {
            // given
            context.addPathValue("code", 1);
            String expression = "code in [1, 2, 3]";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("단일 값 목록도 처리할 수 있다")
        void canHandleSingleValueList() {
            // given
            context.addPathValue("status", "ONLY");
            String expression = "status in ['ONLY']";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("null 값도 문자열로 변환하여 비교한다")
        void canHandleNullValues() {
            // given
            context.addPathValue("field", null);
            String expression = "field in ['null', 'undefined']";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("공백이 있는 값도 비교할 수 있다")
        void canHandleValuesWithSpaces() {
            // given
            context.addPathValue("status", "A");
            String expression = "status in [ 'A' , 'B' , 'C' ]";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("빈 목록이면 항상 false를 반환한다")
        void returnsFalseForEmptyList() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "status in []";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }
    }
}
