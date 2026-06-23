package com.scbank.process.api.svc.common.service.securities;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D96000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D96000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H96100Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H96100Res;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.mapper.SecuritiesBlockingMapper;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkApplyBlockingForeignIpMainRequest;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkApplyBlockingForeignIpMainResponse;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkGetBlockingForeignIpRequest;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkGetBlockingForeignIpResponse;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkSetBlockingForeignIpRequest;
import com.scbank.process.api.svc.common.service.securities.dto.blocking.SecBlkSetBlockingForeignIpResponse;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.UserType;
import com.scbank.process.api.svc.shared.integration.HostClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "보안서비스 - 차단", url = "/securities/blocking", author = "허정우")
public class SecuritiesBlockingService {

	// 세션
	private final ISessionContextManager sessionManager;

	// 전문
	private final HostClient hostClient;

	private final SecuritiesBlockingMapper securitiesBlockingMapper;

	/**
	 * 해외IP차단서비스 화면
	 * 
	 * @param IServiceContext
	 * @param SecBlkSetBlockingForeignIpRequest
	 * @return SecBlkSetBlockingForeignIpResponse
	 */
	@ServiceEndpoint(url = "/setBlockingForeignIp", name = "해외IP차단서비스 화면 [ASIS:H96001.jsp]", author = "허정우")
	public SecBlkSetBlockingForeignIpResponse setBlockingForeignIp(IServiceContext ctx,
			SecBlkSetBlockingForeignIpRequest input) {
		log.debug("SecuritiesBlockingService.setBlockingForeignIp 해외IP차단서비스 화면 START");

		SecBlkSetBlockingForeignIpResponse output = SecBlkSetBlockingForeignIpResponse.builder()
				.custName(sessionManager.getLoginValue("CustName", String.class))
				.ipBlockYn(sessionManager.getLoginValue("IPBlockYN", String.class))
				.ipProtectYn(sessionManager.getLoginValue("IPProtectYN", String.class))
				.connectedKoreaYn(sessionManager.getLoginValue("ConnectedKoreaYN", String.class))
				.connectType(sessionManager.getLoginValue("ConnectType", String.class))
				.userType(sessionManager.getLoginValue("UserType", String.class))
				.userWebType(UserType.USER_WEB_TYPE.getCode()).build();

		log.debug("SecuritiesBlockingService.setBlockingForeignIp 해외IP차단서비스 화면 END");
		return output;
	}

	/**
	 * 해외IP차단서비스 조회
	 * 
	 * @param IServiceContext
	 * @param SecBlkGetBlockingForeignIpRequest
	 * @return SecBlkGetBlockingForeignIpResponse
	 */
	@ServiceEndpoint(url = "/getBlockingForeignIp", name = "해외IP차단서비스 조회 [ASIS:H96001.jsp]", author = "허정우")
	public SecBlkGetBlockingForeignIpResponse getBlockingForeignIp(IServiceContext ctx,
			SecBlkGetBlockingForeignIpRequest input) {
		log.debug("SecuritiesBlockingService.getBlockingForeignIp 해외IP차단서비스 조회 START");

		// TI1IBK01_H961 (CB_IBK01_H961)
		OltpRequestOptions oltpRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H961");

		// 필수 공통부 설정
		oltpRequestOptions.setImsTranCd("TI1IBK01");
		oltpRequestOptions.setInClassCd("H961");
		oltpRequestOptions.setSvcCd("961");
		oltpRequestOptions.setCaptureSystem("OLTP");
		// 필수 공통부 설정

		// 데이터부 설정
		CbIbk01H96100Req sendData = new CbIbk01H96100Req();

		OltpResponse<CbIbk01H96100Res> oltpResponse = hostClient.sendOltp(oltpRequestOptions, sendData,
				CbIbk01H96100Res.class);
		SecBlkGetBlockingForeignIpResponse output = this.securitiesBlockingMapper
				.toSecBlkGetBlockingForeignIpResponse(oltpResponse.getResponse());
		log.debug("SecuritiesBlockingService.getBlockingForeignIp output : " + output);

		log.debug("SecuritiesBlockingService.getBlockingForeignIp 해외IP차단서비스 조회 END");
		return output;
	}

	/**
	 * 해외IP차단서비스 본거래
	 * 
	 * @param IServiceContext
	 * @param SecBlkApplyBlockingForeignIpMainRequest
	 * @return SecBlkApplyBlockingForeignIpMainResponse
	 */
	@ServiceEndpoint(url = "/applyBlockingForeignIpMain", name = "해외IP차단서비스 본거래 [ASIS:H96001.jsp]", author = "허정우")
	public SecBlkApplyBlockingForeignIpMainResponse applyBlockingForeignIpMain(IServiceContext ctx,
			SecBlkApplyBlockingForeignIpMainRequest input) {
		log.debug("SecuritiesBlockingService.getBlockingForeignIp 해외IP차단서비스 본거래 START");

		// TI1IBK01_H960 (CB_IBK01_D960)
		OltpRequestOptions oltpRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D960");

		// 필수 공통부 설정
		oltpRequestOptions.setImsTranCd("TI1IBK01");
		oltpRequestOptions.setInClassCd("D960");
		oltpRequestOptions.setSvcCd("960");
		oltpRequestOptions.setCaptureSystem("OLTP");
		// 필수 공통부 설정

		// 데이터부 설정
		CbIbk01D96000Req sendData = this.securitiesBlockingMapper.toCbIbk01D96000Req(input);

		// 2.전자서명 검증관련 속성 설정
		oltpRequestOptions.setAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.VERIFY_N_SAVE);

		OltpResponse<CbIbk01D96000Res> oltpResponse = hostClient.sendOltp(oltpRequestOptions, sendData,
				CbIbk01D96000Res.class);
		SecBlkApplyBlockingForeignIpMainResponse output = this.securitiesBlockingMapper
				.toSecBlkApplyBlockingForeignIpMainResponse(oltpResponse.getResponse());
		log.debug("SecuritiesBlockingService.getBlockingForeignIp output : " + output);

		sessionManager.setLoginValue("IPProtectYN", output.getYoSTGB());
		log.debug("SecuritiesBlockingService.getBlockingForeignIp 해외IP차단서비스 조회 END");
		return output;
	}
}
