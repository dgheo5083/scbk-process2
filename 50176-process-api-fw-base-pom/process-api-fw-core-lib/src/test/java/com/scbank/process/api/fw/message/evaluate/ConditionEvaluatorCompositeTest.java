package com.scbank.process.api.fw.message.evaluate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.context.MessageContext;

/**
 * {@link ConditionEvaluatorComposite} 단위 테스트
 */
@DisplayName("ConditionEvaluatorComposite 테스트")
class ConditionEvaluatorCompositeTest {

    private ConditionEvaluatorComposite composite;
    private MessageContext context;

    @BeforeEach
    void setUp() {
        composite = new ConditionEvaluatorComposite();
        context = new MessageContext();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("기본 생성자로 인스턴스를 생성할 수 있다")
        void canCreateInstance() {
            // when & then
            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("equals 연산자 테스트")
    class EqualsOperatorTests {

        @Test
        @DisplayName("== 연산자를 처리할 수 있다")
        void canEvaluateEqualsExpression() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "status == 'ACTIVE'";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("== 연산자로 일치하지 않는 경우 false를 반환한다")
        void returnsFalseForNonMatchingEquals() {
            // given
            context.addPathValue("status", "INACTIVE");
            String expression = "status == 'ACTIVE'";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("greater than 연산자 테스트")
    class GreaterThanOperatorTests {

        @Test
        @DisplayName("> 연산자를 처리할 수 있다")
        void canEvaluateGreaterThanExpression() {
            // given
            context.addPathValue("count", 15);
            String expression = "count > 10";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("> 연산자로 작거나 같은 경우 false를 반환한다")
        void returnsFalseWhenNotGreater() {
            // given
            context.addPathValue("count", 5);
            String expression = "count > 10";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("contains 연산자 테스트")
    class ContainsOperatorTests {

        @Test
        @DisplayName("contains 연산자를 처리할 수 있다")
        void canEvaluateContainsExpression() {
            // given
            context.addPathValue("message", "Hello World");
            String expression = "message contains 'World'";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("contains 연산자로 포함하지 않는 경우 false를 반환한다")
        void returnsFalseWhenNotContains() {
            // given
            context.addPathValue("message", "Hello World");
            String expression = "message contains 'Java'";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("in 연산자 테스트")
    class InOperatorTests {

        @Test
        @DisplayName("in 연산자를 처리할 수 있다")
        void canEvaluateInExpression() {
            // given
            context.addPathValue("status", "ACTIVE");
            String expression = "status in ['ACTIVE', 'PENDING']";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("in 연산자로 목록에 없는 경우 false를 반환한다")
        void returnsFalseWhenNotInList() {
            // given
            context.addPathValue("status", "DELETED");
            String expression = "status in ['ACTIVE', 'PENDING']";

            // when
            boolean result = composite.evaluate(expression, context);

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("지원하지 않는 연산자 테스트")
    class UnsupportedOperatorTests {

        @Test
        @DisplayName("지원하지 않는 연산자는 예외를 발생시킨다")
        void throwsExceptionForUnsupportedOperator() {
            // given
            context.addPathValue("count", 10);
            String expression = "count != 5";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> composite.evaluate(expression, context));

            assertTrue(exception.getMessage().contains("지원하지 않는 조건식"));
        }

        @Test
        @DisplayName("잘못된 형식의 표현식은 예외를 발생시킨다")
        void throwsExceptionForInvalidExpression() {
            // given
            String expression = "invalid expression without operator";

            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> composite.evaluate(expression, context));
        }
    }

    @Nested
    @DisplayName("복합 시나리오 테스트")
    class ComplexScenarioTests {

        @Test
        @DisplayName("여러 타입의 값을 평가할 수 있다")
        void canEvaluateVariousTypes() {
            // given - String
            context.addPathValue("name", "test");
            assertTrue(composite.evaluate("name == 'test'", context));

            // given - Integer
            context.addPathValue("age", 25);
            assertTrue(composite.evaluate("age > 20", context));

            // given - Double
            context.addPathValue("price", 99.99);
            assertTrue(composite.evaluate("price > 50", context));
        }

        @Test
        @DisplayName("같은 필드에 대해 여러 조건을 순차적으로 평가할 수 있다")
        void canEvaluateMultipleConditionsSequentially() {
            // given
            context.addPathValue("count", 15);

            // when & then
            assertTrue(composite.evaluate("count > 10", context));
            assertFalse(composite.evaluate("count > 20", context));
        }
    }
}
