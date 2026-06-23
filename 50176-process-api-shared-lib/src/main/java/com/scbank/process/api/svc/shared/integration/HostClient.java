package com.scbank.process.api.svc.shared.integration;

import com.scbank.process.api.fw.base.integration.system.edmi.EdmiManager;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiRequestOptions;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiResponse;
import com.scbank.process.api.fw.base.integration.system.mci.MciManager;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.mci.rebound.MciReboundStrategy;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManager;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.rebound.OltpReboundStrategy;
import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 제일은행 호스트 시스템 전문 송/수신 처리 Facade 클래스
 * 연계시스템 전문 송/수신은 해당 매니저 클래스를 이용하여 처리한다.
 */
@SharedComponent
@RequiredArgsConstructor
@Slf4j
public class HostClient {

	/**
	 * OLTP hexstring 전문 송/수신 매니저
	 */
	private final OltpManager hostManager;

	/**
	 * 호스트 json mapping 전문 송/수신 매니저
	 */
	private final EdmiManager edmiManager;

	/**
	 * MCI 전문 송/수신 매니저
	 */
	private final MciManager mciManager;

	/**
	 * 
	 */
	private final SecureDataComponent secureData;

	/**
	 * OLTP 사용자 커스텀 설정 객체 획득
	 * 
	 * @param interfaceId EDMi 인터페이스ID
	 * @return
	 */
	@ComponentOperation(name = "OLTP 시스템 사용자 커스텀 설정 객체 획득", author = "sungdon.choi")
	public OltpRequestOptions getOltpRequestOptions(String interfaceId) {
		// 추적 ID
		String trackingId = ThreadLocalStoreDelegator.getTrackingId();

		OltpRequestOptions cfg = new OltpRequestOptions();
		cfg.setInterfaceId(interfaceId);
		cfg.setTrackingId(trackingId);
		cfg.setExceptionOnError(true);

		// cfg.setCaptureSystem("OLTP");
		cfg.setTypeName("CoreBanking:mbOLTPCommonRoute");
		cfg.setMessageSenderBody("MB");
		cfg.setSenderDomainBody("CoreBanking");
		return cfg;
	}

	/**
	 * OLTP 사용자 커스텀 설정 객체 획득
	 * 
	 * @param interfaceId
	 * @param captureSystem
	 * @param tranCd
	 * @return
	 */
	@ComponentOperation(name = "OLTP 시스템 사용자 커스텀 설정 객체 획득", author = "sungdon.choi")
	public OltpRequestOptions getOltpRequestOptions(String interfaceId, String captureSystem, String tranCd) {
		// 추적 ID
		String trackingId = ThreadLocalStoreDelegator.getTrackingId();

		OltpRequestOptions cfg = new OltpRequestOptions();
		cfg.setInterfaceId(interfaceId);
		cfg.setCaptureSystem(captureSystem);
		cfg.setImsTranCd(tranCd);
		cfg.setTrackingId(trackingId);
		cfg.setExceptionOnError(true);

		// cfg.setCaptureSystem("OLTP");
		cfg.setTypeName("CoreBanking:mbOLTPCommonRoute");
		cfg.setMessageSenderBody("MB");
		cfg.setSenderDomainBody("CoreBanking");
		return cfg;
	}

	/**
	 * EDMi 전문 거래 사용자 커스텀 설정 객체 획득
	 * 
	 * @param interfaceId EDMI 인터페이스 ID
	 * @return
	 */
	@ComponentOperation(name = "EDMi 전문 거래 사용자 커스텀 설정 객체 획득", author = "sungdon.choi")
	public EdmiRequestOptions getEdmiRequestOptions(String interfaceId) {
		// 추적 ID
		String trackingId = ThreadLocalStoreDelegator.getTrackingId();

		EdmiRequestOptions cfg = new EdmiRequestOptions();
		cfg.setInterfaceId(interfaceId);
		cfg.setTrackingId(trackingId);
		cfg.setExceptionOnError(true);
		return cfg;
	}

	/**
	 * 
	 * @param interfaceId
	 * @return
	 */
	@ComponentOperation(name = "MCI 시스템 사용자 커스텀 설정 객체 획득", author = "sungdon.choi")
	public MciRequestOptions getMciRequestOptions(String interfaceId) {
		// 추적 ID
		String trackingId = ThreadLocalStoreDelegator.getTrackingId();

		MciRequestOptions cfg = new MciRequestOptions();
		cfg.setInterfaceId(interfaceId);
		cfg.setCaptureSystem("");
		cfg.setTrackingId(trackingId);
		cfg.setExceptionOnError(true);
		return cfg;
	}

	/**
	 * EDMi OLTP(호스트) 전문 송/수신 처리
	 *
	 * @param cfg          업무별 연계 설정
	 * @param input        요청 메시지 (IMessageObject)
	 * @param responseType 응답 메시지 타입
	 * @param <I>          요청 메시지 타입
	 * @param <O>          응답 메시지 타입
	 * @return HostResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "EDMi OLTP(호스트) Hex 문자열 기반 전문 송/수신 처리", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> sendOltp(OltpRequestOptions cfg,
			I input, Class<O> responseType) throws IntegrationException {
		return this.hostManager.send(cfg, input, responseType);
	}

	/**
	 * EDMi OLTP(호스트) 전문 송/수신 처리 (보안매체 체크 전처리 포함)
	 *
	 * @param cfg          업무별 연계 설정
	 * @param input        요청 메시지 (IMessageObject)
	 * @param responseType 응답 메시지 타입
	 * @param <I>          요청 메시지 타입
	 * @param <O>          응답 메시지 타입
	 * @return HostResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "EDMi OLTP(호스트) Hex 문자열 기반 전문 송/수신 처리", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> sendOltpWithSecure(
			OltpRequestOptions cfg, I input, Class<O> responseType) throws IntegrationException {

		try {
			secureData.verifyVerification();

		} catch (Exception e) {
			if (RunMode.LOCAL.equals(RuntimeContext.getRunModeString())
					|| RunMode.UT.equals(RuntimeContext.getRunModeString())) {
				e.printStackTrace();
				log.debug("HDG sendOltpWithSecure VerifyVerification Error, {}", e.getMessage());
			} else {
				throw e;
			}
		}

		return this.hostManager.send(cfg, input, responseType);
	}

	/**
	 * EDMi OLTP(호스트) 전문 리바운드 거래 요청/응답
	 * 
	 * @param <I>          IMessageObject 인터페이스를 구현한 요청 메시지 타입
	 * @param <O>          IMessageObject 인터페이스를 구현한 응답 메시지 타입
	 * @param cfg          업무별 연계 설정
	 * @param input        요청 메시지 (IMessageObject)
	 * @param responseType 응답 메시지 타입
	 * @return HostResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "EDMi OLTP(호스트) Hex 문자열 기반 전문 리바운드 거래 요청/응답", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> sendOltpWithRebound(
			OltpRequestOptions cfg, I input, Class<O> responseType) throws IntegrationException {
		return this.sendOltpWithRebound(cfg, input, responseType, new OltpReboundStrategy());
	}

	/**
	 * EDMi OLTP(호스트) 전문 리바운드 거래 요청/응답
	 * 
	 * @param <I>             IMessageObject 인터페이스를 구현한 요청 메시지 타입
	 * @param <O>             IMessageObject 인터페이스를 구현한 응답 메시지 타입
	 * @param cfg             업무별 연계 설정
	 * @param input           요청 메시지 (IMessageObject)
	 * @param responseType    응답 메시지 타입
	 * @param reboundStrategy 호스트 리바운드거래 핸들러 Strategy 구현 객체
	 * @return HostResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "EDMi OLTP(호스트) Hex 문자열 기반 전문 리바운드 거래 요청/응답", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> OltpResponse<O> sendOltpWithRebound(
			OltpRequestOptions cfg,
			I input,
			Class<O> responseType, OltpReboundStrategy reboundStrategy) throws IntegrationException {
		if (reboundStrategy == null) {
			reboundStrategy = new OltpReboundStrategy();
		}
		return this.hostManager.sendWithRebound(cfg, input, responseType, reboundStrategy);
	}

	/**
	 * 
	 * @param <I>
	 * @param <O>
	 * @param cfg
	 * @param input
	 * @param responseType
	 * @return
	 * @throws IntegrationException
	 */
	@ComponentOperation(name = "EDMi json 매핑 거래 요청/응답", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> EdmiResponse<O> sendEdmi(EdmiRequestOptions cfg,
			I input, Class<O> responseType) throws IntegrationException {
		return this.edmiManager.send(cfg, input, responseType);
	}

	/**
	 * 
	 * @param <I>
	 * @param <O>
	 * @param cfg
	 * @param input
	 * @param responseType
	 * @return
	 * @throws IntegrationException
	 */
	@ComponentOperation(name = "MCI 전문 거래 요청/응답", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> sendMci(MciRequestOptions cfg,
			I input, Class<O> responseType) throws IntegrationException {
		return this.mciManager.send(cfg, input, responseType);
	}

	/**
	 * MCI 전문 송/수신 처리 (보안매체 체크 전처리 포함)
	 *
	 * @param cfg          업무별 연계 설정
	 * @param input        요청 메시지 (IMessageObject)
	 * @param responseType 응답 메시지 타입
	 * @param <I>          요청 메시지 타입
	 * @param <O>          응답 메시지 타입
	 * @return MciResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "MCI 전문 거래 요청/응답 with Secure", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> sendMciWithSecure(
			MciRequestOptions cfg, I input, Class<O> responseType) throws IntegrationException {

		try {
			secureData.verifyVerification();

		} catch (Exception e) {
			if (RunMode.LOCAL.equals(RuntimeContext.getRunModeString())
					|| RunMode.UT.equals(RuntimeContext.getRunModeString())) {
				e.printStackTrace();
				log.debug("HDG sendOltpWithSecure VerifyVerification Error, {}", e.getMessage());
			} else {
				throw e;
			}
		}

		return this.mciManager.send(cfg, input, responseType);
	}

	/**
	 * MCI 전문 리바운드 거래 요청/응답
	 * 
	 * @param <I>          IMessageObject 인터페이스를 구현한 요청 메시지 타입
	 * @param <O>          IMessageObject 인터페이스를 구현한 응답 메시지 타입
	 * @param cfg          업무별 연계 설정
	 * @param input        요청 메시지 (IMessageObject)
	 * @param responseType 응답 메시지 타입
	 * @return MCIResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "MCI 전문 리바운드 거래 요청/응답", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> sendMciWithRebound(
			MciRequestOptions cfg, I input, Class<O> responseType) throws IntegrationException {
		return this.sendMciWithRebound(cfg, input, responseType, new MciReboundStrategy());
	}

	/**
	 * MCI 전문 리바운드 거래 요청/응답
	 * 
	 * @param <I>             IMessageObject 인터페이스를 구현한 요청 메시지 타입
	 * @param <O>             IMessageObject 인터페이스를 구현한 응답 메시지 타입
	 * @param cfg             업무별 연계 설정
	 * @param input           요청 메시지 (IMessageObject)
	 * @param responseType    응답 메시지 타입
	 * @param reboundStrategy MCI 리바운드거래 핸들러 Strategy 구현 객체
	 * @return MCIResponse<O>
	 * @throws IntegrationException 전문 송/수신 실패 시 발생한 예외
	 */
	@ComponentOperation(name = "MCI 전문 리바운드 거래 요청/응답", author = "sungdon.choi")
	public <I extends IMessageObject, O extends IMessageObject> MciResponse<O> sendMciWithRebound(
			MciRequestOptions cfg,
			I input,
			Class<O> responseType, MciReboundStrategy reboundStrategy) throws IntegrationException {
		if (reboundStrategy == null) {
			reboundStrategy = new MciReboundStrategy();
		}
		return this.mciManager.sendWithRebound(cfg, input, responseType, reboundStrategy);
	}
}
