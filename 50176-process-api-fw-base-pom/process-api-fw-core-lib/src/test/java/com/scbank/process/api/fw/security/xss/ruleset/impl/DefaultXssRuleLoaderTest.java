package com.scbank.process.api.fw.security.xss.ruleset.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.security.xss.ruleset.XssRuleInfo;

/**
 * {@link DefaultXssRuleLoader} 단위 테스트
 */
@DisplayName("DefaultXssRuleLoader 테스트")
class DefaultXssRuleLoaderTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("location을 주입받아 생성할 수 있다")
        void createWithLocation() {
            // when
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader("classpath:config/xss/xss-ruleset.xml");

            // then
            assertNotNull(loader);
        }

        @Test
        @DisplayName("null location으로 생성할 수 있다")
        void createWithNullLocation() {
            // when
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader(null);

            // then
            assertNotNull(loader);
        }

        @Test
        @DisplayName("빈 문자열 location으로 생성할 수 있다")
        void createWithEmptyLocation() {
            // when
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader("");

            // then
            assertNotNull(loader);
        }
    }

    @Nested
    @DisplayName("load 메서드 테스트")
    class LoadMethodTests {

        @Test
        @DisplayName("존재하지 않는 파일 경로로 load하면 빈 리스트를 반환한다")
        void loadReturnsEmptyListForNonExistentFile() {
            // given
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader("classpath:non-existent-file.xml");

            // when
            List<XssRuleInfo> rules = loader.load();

            // then
            assertNotNull(rules);
            assertTrue(rules.isEmpty());
        }

        @Test
        @DisplayName("null location으로 load하면 기본 경로에서 로드를 시도한다")
        void loadWithNullLocationTriesDefaultPath() {
            // given
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader(null);

            // when
            List<XssRuleInfo> rules = loader.load();

            // then
            assertNotNull(rules);
            // 기본 경로 파일이 없으면 빈 리스트 반환
        }

        @Test
        @DisplayName("빈 문자열 location으로 load하면 기본 경로에서 로드를 시도한다")
        void loadWithEmptyLocationTriesDefaultPath() {
            // given
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader("");

            // when
            List<XssRuleInfo> rules = loader.load();

            // then
            assertNotNull(rules);
        }

        @Test
        @DisplayName("잘못된 형식의 XML 파일 경로로 load하면 빈 리스트를 반환한다")
        void loadReturnsEmptyListForInvalidXml() {
            // given
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader("classpath:invalid-format.txt");

            // when
            List<XssRuleInfo> rules = loader.load();

            // then
            assertNotNull(rules);
            assertTrue(rules.isEmpty());
        }
    }

    @Nested
    @DisplayName("IXssRuleLoader 인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("IXssRuleLoader 인터페이스를 구현한다")
        void implementsInterface() {
            // given
            DefaultXssRuleLoader loader = new DefaultXssRuleLoader("location");

            // then
            assertInstanceOf(com.scbank.process.api.fw.security.xss.ruleset.IXssRuleLoader.class, loader);
        }
    }
}
