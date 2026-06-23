package com.scbank.process.api.fw.base.integration.system.oltp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("OltpRequestHeaderBuilder Tests")
class OltpRequestHeaderBuilderTest {

    private OltpRequestHeaderBuilder builder;
    
    @Mock
	private ISessionContextManager sessionManager;

    @Mock
    private IntegrationTranNoGenerator mockTranNoGenerator;

    @Mock
    private OltpRequestOptions mockOptions;

    @Mock
    private OltpCommon mockOltpCommon;

    @BeforeEach
    void setUp() {
        builder = new OltpRequestHeaderBuilder(mockTranNoGenerator);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with IntegrationTranNoGenerator")
        void testConstructorWithGenerator() {
            OltpRequestHeaderBuilder headerBuilder = new OltpRequestHeaderBuilder(mockTranNoGenerator);
            assertNotNull(headerBuilder);
        }
    }

    @Nested
    @DisplayName("Build Header Tests")
    class BuildHeaderTests {

        @Test
        @DisplayName("Should build header with valid options")
        void testBuildHeaderWithValidOptions() {
            OltpCommon OltpCommon = new OltpCommon();
            OltpCommon.setBranchNo("001");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getProcessTp()).thenReturn("F");
            when(mockOptions.getVanTp()).thenReturn("15");
            when(mockOptions.getInOutCnlTp()).thenReturn("IO");
            when(mockOptions.getOltpCommon()).thenReturn(OltpCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN000001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertNotNull(header.getOltpCommon());
            }
        }

        @Test
        @DisplayName("Should build header with null OltpCommon in options")
        void testBuildHeaderWithNullOltpCommon() {
            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getProcessTp()).thenReturn("");
            when(mockOptions.getVanTp()).thenReturn("");
            when(mockOptions.getInOutCnlTp()).thenReturn("");
            when(mockOptions.getOltpCommon()).thenReturn(null);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN000001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertNotNull(header.getOltpCommon());
            }
        }

        @Test
        @DisplayName("Should set default branch number for DEV mode")
        void testDefaultBranchNoForDevMode() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN000001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertEquals("860", header.getOltpCommon().getBranchNo());
            }
        }

        @Test
        @DisplayName("Should set default branch number for PRD mode")
        void testDefaultBranchNoForPrdMode() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN000001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertEquals("019", header.getOltpCommon().getBranchNo());
            }
        }
    }

    @Nested
    @DisplayName("Transaction Number Generation Tests")
    class TransactionNumberTests {

        @Test
        @DisplayName("Should set transaction number from generator")
        void testTransactionNumberGeneration() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("001");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("GENERATED_TRN_ID");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertEquals("GENERATED_TRN_ID", header.getOltpCommon().getTrCd());
                verify(mockTranNoGenerator).generateId();
            }
        }
    }

    @Nested
    @DisplayName("Options Field Mapping Tests")
    class OptionsFieldMappingTests {

        @Test
        @DisplayName("Should map IMS transaction code")
        void testMapImsTranCd() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("001");

            when(mockOptions.getImsTranCd()).thenReturn("TESTTRAN");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertEquals("TESTTRAN", header.getOltpCommon().getImsTranCd());
            }
        }

        @Test
        @DisplayName("Should map service code")
        void testMapSvcCd() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("001");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("TST");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertEquals("TST", header.getOltpCommon().getSvcCd());
            }
        }

        @Test
        @DisplayName("Should set default job type when empty")
        void testDefaultJobType() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("001");
            hostCommon.setJobTp("");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertEquals("GP", header.getOltpCommon().getJobTp());
            }
        }
    }

    @Nested
    @DisplayName("Date and Time Tests")
    class DateTimeTests {

        @Test
        @DisplayName("Should set send date and time")
        void testSetSendDateTime() {
            OltpCommon hostCommon = new OltpCommon();
            hostCommon.setBranchNo("001");

            when(mockOptions.getImsTranCd()).thenReturn("TRAN0001");
            when(mockOptions.getInClassCd()).thenReturn("INCL");
            when(mockOptions.getSvcCd()).thenReturn("SVC");
            when(mockOptions.getOltpCommon()).thenReturn(hostCommon);
            when(mockTranNoGenerator.generateId()).thenReturn("TRN001");

            try (MockedStatic<RuntimeContext> mockedStatic = mockStatic(RuntimeContext.class)) {
                mockedStatic.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);

                Map<String, Object> defaultHeader = new HashMap<>();
                OltpReqHeader header = builder.build(defaultHeader, mockOptions);

                assertNotNull(header);
                assertNotNull(header.getOltpCommon().getSendDate());
                assertNotNull(header.getOltpCommon().getSendTime());
            }
        }
    }
}
