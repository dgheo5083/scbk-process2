package com.scbank.process.api.fw.base.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

/**
 * Generated unit test for {@link CodeUtils}.
 */
class CodeUtilsTest {

    private ICodeManager codeManager;

    @BeforeEach
    void setUp() {
        codeManager = mock(ICodeManager.class);
        ReflectionTestUtils.setField(CodeUtils.class, "codeManager", codeManager);
        lenient().when(codeManager.getCodeItem(any(), any(), any())).thenReturn("VALUE");
        lenient().when(codeManager.getCodeItemList(any(), any())).thenReturn(List.of(mock(ICodeItemInfo.class)));
    }

    @Test
    void getCodeValueUsesDefaultLanguageWhenNoContext() {
        try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
                MockedStatic<ServiceContextHolder> sch = mockStatic(ServiceContextHolder.class)) {
            rc.when(RuntimeContext::getDefaultLocale).thenReturn("ko");
            sch.when(ServiceContextHolder::getContext).thenReturn(null);

            assertThat(CodeUtils.getCodeValue("CAT", "C")).isEqualTo("VALUE");
        }
    }

    @Test
    void getCodeValueUsesContextLocale() {
        IServiceContext context = mock(IServiceContext.class);
        when(context.locale()).thenReturn(Locale.ENGLISH);

        try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
                MockedStatic<ServiceContextHolder> sch = mockStatic(ServiceContextHolder.class)) {
            rc.when(RuntimeContext::getDefaultLocale).thenReturn("ko");
            sch.when(ServiceContextHolder::getContext).thenReturn(context);

            assertThat(CodeUtils.getCodeValue("CAT", "C", "en")).isEqualTo("VALUE");
        }
    }

    @Test
    void getCodeValueFallsBackWhenContextLocaleNull() {
        IServiceContext context = mock(IServiceContext.class);
        when(context.locale()).thenReturn(null);

        try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
                MockedStatic<ServiceContextHolder> sch = mockStatic(ServiceContextHolder.class)) {
            rc.when(RuntimeContext::getDefaultLocale).thenReturn("ko");
            sch.when(ServiceContextHolder::getContext).thenReturn(context);

            assertThat(CodeUtils.getCodeValue("CAT", "C")).isEqualTo("VALUE");
        }
    }

    @Test
    void getCodeValueReplacesMetaSequences() {
        when(codeManager.getCodeItem(any(), any(), any())).thenReturn("a&#10;b");

        try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
                MockedStatic<ServiceContextHolder> sch = mockStatic(ServiceContextHolder.class)) {
            rc.when(RuntimeContext::getDefaultLocale).thenReturn("ko");
            sch.when(ServiceContextHolder::getContext).thenReturn(null);

            assertThat(CodeUtils.getCodeValue("CAT", "C", true)).isEqualTo("a\nb");
            assertThat(CodeUtils.getCodeValue("CAT", "C", false)).isEqualTo("a&#10;b");
        }
    }

    @Test
    void getCodesReturnsList() {
        try (MockedStatic<RuntimeContext> rc = mockStatic(RuntimeContext.class);
                MockedStatic<ServiceContextHolder> sch = mockStatic(ServiceContextHolder.class)) {
            rc.when(RuntimeContext::getDefaultLocale).thenReturn("ko");
            sch.when(ServiceContextHolder::getContext).thenReturn(null);

            assertThat(CodeUtils.getCodes("CAT")).hasSize(1);
        }
    }
}
