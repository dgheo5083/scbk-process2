package com.scbank.process.api.fw.integration.rebound;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.integration.context.IntegrationContext;

/**
 * AbstractIntegrationReboundStrategy Test Class
 */
@ExtendWith(MockitoExtension.class)
class AbstractIntegrationReboundStrategyTest {

    @Mock
    private IntegrationContext mockContext;

    /**
     * Concrete test implementation of AbstractIntegrationReboundStrategy
     */
    private static class TestReboundStrategy extends AbstractIntegrationReboundStrategy<String, String> {

        @Override
        public boolean isContinue(IntegrationContext context, String response) {
            return response != null && response.contains("continue");
        }

        @Override
        public String handleData(String request, String response) {
            return request + "_next";
        }

        @Override
        public int getMaxLoopCnt() {
            return 10;
        }

        @Override
        public String getListFieldName() {
            return "itemList";
        }
    }

    private TestReboundStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new TestReboundStrategy();
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement IntegrationReboundStrategy interface")
        void shouldImplementIntegrationReboundStrategyInterface() {
            assertTrue(strategy instanceof IntegrationReboundStrategy);
        }

        @Test
        @DisplayName("Should be instance of AbstractIntegrationReboundStrategy")
        void shouldBeInstanceOfAbstractIntegrationReboundStrategy() {
            assertTrue(strategy instanceof AbstractIntegrationReboundStrategy);
        }
    }

    @Nested
    @DisplayName("isContinue tests")
    class IsContinueTests {

        @Test
        @DisplayName("Should return true when response contains continue keyword")
        void shouldReturnTrueWhenResponseContainsContinueKeyword() {
            assertTrue(strategy.isContinue(mockContext, "need_continue_process"));
        }

        @Test
        @DisplayName("Should return false when response does not contain continue keyword")
        void shouldReturnFalseWhenResponseDoesNotContainContinueKeyword() {
            assertFalse(strategy.isContinue(mockContext, "normal_response"));
        }

        @Test
        @DisplayName("Should return false when response is null")
        void shouldReturnFalseWhenResponseIsNull() {
            assertFalse(strategy.isContinue(mockContext, null));
        }
    }

    @Nested
    @DisplayName("handleData tests")
    class HandleDataTests {

        @Test
        @DisplayName("Should process handle data request")
        void shouldProcessHandleDataRequest() {
            String result = strategy.handleData("request", "response");

            assertEquals("request_next", result);
        }

        @Test
        @DisplayName("Should handle null request in handleData")
        void shouldHandleNullRequestInHandleData() {
            String result = strategy.handleData(null, "response");

            assertEquals("null_next", result);
        }
    }

    @Nested
    @DisplayName("getMaxLoopCnt tests")
    class GetMaxLoopCntTests {

        @Test
        @DisplayName("Should return max loop count")
        void shouldReturnMaxLoopCount() {
            assertEquals(10, strategy.getMaxLoopCnt());
        }
    }

    @Nested
    @DisplayName("getListFieldName tests")
    class GetListFieldNameTests {

        @Test
        @DisplayName("Should return list field name")
        void shouldReturnListFieldName() {
            assertEquals("itemList", strategy.getListFieldName());
        }
    }

    @Nested
    @DisplayName("generic type tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should support different generic types")
        void shouldSupportDifferentGenericTypes() {
            AbstractIntegrationReboundStrategy<Integer, Double> numericStrategy =
                    new AbstractIntegrationReboundStrategy<>() {
                        @Override
                        public boolean isContinue(IntegrationContext context, Double response) {
                            return response != null && response > 0;
                        }

                        @Override
                        public Integer handleData(Integer request, Double response) {
                            return request + 1;
                        }

                        @Override
                        public int getMaxLoopCnt() {
                            return 5;
                        }

                        @Override
                        public String getListFieldName() {
                            return "numbers";
                        }
                    };

            assertTrue(numericStrategy.isContinue(mockContext, 5.0));
            assertFalse(numericStrategy.isContinue(mockContext, -1.0));
            assertEquals(2, numericStrategy.handleData(1, 10.0));
            assertEquals(5, numericStrategy.getMaxLoopCnt());
            assertEquals("numbers", numericStrategy.getListFieldName());
        }
    }

    @Nested
    @DisplayName("abstract class extensibility tests")
    class ExtensibilityTests {

        @Test
        @DisplayName("Should allow multiple implementations")
        void shouldAllowMultipleImplementations() {
            AbstractIntegrationReboundStrategy<String, String> fepStrategy =
                    new AbstractIntegrationReboundStrategy<>() {
                        @Override
                        public boolean isContinue(IntegrationContext context, String response) {
                            return response != null && response.startsWith("FEP_CONTINUE");
                        }

                        @Override
                        public String handleData(String request, String response) {
                            return "FEP:" + request;
                        }

                        @Override
                        public int getMaxLoopCnt() {
                            return 20;
                        }

                        @Override
                        public String getListFieldName() {
                            return "fepItems";
                        }
                    };

            AbstractIntegrationReboundStrategy<String, String> mciStrategy =
                    new AbstractIntegrationReboundStrategy<>() {
                        @Override
                        public boolean isContinue(IntegrationContext context, String response) {
                            return response != null && response.startsWith("MCI_CONTINUE");
                        }

                        @Override
                        public String handleData(String request, String response) {
                            return "MCI:" + request;
                        }

                        @Override
                        public int getMaxLoopCnt() {
                            return 15;
                        }

                        @Override
                        public String getListFieldName() {
                            return "mciItems";
                        }
                    };

            assertEquals("fepItems", fepStrategy.getListFieldName());
            assertEquals("mciItems", mciStrategy.getListFieldName());

            assertTrue(fepStrategy.isContinue(mockContext, "FEP_CONTINUE_request"));
            assertFalse(fepStrategy.isContinue(mockContext, "MCI_CONTINUE_request"));

            assertTrue(mciStrategy.isContinue(mockContext, "MCI_CONTINUE_request"));
            assertFalse(mciStrategy.isContinue(mockContext, "FEP_CONTINUE_request"));

            assertEquals(20, fepStrategy.getMaxLoopCnt());
            assertEquals(15, mciStrategy.getMaxLoopCnt());
        }
    }
}
