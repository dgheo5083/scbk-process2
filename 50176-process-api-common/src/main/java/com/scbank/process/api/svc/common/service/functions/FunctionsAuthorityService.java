package com.scbank.process.api.svc.common.service.functions;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckMenuInfoRequest;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckMenuInfoResponse;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckServiceTimeRequest;
import com.scbank.process.api.svc.common.service.functions.dto.authority.FncAutCheckServiceTimeResponse;
import com.scbank.process.api.svc.shared.components.accesscontrol.MenuAuthorityCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.ServiceTimeCheckComponent;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.MenuAuthorityCheckRequest;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.ServiceTimeCheckRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통기능-메뉴권한 처리 서비스 컴포넌트", url = "/functions/authority")
public class FunctionsAuthorityService {

	/**
	 * 메뉴 접근제어 처리 공유 컴포넌트
	 */
	private final MenuAuthorityCheckComponent menuAuthorityCheckComponent;

	private final ISessionContextManager sessionManager;

	/**
	 * 서비스 이용시간 체크 공유 컴포넌트
	 */
	private final ServiceTimeCheckComponent serviceTimeCheckComponent;

	@ServiceEndpoint(name = "메뉴권한 체크", url = "/checkMenuInfo", author = "sungdon.choi")
	public FncAutCheckMenuInfoResponse checkMenuInfo(FncAutCheckMenuInfoRequest request) {

		FncAutCheckMenuInfoResponse response = new FncAutCheckMenuInfoResponse();

		// 메뉴 세션 초기화
		this.removeMenuSessionInfo();

		this.menuAuthorityCheckComponent.checkAuthority(MenuAuthorityCheckRequest.builder()
				.menuId(request.getMenuId())
				.acType(request.getAcType())
				.forceCheckCode(request.getForceCheckCode())
				.build());

		response.setResultCd("00");

		return response;
	}

	@ServiceEndpoint(name = "화면별 서비스 이용시간 체크", url = "/checkServiceTime", author = "sungdon.choi")
	public FncAutCheckServiceTimeResponse checkServiceTime(FncAutCheckServiceTimeRequest request) {

		FncAutCheckServiceTimeResponse response = new FncAutCheckServiceTimeResponse();

		String resultCd = "00";
		try {
			serviceTimeCheckComponent.checkServiceTime(ServiceTimeCheckRequest.builder()
					.type("menu")
					.code(request.getForceCheckCode())
					.forceCheckCode(request.getForceCheckCode())
					.build());
		} catch (PRCServiceException e) {
			String errorCode = e.getErrorCode();
			if ("PRCCMM0027".equals(errorCode) || "PRCCMM0028".equals(errorCode) || "PRCCMM0054".equals(errorCode)) {
				resultCd = errorCode;
			} else {
				throw e;
			}
		}
		response.setResultCd(resultCd);

		return response;
	}

	/**
	 * 메뉴 접근시 메뉴별 세션 초기화 - 허동규
	 */
	private void removeMenuSessionInfo() {

		sessionManager.removeGlobalValue("FIND_USER_ID_TRCD");
		sessionManager.removeGlobalValue("FIND_USER_ID_TEL1");
		sessionManager.removeGlobalValue("FIND_USER_ID_TEL2");
		sessionManager.removeGlobalValue("FIND_USER_ID_TEL3");

		sessionManager.removeGlobalValue("GUEST_USER_TRCD");
		sessionManager.removeGlobalValue("GUEST_USER_TEL1");
		sessionManager.removeGlobalValue("GUEST_USER_TEL2");
		sessionManager.removeGlobalValue("GUEST_USER_TEL3");

		sessionManager.removeGlobalValue("transPassword");
		sessionManager.removeGlobalValue("fidoSignSkipYn");
		sessionManager.removeGlobalValue("motpSignSkipYn");
		sessionManager.removeGlobalValue("myAcctSkipYn");
		sessionManager.removeGlobalValue("LogSkip");

	}
}
