package com.scbank.process.api.fw.message.evaluate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * {@link ContainsEvaluator} 단위 테스트
 */
@DisplayName("ContainsEvaluator 테스트")
class ContainsEvaluatorTest {

    private ContainsEvaluator evaluator;
    private MessageContext context;

    @BeforeEach
    void setUp() {
        evaluator = new ContainsEvaluator();
        context = new MessageContext();
    }

    @Nested
    @DisplayName("supports 테스트")
    class SupportsTests {

        @Test
        @DisplayName("contains 연산자가 포함된 표현식을 지원한다")
        void supportsContainsExpression() {
            // when
            boolean result = evaluator.supports("field contains 'value'");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("contains가 없는 표현식은 지원하지 않는다")
        void doesNotSupportExpressionWithoutContains() {
            // when
            boolean result = evaluator.supports("field == 'value'");

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("> 연산자 표현식은 지원하지 않는다")
        void doesNotSupportGreaterThanExpression() {
            // when
            boolean result = evaluator.supports("field > 10");

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("evaluate 테스트")
    class EvaluateTests {

        @Test
        @DisplayName("값이 키워드를 포함하면 true를 반환한다")
        void returnsTrueWhenValueContainsKeyword() {
            // given
            context.addPathValue("message", "Hello World");
            String expression = "message contains 'World'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("값이 키워드를 포함하지 않으면 false를 반환한다")
        void returnsFalseWhenValueDoesNotContainKeyword() {
            // given
            context.addPathValue("message", "Hello World");
            String expression = "message contains 'Java'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("대소문자를 구분하여 비교한다")
        void isCaseSensitive() {
            // given
            context.addPathValue("message", "Hello World");
            String expression = "message contains 'world'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("쌍따옴표로 감싼 키워드도 처리할 수 있다")
        void canHandleDoubleQuotes() {
            // given
            context.addPathValue("message", "Hello World");
            String expression = "message contains \"World\"";

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
            String expression = "field contains 'null'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("빈 문자열 키워드는 항상 포함된다")
        void emptyKeywordAlwaysContained() {
            // given
            context.addPathValue("message", "Hello");
            String expression = "message contains ''";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("숫자 값도 문자열로 변환하여 비교한다")
        void canHandleNumericValues() {
            // given
            context.addPathValue("code", 12345);
            String expression = "code contains '234'";

            // when
            boolean result = evaluator.evaluate(expression, context);

            // then
            assertTrue(result);
        }
    }
}
