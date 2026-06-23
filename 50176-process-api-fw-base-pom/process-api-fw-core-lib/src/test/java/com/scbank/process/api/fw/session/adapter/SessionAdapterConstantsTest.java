package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link SessionAdapterConstants} 단위 테스트
 */
@DisplayName("SessionAdapterConstants 테스트")
class SessionAdapterConstantsTest {

    @Nested
    @DisplayName("상수 값 테스트")
    class ConstantValueTests {

        @Test
        @DisplayName("FLD_NM_ADAPTER 상수 값이 _adapter_이다")
        void fldNmAdapterValue() {
            // then
            assertEquals("_adapter_", SessionAdapterConstants.FLD_NM_ADAPTER);
        }

        @Test
        @DisplayName("FLD_NM_VERSION_3 상수 값이 v3이다")
        void fldNmVersion3Value() {
            // then
            assertEquals("v3", SessionAdapterConstants.FLD_NM_VERSION_3);
        }

        @Test
        @DisplayName("FLD_NM_VERSION_4 상수 값이 v4이다")
        void fldNmVersion4Value() {
            // then
            assertEquals("v4", SessionAdapterConstants.FLD_NM_VERSION_4);
        }

        @Test
        @DisplayName("FLD_NM_CLASS 상수 값이 @class이다")
        void fldNmClassValue() {
            // then
            assertEquals("@class", SessionAdapterConstants.FLD_NM_CLASS);
        }

        @Test
        @DisplayName("FLD_NM_LOGIN_DATA 상수 값이 loginData이다")
        void fldNmLoginDataValue() {
            // then
            assertEquals("loginData", SessionAdapterConstants.FLD_NM_LOGIN_DATA);
        }

        @Test
        @DisplayName("FLD_NM_LOGINED 상수 값이 logined이다")
        void fldNmLoginedValue() {
            // then
            assertEquals("logined", SessionAdapterConstants.FLD_NM_LOGINED);
        }

        @Test
        @DisplayName("FLD_NM_USERID 상수 값이 userId이다")
        void fldNmUserIdValue() {
            // then
            assertEquals("userId", SessionAdapterConstants.FLD_NM_USERID);
        }

        @Test
        @DisplayName("FLD_NM_CLIENT_IP 상수 값이 clientIp이다")
        void fldNmClientIpValue() {
            // then
            assertEquals("clientIp", SessionAdapterConstants.FLD_NM_CLIENT_IP);
        }

        @Test
        @DisplayName("FLD_NM_SESSION_ID 상수 값이 sessionId이다")
        void fldNmSessionIdValue() {
            // then
            assertEquals("sessionId", SessionAdapterConstants.FLD_NM_SESSION_ID);
        }

        @Test
        @DisplayName("GLOBAL_SESSION_ATTR_KEY 상수 값이 globalSession이다")
        void globalSessionAttrKeyValue() {
            // then
            assertEquals("globalSession", SessionAdapterConstants.GLOBAL_SESSION_ATTR_KEY);
        }

        @Test
        @DisplayName("LOGIN_SESSION_ATTR_KEY 상수 값이 loginSession이다")
        void loginSessionAttrKeyValue() {
            // then
            assertEquals("loginSession", SessionAdapterConstants.LOGIN_SESSION_ATTR_KEY);
        }
    }

    @Nested
    @DisplayName("상수 클래스 테스트")
    class ConstantsClassTests {

        @Test
        @DisplayName("인스턴스를 생성할 수 있다")
        void canCreateInstance() {
            // when
            SessionAdapterConstants constants = new SessionAdapterConstants();

            // then
            assertNotNull(constants);
        }
    }
}
