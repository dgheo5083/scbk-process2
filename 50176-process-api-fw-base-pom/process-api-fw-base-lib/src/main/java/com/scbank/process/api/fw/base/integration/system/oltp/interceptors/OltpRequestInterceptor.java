package com.scbank.process.api.fw.base.integration.system.oltp.interceptors;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OLTP 요청 데이터 공통설정 인터셉터 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component("oltpRequestInterceptor")
public class OltpRequestInterceptor implements IntegrationInterceptor {

	/**
	 * 세션 매니저 인터페이스
	 */
	private final ISessionContextManager sessionContextManager;

	@SuppressWarnings("rawtypes")
	@Override
	public void before(IntegrationContext context, Object request) {
		if (!"host".equals(context.getSystemId())) {
			log.debug("# 호스트 거래가 아니므로 skip 처리 [{}]", context.getSystemId());
			return;
		}

		OltpRequest oltpRequest = (OltpRequest) request;
		OltpReqHeader header = oltpRequest.getHeader();
		OltpCommon common = header.getOltpCommon();
		IMessageObject requestDto = oltpRequest.getRequestMessage();

		log.debug("## request dto type={}", requestDto.getClass().getSimpleName());

		// 로그인 여부에 따라 설정을 다르게 한다.
		if (this.sessionContextManager.isLogin()) {
			// *************************************************
			// 사용자ID 설정 처리
			// *************************************************
			String userId = StringUtils.defaultIfEmpty(this.sessionContextManager.getLoginValue("UserID", String.class),
					StringUtils.EMPTY);
			String requestUserId = StringUtils.defaultIfEmpty(ReflectionUtils.getFieldValue(requestDto, "UserID"),
					StringUtils.EMPTY);

			log.debug("## login session userId={}", userId);
			log.debug("## request dto requestUserId={}", requestUserId);

			if (StringUtils.hasLength(requestUserId) && !"null".equals(requestUserId)) {
				userId = requestUserId;
			}

			ReflectionUtils.setFieldValue(requestDto, "UserID", userId);

			// *************************************************
			// 고객명 처리
			// *************************************************
			String custName = StringUtils.defaultIfEmpty(
					this.sessionContextManager.getLoginValue("CustName", String.class), StringUtils.EMPTY);
			ReflectionUtils.setFieldValue(requestDto, "CustName", custName);

			// 등록점번 처리
			String regiBranchNum = StringUtils.defaultIfEmpty(
					this.sessionContextManager.getLoginValue("RegiBranchNum", String.class), StringUtils.EMPTY);
			common.setBranchNo(regiBranchNum);

			// *************************************************
			// 본거래 일경우에만 아래의 항목의 값을 세션에 넣어준다.
			// *************************************************
			if (context.getAttribute("isRealTran", Boolean.class)) {
				String safeCardBranchNum = StringUtils.defaultIfEmpty(
						sessionContextManager.getLoginValue("SafeCardBranchNum", String.class), StringUtils.EMPTY);
				String safeCardINDEX = StringUtils.defaultIfEmpty(
						sessionContextManager.getLoginValue("SafeCardINDEX", String.class), StringUtils.EMPTY);
				String safeCardINDEX2 = StringUtils.defaultIfEmpty(
						sessionContextManager.getLoginValue("SafeCardINDEX2", String.class), StringUtils.EMPTY);

				ReflectionUtils.setFieldValue(requestDto, "SafeCardBranchNum", safeCardBranchNum);
				ReflectionUtils.setFieldValue(requestDto, "SafeCardINDEX", safeCardINDEX);
				ReflectionUtils.setFieldValue(requestDto, "SafeCardINDEX2", safeCardINDEX2);

			}

			// *************************************************
			// 사용자 통신비밀번호 설정
			// *************************************************
			String tsPswd = StringUtils
					.defaultIfEmpty(sessionContextManager.getLoginValue("TSPassword", String.class), StringUtils.EMPTY);
			ReflectionUtils.setFieldValue(requestDto, "TSPassword", tsPswd);

			if (StringUtils.hasLength(tsPswd)) {
				String perBusNo = StringUtils.defaultIfEmpty(
						sessionContextManager.getLoginValue("PerBusNo", String.class), StringUtils.EMPTY);

				// asis 기준 NotSettingPerBusNo 필드를 사용하지 않으므로 무조건 주민번호를 설정하도록 한다.
				ReflectionUtils.setFieldValue(requestDto, "PerBusNo", perBusNo);

				String perBusNo1 = ReflectionUtils.getFieldValue(requestDto, "PerBusNo1");
				if (!"".equals(perBusNo1)) {
					ReflectionUtils.setFieldValue(requestDto, "PerBusNo1", perBusNo);
				}
			}

			// *************************************************
			// 이체비밀번호 설정
			// *************************************************
			String transPwUseYn = StringUtils.defaultIfEmpty(
					sessionContextManager.getLoginValue("TransPWUseYN", String.class), StringUtils.EMPTY);
			if ("1".equals(transPwUseYn)) {
				ReflectionUtils.setFieldValue(requestDto, "TransPassword", "99999999");
			}
		} else {
			// 비로그인 처리
			// *************************************************
			// 사용자ID 설정 처리
			// *************************************************
			String userId = StringUtils.defaultIfEmpty(
					this.sessionContextManager.getGlobalValue("UserID", String.class), StringUtils.EMPTY);
			String requestUserId = ReflectionUtils.getFieldValue(requestDto, "UserID");

			log.debug("## global session userId={}", userId);
			log.debug("## request dto requestUserId={}", requestUserId);

			if (StringUtils.hasLength(requestUserId) && !"null".equals(requestUserId)) {
				userId = requestUserId;
			}

			if (StringUtils.hasLength(userId)) {
				ReflectionUtils.setFieldValue(requestDto, "UserID", userId);
			}

			// *************************************************
			// 고객명 처리
			// *************************************************
			String custName = StringUtils.defaultIfEmpty(
					this.sessionContextManager.getGlobalValue("CustName", String.class), StringUtils.EMPTY);
			if (StringUtils.hasLength(custName)) {
				ReflectionUtils.setFieldValue(requestDto, "CustName", custName);
			}

			String safeCardBranchNum = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("SafeCardBranchNum", String.class), StringUtils.EMPTY);
			String safeCardINDEX = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("SafeCardINDEX", String.class), StringUtils.EMPTY);
			String safeCardINDEX2 = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("SafeCardINDEX2", String.class), StringUtils.EMPTY);

			if (StringUtils.isNotEmpty(safeCardBranchNum)) {
				ReflectionUtils.setFieldValue(requestDto, "SafeCardBranchNum", safeCardBranchNum);
			}

			if (StringUtils.isNotEmpty(safeCardINDEX)) {
				ReflectionUtils.setFieldValue(requestDto, "SafeCardINDEX", safeCardINDEX);
			}

			if (StringUtils.isNotEmpty(safeCardINDEX2)) {
				ReflectionUtils.setFieldValue(requestDto, "SafeCardINDEX2", safeCardINDEX2);
			}

			// *************************************************
			// 사용자 통신비밀번호 설정
			// *************************************************
			String tSPassword = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("TSPassword", String.class), StringUtils.EMPTY);

			if (StringUtils.hasLength(tSPassword)) {
				ReflectionUtils.setFieldValue(requestDto, "TSPassword", tSPassword);
				String perBusNo = StringUtils.defaultIfEmpty(
						sessionContextManager.getLoginValue("PerBusNo", String.class), StringUtils.EMPTY);
				String perBusNo1 = ReflectionUtils.getFieldValue(requestDto, "PerBusNo1");
				if (!"".equals(perBusNo1)) {
					ReflectionUtils.setFieldValue(requestDto, "PerBusNo1", perBusNo);
				}
			}

			// *************************************************
			// 이체비밀번호 설정
			// *************************************************
			String transPwUseYn = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("TransPWUseYN", String.class), StringUtils.EMPTY);
			if ("1".equals(transPwUseYn)) {
				ReflectionUtils.setFieldValue(requestDto, "TransPassword", "99999999");
			}
		}

		if (context.getAttribute("isPreTran", Boolean.class)) {
			ReflectionUtils.setFieldValue(requestDto, "TransPassword", "99999999");
		}

		// *************************************************
		// S - 2019-05-01 업무팀 요청 - 다건 본거래 시 보안매체 태그에서 넘긴 Param 이체비밀번호 셋팅
		// *************************************************
		if (context.getAttribute("isRealTran", Boolean.class)) {
			String transPwUseYn = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("TransPWUseYN", String.class), StringUtils.EMPTY);
			if (this.sessionContextManager.isLogin()) {
				transPwUseYn = StringUtils.defaultIfEmpty(
						sessionContextManager.getLoginValue("TransPWUseYN", String.class), StringUtils.EMPTY);
			}

			// ASIS 보안매체 tag Params 사용 -> TOBE 보안매체 검증시 설정한 세션값으로 변경 - 허동규
			String transPassword = StringUtils.defaultIfEmpty(
					sessionContextManager.getGlobalValue("transPassword", String.class), StringUtils.EMPTY);
			ReflectionUtils.setFieldValue(requestDto, "TransPassword",
					"1".equals(transPwUseYn) ? "99999999" : transPassword);
		}

		// *************************************************
		// 로그인여부를 떠나서 조회성 거래 UserID/TSPassword 를 세팅한다.
		// *************************************************
		String noneUserId = StringUtils.defaultIfEmpty(ReflectionUtils.getFieldValue(requestDto, "UserID"), "")
				.toString().toUpperCase();
		if (("FIRST995".equalsIgnoreCase(noneUserId) || "FIRST999".equalsIgnoreCase(noneUserId)) &&
				"".equalsIgnoreCase(ReflectionUtils.getFieldValue(requestDto, "TSPassword"))) {
			ReflectionUtils.setFieldValue(requestDto, "TSPassword", "111111");
		}
	}
}
