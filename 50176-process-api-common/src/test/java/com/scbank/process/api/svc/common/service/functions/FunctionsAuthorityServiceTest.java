package com.scbank.process.api.svc.common.service.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckMenuInfoRequest;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckMenuInfoResponse;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckServiceTimeRequest;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckServiceTimeResponse;
import com.scbank.process.api.svc.shared.components.accesscontrol.MenuAuthorityCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.ServiceTimeCheckComponent;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FunctionsAuthorityService")
class FunctionsAuthorityServiceTest {

    @Mock
    private MenuAuthorityCheckComponent menuAuthorityCheckComponent;

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private ServiceTimeCheckComponent serviceTimeCheckComponent;

    @InjectMocks
    private FunctionsAuthorityService service;

    @Nested
    @DisplayName("checkMenuInfo")
    class CheckMenuInfo {

        @Test
        @DisplayName("메뉴권한 체크 성공 시 resultCd 00")
        void checkMenuInfoSuccessTest() {
            FncAutCheckMenuInfoRequest request = new FncAutCheckMenuInfoRequest();
            request.setMenuId("MENU01");
            request.setAcType("R");
            request.setForceCheckCode("CODE01");

            FncAutCheckMenuInfoResponse response = service.checkMenuInfo(request);

            assertEquals("00", response.getResultCd());
            verify(menuAuthorityCheckComponent).checkAuthority(any());
            verify(sessionManager).removeGlobalValue("FIND_USER_ID_TRCD");
            verify(sessionManager).removeGlobalValue("transPassword");
            verify(sessionManager).removeGlobalValue("LogSkip");
        }
    }

    @Nested
    @DisplayName("checkServiceTime")
    class CheckServiceTime {

        @Test
        @DisplayName("이용시간 체크 성공 시 resultCd 00")
        void checkServiceTimeSuccessTest() {
            FncAutCheckServiceTimeRequest request = new FncAutCheckServiceTimeRequest();
            request.setForceCheckCode("CODE01");

            FncAutCheckServiceTimeResponse response = service.checkServiceTime(request);

            assertEquals("00", response.getResultCd());
            verify(serviceTimeCheckComponent).checkServiceTime(any());
        }

        @Test
        @DisplayName("이용시간 예외코드 PRCCMM0027은 resultCd로 반환")
        void checkServiceTimeKnownErrorTest() {
            FncAutCheckServiceTimeRequest request = new FncAutCheckServiceTimeRequest();
            request.setForceCheckCode("CODE01");

            doThrow(new PRCServiceException("PRCCMM0027", "이용시간 외"))
                    .when(serviceTimeCheckComponent).checkServiceTime(any());

            FncAutCheckServiceTimeResponse response = service.checkServiceTime(request);

            assertEquals("PRCCMM0027", response.getResultCd());
        }

        @Test
        @DisplayName("알 수 없는 예외코드는 재throw")
        void checkServiceTimeUnknownErrorTest() {
            FncAutCheckServiceTimeRequest request = new FncAutCheckServiceTimeRequest();
            request.setForceCheckCode("CODE01");

            doThrow(new PRCServiceException("PRCCMM9999", "기타오류"))
                    .when(serviceTimeCheckComponent).checkServiceTime(any());

            assertThrows(PRCServiceException.class, () -> service.checkServiceTime(request));
        }
    }
}
