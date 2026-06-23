package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.impl.DefaultSessionContext;
import com.scbank.process.api.fw.session.impl.GlobalSessionObject;
import com.scbank.process.api.fw.session.impl.LoginSessionObject;

/**
 * {@link SessionContextRevokeAdapter} 단위 테스트
 */
@DisplayName("SessionContextRevokeAdapter 테스트")
@ExtendWith(MockitoExtension.class)
class SessionContextRevokeAdapterTest {

    private SessionContextRevokeAdapter adapter;

    @Mock
    private ISessionMetadataRegistry metadataRegistry;

    @BeforeEach
    void setUp() {
        adapter = new SessionContextRevokeAdapter(metadataRegistry);
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("ISessionMetadataRegistry를 주입받아 생성할 수 있다")
        void createWithMetadataRegistry() {
            // when
            SessionContextRevokeAdapter newAdapter = new SessionContextRevokeAdapter(metadataRegistry);

            // then
            assertNotNull(newAdapter);
        }
    }

    @Nested
    @DisplayName("supports 메서드 테스트")
    class SupportsMethodTests {

        @Test
        @DisplayName("ISessionContext.SESSION_ATTR_KEY를 지원한다")
        void supportsSessionAttrKey() {
            // when
            boolean result = adapter.supports(ISessionContext.SESSION_ATTR_KEY, new DefaultSessionContext());

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("다른 키는 지원하지 않는다")
        void doesNotSupportOtherKeys() {
            // when
            boolean result = adapter.supports("otherKey", "value");

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("wrap 메서드 테스트")
    class WrapMethodTests {

        @Test
        @DisplayName("ISessionContext가 아닌 값은 그대로 반환한다")
        void returnsNonSessionContextAsIs() {
            // when
            Object result = adapter.wrap(ISessionContext.SESSION_ATTR_KEY, "stringValue");

            // then
            assertEquals("stringValue", result);
        }

        @Test
        @DisplayName("ISessionContext를 Map으로 변환한다")
        void wrapsSessionContextToMap() {
            // given
            DefaultSessionContext context = new DefaultSessionContext();
            context.setSessionId("session123");
            context.setClientIp("192.168.1.1");
            context.setLoginSession(new LoginSessionObject());
            context.setGlobalSession(new GlobalSessionObject());

            SessionMetadataInfo meta = createMetadataInfo();
            when(metadataRegistry.getMetadata(ISessionContext.SESSION_ATTR_KEY)).thenReturn(Optional.of(meta));

            // when
            Object result = adapter.wrap(ISessionContext.SESSION_ATTR_KEY, context);

            // then
            assertInstanceOf(Map.class, result);
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) result;
            assertEquals("session123", resultMap.get("sessionId"));
            assertEquals("192.168.1.1", resultMap.get("clientIp"));
        }

        @Test
        @DisplayName("메타데이터가 없으면 원본 값을 반환한다")
        void returnsOriginalWhenNoMetadata() {
            // given
            DefaultSessionContext context = new DefaultSessionContext();
            when(metadataRegistry.getMetadata(ISessionContext.SESSION_ATTR_KEY)).thenReturn(Optional.empty());

            // when
            Object result = adapter.wrap(ISessionContext.SESSION_ATTR_KEY, context);

            // then
            assertSame(context, result);
        }
    }

    @Nested
    @DisplayName("unwrap 메서드 테스트")
    class UnwrapMethodTests {

        @Test
        @DisplayName("Map이 아닌 값은 그대로 반환한다")
        void returnsNonMapAsIs() {
            // when
            Object result = adapter.unwrap(ISessionContext.SESSION_ATTR_KEY, "stringValue");

            // then
            assertEquals("stringValue", result);
        }

        @Test
        @DisplayName("메타데이터가 없으면 원본 값을 반환한다")
        void returnsOriginalWhenNoMetadata() {
            // given
            Map<String, Object> rawMap = new HashMap<>();
            when(metadataRegistry.getMetadata(ISessionContext.SESSION_ATTR_KEY)).thenReturn(Optional.empty());

            // when
            Object result = adapter.unwrap(ISessionContext.SESSION_ATTR_KEY, rawMap);

            // then
            assertEquals(rawMap, result);
        }

        @Test
        @DisplayName("Map을 DefaultSessionContext로 변환한다")
        void unwrapsMapToSessionContext() {
            // given
            Map<String, Object> rawMap = new HashMap<>();
            rawMap.put("sessionId", "session123");
            rawMap.put("clientIp", "192.168.1.1");
            rawMap.put("loginSession", new HashMap<>());
            rawMap.put("globalSession", new HashMap<>());

            SessionMetadataInfo meta = createMetadataInfo();
            when(metadataRegistry.getMetadata(ISessionContext.SESSION_ATTR_KEY)).thenReturn(Optional.of(meta));

            // when
            Object result = adapter.unwrap(ISessionContext.SESSION_ATTR_KEY, rawMap);

            // then
            assertInstanceOf(DefaultSessionContext.class, result);
            DefaultSessionContext context = (DefaultSessionContext) result;
            assertEquals("session123", context.getSessionId());
            assertEquals("192.168.1.1", context.getClientIp());
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("AbstractSessionAdapter를 상속한다")
        void extendsAbstractSessionAdapter() {
            // then
            assertInstanceOf(AbstractSessionAdapter.class, adapter);
        }

        @Test
        @DisplayName("ISessionAdapter 인터페이스를 구현한다")
        void implementsISessionAdapter() {
            // then
            assertInstanceOf(ISessionAdapter.class, adapter);
        }
    }

    private SessionMetadataInfo createMetadataInfo() {
        SessionMetadataInfo meta = new SessionMetadataInfo();
        meta.setV3("co.kr.ma30.session.SessionContext");
        meta.setV4("com.scbank.process.api.fw.session.impl.DefaultSessionContext");

        List<Map<String, SessionMetadataInfo>> children = new ArrayList<>();

        // loginSession child
        Map<String, SessionMetadataInfo> loginChild = new HashMap<>();
        SessionMetadataInfo loginMeta = new SessionMetadataInfo();
        loginMeta.setV3("co.kr.ma30.session.LoginSession");
        loginMeta.setV4("com.scbank.process.api.fw.session.impl.LoginSessionObject");
        loginMeta.setChildren(new ArrayList<>());
        loginChild.put("loginSession", loginMeta);
        children.add(loginChild);

        // globalSession child
        Map<String, SessionMetadataInfo> globalChild = new HashMap<>();
        SessionMetadataInfo globalMeta = new SessionMetadataInfo();
        globalMeta.setV3("co.kr.ma30.session.GlobalSession");
        globalMeta.setV4("com.scbank.process.api.fw.session.impl.GlobalSessionObject");
        globalMeta.setChildren(new ArrayList<>());
        globalChild.put("globalSession", globalMeta);
        children.add(globalChild);

        meta.setChildren(children);

        return meta;
    }
}
