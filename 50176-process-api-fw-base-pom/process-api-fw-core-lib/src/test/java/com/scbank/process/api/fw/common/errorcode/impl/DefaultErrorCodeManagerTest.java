package com.scbank.process.api.fw.common.errorcode.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.errorcode.IErrorCodeInfo;
import com.scbank.process.api.fw.core.cache.ICacheManager;

/**
 * {@link DefaultErrorCodeManager} 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class DefaultErrorCodeManagerTest {

    @Mock
    private ICacheManager cacheManager;

    @AfterEach
    void tearDown() {
        LocaleContextHolder.resetLocaleContext();
    }

    private DefaultErrorCodeManager managerWith(String location) {
        return new DefaultErrorCodeManager(cacheManager, new Config(true, location));
    }

    @Test
    @DisplayName("init 은 XML 을 파싱하여 에러코드 정보를 캐시에 적재한다(가이드 메시지는 order 순 정렬)")
    void initLoadsIntoCache() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");

        manager.init();

        ArgumentCaptor<IErrorCodeInfo> captor = ArgumentCaptor.forClass(IErrorCodeInfo.class);
        verify(cacheManager).put(eq("csl-errorcode"), any(), captor.capture());
        DefaultErrorCodeInfo info = (DefaultErrorCodeInfo) captor.getValue();
        assertThat(info.getCode()).isEqualTo("E001");
        assertThat(info.getLangCode()).isEqualTo("ko");
        assertThat(info.getMessage()).isEqualTo("error message {0}");
        assertThat(info.getErrorGuideMessageInfos()).hasSize(2);
        // order 순 정렬: guide1(order=1) 먼저
        assertThat(info.getErrorGuideMessageInfos().get(0).getMessage()).isEqualTo("guide1");
        assertThat(info.getErrorGuideMessageInfos().get(1).getMessage()).isEqualTo("guide2");
    }

    @Test
    @DisplayName("getErrorCodeInfo(code, lang) 는 캐시 값을 반환")
    void getErrorCodeInfo() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");
        IErrorCodeInfo info = mock(IErrorCodeInfo.class);
        when(cacheManager.get(eq("csl-errorcode"), any(), eq(IErrorCodeInfo.class))).thenReturn(info);

        assertThat(manager.getErrorCodeInfo("E001", "ko")).isSameAs(info);
    }

    @Test
    @DisplayName("getErrorMessage(code, lang): 정보가 있으면 메시지, 없으면 빈 문자열")
    void getErrorMessage() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");
        IErrorCodeInfo info = mock(IErrorCodeInfo.class);
        when(info.getMessage()).thenReturn("오류");
        when(cacheManager.get(eq("csl-errorcode"), any(), eq(IErrorCodeInfo.class)))
                .thenReturn(info, (IErrorCodeInfo) null);

        assertThat(manager.getErrorMessage("E001", "ko")).isEqualTo("오류");
        assertThat(manager.getErrorMessage("E404", "ko")).isEmpty();
    }

    @Test
    @DisplayName("getErrorMessage(code, lang, args): MessageFormat 으로 인자를 치환")
    void getErrorMessageWithArgs() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");
        IErrorCodeInfo info = mock(IErrorCodeInfo.class);
        when(info.getMessage()).thenReturn("error message {0}");
        when(cacheManager.get(eq("csl-errorcode"), any(), eq(IErrorCodeInfo.class))).thenReturn(info);

        assertThat(manager.getErrorMessage("E001", "ko", "X")).isEqualTo("error message X");
    }

    @Test
    @DisplayName("getErrorMessage(code, lang, args): 인자가 없으면 빈 문자열")
    void getErrorMessageWithoutArgs() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");
        IErrorCodeInfo info = mock(IErrorCodeInfo.class);
        when(info.getMessage()).thenReturn("error message {0}");
        when(cacheManager.get(eq("csl-errorcode"), any(), eq(IErrorCodeInfo.class))).thenReturn(info);

        assertThat(manager.getErrorMessage("E001", "ko", new Object[0])).isEmpty();
    }

    @Test
    @DisplayName("getErrorGuideMessages(code, lang): 정보가 없으면 빈 목록")
    void getErrorGuideMessagesMissing() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");
        when(cacheManager.get(eq("csl-errorcode"), any(), eq(IErrorCodeInfo.class))).thenReturn(null);

        assertThat(manager.getErrorGuideMessages("E404", "ko")).isEmpty();
    }

    @Test
    @DisplayName("getErrorCodeInfo(code) 기본 메서드는 LocaleContextHolder 로케일을 사용")
    void getErrorCodeInfoDefaultLocale() {
        LocaleContextHolder.setLocale(Locale.KOREAN);
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");
        IErrorCodeInfo info = mock(IErrorCodeInfo.class);
        lenient().when(info.getMessage()).thenReturn("오류");
        when(cacheManager.get(eq("csl-errorcode"), any(), eq(IErrorCodeInfo.class))).thenReturn(info);

        assertThat(manager.getErrorCodeInfo("E001")).isSameAs(info);
        assertThat(manager.getErrorMessage("E001")).isEqualTo("오류");
    }

    @Test
    @DisplayName("reload 는 캐시를 비우고 재적재한다")
    void reloadClearsAndReloads() {
        DefaultErrorCodeManager manager = managerWith("classpath:common-test/errorcode.xml");

        manager.reload();

        verify(cacheManager).clear("csl-errorcode");
        verify(cacheManager).put(eq("csl-errorcode"), any(), any());
    }
}
