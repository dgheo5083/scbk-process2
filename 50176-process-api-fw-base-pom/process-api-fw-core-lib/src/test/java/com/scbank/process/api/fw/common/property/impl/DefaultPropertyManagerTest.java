package com.scbank.process.api.fw.common.property.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.CommonProperties.PropertyConfig;

/**
 * {@link DefaultPropertyManager} 단위 테스트.
 */
class DefaultPropertyManagerTest {

    private DefaultPropertyManager managerWith(List<String> locations) {
        return new DefaultPropertyManager(new PropertyConfig(true, locations));
    }

    @Test
    @DisplayName("classpath properties 를 로드하여 문자열/정수/불리언 값을 조회한다")
    void loadsAndReadsValues() {
        DefaultPropertyManager manager = managerWith(List.of("classpath:common-test/test.properties"));
        manager.init();

        assertThat(manager.getString("str.key", "def")).isEqualTo("hello");
        assertThat(manager.getInt("int.key", 0)).isEqualTo(42);
        assertThat(manager.getBoolean("bool.key")).isTrue();
        assertThat(manager.getBoolean("bool.key", false)).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 키는 기본값을 반환한다")
    void returnsDefaultForMissingKeys() {
        DefaultPropertyManager manager = managerWith(List.of("classpath:common-test/test.properties"));
        manager.init();

        assertThat(manager.getString("absent", "fallback")).isEqualTo("fallback");
        assertThat(manager.getInt("absent", -1)).isEqualTo(-1);
        assertThat(manager.getBoolean("absent", false)).isFalse();
    }

    @Test
    @DisplayName("getProperty 는 존재 시 값을, 없으면 기본값을 반환한다")
    void getProperty() {
        DefaultPropertyManager manager = managerWith(List.of("classpath:common-test/test.properties"));
        manager.init();

        assertThat(manager.<String>getProperty("str.key", "def")).isEqualTo("hello");
        assertThat(manager.<String>getProperty("absent", "def")).isEqualTo("def");
    }

    @Test
    @DisplayName("설정 경로가 비어 있으면 초기화는 아무것도 로드하지 않는다")
    void emptyLocationsLoadsNothing() {
        DefaultPropertyManager manager = managerWith(List.of());
        manager.init();

        assertThat(manager.getString("str.key", "def")).isEqualTo("def");
    }

    @Test
    @DisplayName("reload 는 재초기화를 수행한다")
    void reloadReinitializes() {
        DefaultPropertyManager manager = managerWith(List.of("classpath:common-test/test.properties"));
        manager.init();
        manager.reload();

        assertThat(manager.getString("str.key", "def")).isEqualTo("hello");
    }
}
