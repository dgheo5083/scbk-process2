package com.scbank.process.api.fw.common.code.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.common.CommonProperties.Config;
import com.scbank.process.api.fw.common.code.ICodeInfo;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.common.code.impl.DefaultCodeManager.CodeCacheKey;
import com.scbank.process.api.fw.core.cache.ICacheKey;
import com.scbank.process.api.fw.core.cache.ICacheManager;

/**
 * {@link DefaultCodeManager} 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class DefaultCodeManagerTest {

    @Mock
    private ICacheManager cacheManager;

    @AfterEach
    void tearDown() {
        LocaleContextHolder.resetLocaleContext();
    }

    private DefaultCodeManager managerWith(String location) {
        return new DefaultCodeManager(cacheManager, new Config(true, location));
    }

    private DefaultCodeInfo genderCode() {
        ICodeItemInfo male = DefaultCodeItemInfo.builder().key("M").value("Male").order(1).build();
        ICodeItemInfo female = DefaultCodeItemInfo.builder().key("F").value("Female").order(2).build();
        return DefaultCodeInfo.builder().key("GENDER").locale("ko")
                .codeItemList(List.of(male, female)).build();
    }

    @Test
    @DisplayName("init 은 XML 을 파싱하여 CodeCacheKey 로 캐시에 적재한다")
    void initLoadsIntoCache() {
        DefaultCodeManager manager = managerWith("classpath:common-test/code.xml");

        manager.init();

        verify(cacheManager).put(eq("csl-code"), eq(new CodeCacheKey("GENDER", "ko")),
                org.mockito.ArgumentMatchers.any(DefaultCodeInfo.class));
    }

    @Test
    @DisplayName("getCodeItemList(key, locale) 는 캐시의 아이템 목록을 반환")
    void getCodeItemListWithLocale() {
        DefaultCodeManager manager = managerWith("classpath:common-test/code.xml");
        when(cacheManager.get("csl-code", new CodeCacheKey("GENDER", "ko"), DefaultCodeInfo.class))
                .thenReturn(genderCode());

        List<ICodeItemInfo> items = manager.getCodeItemList("GENDER", "ko");

        assertThat(items).hasSize(2);
    }

    @Test
    @DisplayName("getCodeItemList 는 캐시 미존재 시 빈 목록")
    void getCodeItemListMissing() {
        DefaultCodeManager manager = managerWith("classpath:common-test/code.xml");
        when(cacheManager.get("csl-code", new CodeCacheKey("GENDER", "ko"), DefaultCodeInfo.class))
                .thenReturn(null);

        assertThat(manager.getCodeItemList("GENDER", "ko")).isEmpty();
    }

    @Test
    @DisplayName("getCodeItem(key, itemKey, locale) 는 매칭되는 값을 반환, 없으면 null")
    void getCodeItemWithLocale() {
        DefaultCodeManager manager = managerWith("classpath:common-test/code.xml");
        when(cacheManager.get("csl-code", new CodeCacheKey("GENDER", "ko"), DefaultCodeInfo.class))
                .thenReturn(genderCode());

        assertThat(manager.getCodeItem("GENDER", "M", "ko")).isEqualTo("Male");
        assertThat(manager.getCodeItem("GENDER", "X", "ko")).isNull();
    }

    @Test
    @DisplayName("getCodeItem(key, itemKey) 기본 메서드는 LocaleContextHolder 로케일을 사용")
    void getCodeItemDefaultLocale() {
        LocaleContextHolder.setLocale(Locale.KOREAN);
        DefaultCodeManager manager = managerWith("classpath:common-test/code.xml");
        when(cacheManager.get("csl-code", new CodeCacheKey("GENDER", "ko"), DefaultCodeInfo.class))
                .thenReturn(genderCode());

        assertThat(manager.getCodeItem("GENDER", "F")).isEqualTo("Female");
        assertThat(manager.getCodeItemList("GENDER")).hasSize(2);
    }

    @Test
    @DisplayName("getAllCodes 는 'code_lang' 키로 아이템 맵 목록을 구성한다")
    void getAllCodes() {
        DefaultCodeManager manager = managerWith("classpath:common-test/code.xml");
        Map<ICacheKey, ICodeInfo> all = Map.of(new CodeCacheKey("GENDER", "ko"), genderCode());
        when(cacheManager.getAll("csl-code", ICodeInfo.class)).thenReturn(all);

        Map<String, Object> result = manager.getAllCodes();

        assertThat(result).containsKey("GENDER_ko");
    }

    @Test
    @DisplayName("CodeCacheKey 레코드 접근자/equals")
    void codeCacheKeyRecord() {
        CodeCacheKey key = new CodeCacheKey("GENDER", "ko");

        assertThat(key.code()).isEqualTo("GENDER");
        assertThat(key.lang()).isEqualTo("ko");
        assertThat(key).isEqualTo(new CodeCacheKey("GENDER", "ko"));
    }
}
