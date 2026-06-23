package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link SessionMetadataInfo} 단위 테스트
 */
@DisplayName("SessionMetadataInfo 테스트")
class SessionMetadataInfoTest {

    private SessionMetadataInfo metadataInfo;

    @BeforeEach
    void setUp() {
        metadataInfo = new SessionMetadataInfo();
    }

    @Nested
    @DisplayName("v3 속성 테스트")
    class V3PropertyTests {

        @Test
        @DisplayName("v3 값을 설정하고 조회할 수 있다")
        void canSetAndGetV3() {
            // when
            metadataInfo.setV3("co.kr.ma30.session.UserSession");

            // then
            assertEquals("co.kr.ma30.session.UserSession", metadataInfo.getV3());
        }
    }

    @Nested
    @DisplayName("v4 속성 테스트")
    class V4PropertyTests {

        @Test
        @DisplayName("v4 값을 설정하고 조회할 수 있다")
        void canSetAndGetV4() {
            // when
            metadataInfo.setV4("com.scbank.process.api.fw.session.impl.DefaultSessionContext");

            // then
            assertEquals("com.scbank.process.api.fw.session.impl.DefaultSessionContext", metadataInfo.getV4());
        }
    }

    @Nested
    @DisplayName("element 속성 테스트")
    class ElementPropertyTests {

        @Test
        @DisplayName("element 값을 설정하고 조회할 수 있다")
        void canSetAndGetElement() {
            // given
            SessionMetadataInfo.ElementTypeMapping element = new SessionMetadataInfo.ElementTypeMapping();
            element.setV3("co.kr.ma30.session.Record");
            element.setV4("java.util.Map");

            // when
            metadataInfo.setElement(element);

            // then
            assertNotNull(metadataInfo.getElement());
            assertEquals("co.kr.ma30.session.Record", metadataInfo.getElement().getV3());
            assertEquals("java.util.Map", metadataInfo.getElement().getV4());
        }
    }

    @Nested
    @DisplayName("children 속성 테스트")
    class ChildrenPropertyTests {

        @Test
        @DisplayName("children을 설정하고 조회할 수 있다")
        void canSetAndGetChildren() {
            // given
            List<Map<String, SessionMetadataInfo>> children = new ArrayList<>();
            Map<String, SessionMetadataInfo> childMap = new HashMap<>();
            SessionMetadataInfo childInfo = new SessionMetadataInfo();
            childInfo.setV4("java.lang.String");
            childMap.put("childKey", childInfo);
            children.add(childMap);

            // when
            metadataInfo.setChildren(children);

            // then
            assertNotNull(metadataInfo.getChildren());
            assertEquals(1, metadataInfo.getChildren().size());
        }
    }

    @Nested
    @DisplayName("getChild 메서드 테스트")
    class GetChildMethodTests {

        @Test
        @DisplayName("존재하는 자식 키로 조회할 수 있다")
        void canGetExistingChild() {
            // given
            List<Map<String, SessionMetadataInfo>> children = new ArrayList<>();
            Map<String, SessionMetadataInfo> childMap = new HashMap<>();
            SessionMetadataInfo childInfo = new SessionMetadataInfo();
            childInfo.setV4("java.lang.String");
            childMap.put("loginSession", childInfo);
            children.add(childMap);
            metadataInfo.setChildren(children);

            // when
            SessionMetadataInfo result = metadataInfo.getChild("loginSession");

            // then
            assertNotNull(result);
            assertEquals("java.lang.String", result.getV4());
        }

        @Test
        @DisplayName("존재하지 않는 자식 키는 null을 반환한다")
        void returnsNullForNonExistentChild() {
            // given
            List<Map<String, SessionMetadataInfo>> children = new ArrayList<>();
            Map<String, SessionMetadataInfo> childMap = new HashMap<>();
            childMap.put("existingKey", new SessionMetadataInfo());
            children.add(childMap);
            metadataInfo.setChildren(children);

            // when
            SessionMetadataInfo result = metadataInfo.getChild("nonExistentKey");

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("children가 null이면 null을 반환한다")
        void returnsNullWhenChildrenIsNull() {
            // given
            metadataInfo.setChildren(null);

            // when
            SessionMetadataInfo result = metadataInfo.getChild("anyKey");

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("여러 맵에서 자식을 찾을 수 있다")
        void canFindChildAcrossMultipleMaps() {
            // given
            List<Map<String, SessionMetadataInfo>> children = new ArrayList<>();

            Map<String, SessionMetadataInfo> childMap1 = new HashMap<>();
            childMap1.put("key1", new SessionMetadataInfo());
            children.add(childMap1);

            Map<String, SessionMetadataInfo> childMap2 = new HashMap<>();
            SessionMetadataInfo targetInfo = new SessionMetadataInfo();
            targetInfo.setV4("target");
            childMap2.put("key2", targetInfo);
            children.add(childMap2);

            metadataInfo.setChildren(children);

            // when
            SessionMetadataInfo result = metadataInfo.getChild("key2");

            // then
            assertNotNull(result);
            assertEquals("target", result.getV4());
        }
    }

    @Nested
    @DisplayName("ElementTypeMapping 내부 클래스 테스트")
    class ElementTypeMappingTests {

        @Test
        @DisplayName("ElementTypeMapping 인스턴스를 생성할 수 있다")
        void canCreateInstance() {
            // when
            SessionMetadataInfo.ElementTypeMapping element = new SessionMetadataInfo.ElementTypeMapping();

            // then
            assertNotNull(element);
        }

        @Test
        @DisplayName("v3 값을 설정하고 조회할 수 있다")
        void canSetAndGetV3() {
            // given
            SessionMetadataInfo.ElementTypeMapping element = new SessionMetadataInfo.ElementTypeMapping();

            // when
            element.setV3("co.kr.ma30.session.Record");

            // then
            assertEquals("co.kr.ma30.session.Record", element.getV3());
        }

        @Test
        @DisplayName("v4 값을 설정하고 조회할 수 있다")
        void canSetAndGetV4() {
            // given
            SessionMetadataInfo.ElementTypeMapping element = new SessionMetadataInfo.ElementTypeMapping();

            // when
            element.setV4("java.util.Map");

            // then
            assertEquals("java.util.Map", element.getV4());
        }
    }

    @Nested
    @DisplayName("Lombok 생성 메서드 테스트")
    class LombokGeneratedMethodTests {

        @Test
        @DisplayName("toString 메서드가 정상 동작한다")
        void toStringWorks() {
            // when
            String result = metadataInfo.toString();

            // then
            assertNotNull(result);
        }

        @Test
        @DisplayName("equals 메서드가 정상 동작한다")
        void equalsWorks() {
            // given
            SessionMetadataInfo other = new SessionMetadataInfo();

            // then
            assertEquals(metadataInfo, other);
        }

        @Test
        @DisplayName("hashCode 메서드가 정상 동작한다")
        void hashCodeWorks() {
            // given
            SessionMetadataInfo other = new SessionMetadataInfo();

            // then
            assertEquals(metadataInfo.hashCode(), other.hashCode());
        }
    }
}
