package com.scbank.process.api.fw.base.integration.system.oltp;

import java.time.DayOfWeek;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.base.integration.uuid.IntegrationTranNoGenerator;
import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 호스트 요청 헤더 빌더 구현 클래스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 18.
 */
@Slf4j
@RequiredArgsConstructor
public class OltpRequestHeaderBuilder implements IntegrationRequestHeaderBuilder<OltpReqHeader, OltpRequestOptions> {

	/**
	 * 전문 거래번호 생성기
	 */
	private final IntegrationTranNoGenerator integrationTranNoGenerator;
	
	@Override
	public OltpReqHeader build(Map<String, Object> defaultHeader, OltpRequestOptions cfg) {

		OltpCommon hostCommon = cfg.getOltpCommon();
		if (hostCommon == null) {
			hostCommon = new OltpCommon();
		}

		// HOST 공통부 기본값 설정한다.
		if (!CollectionUtils.isEmpty(defaultHeader)) {
			BeanUtils.copyProperties(defaultHeader, hostCommon);
		}

		// 호스트 필수 코드 - 전문코드
		String imsTranCd = StringUtils.defaultIfEmpty(cfg.getImsTranCd(), StringUtils.EMPTY).trim();
		// 대상 점번호
		String branchNo = StringUtils.defaultIfEmpty(cfg.getBranchNo(), StringUtils.EMPTY).trim();
		// 업무구분
		String jobTp = StringUtils.defaultIfEmpty(cfg.getJobTp(), StringUtils.EMPTY).trim();
		// 입력식별코드
		String inClassCd = StringUtils.defaultIfEmpty(cfg.getInClassCd(), StringUtils.EMPTY).trim();
		// 서비스코드
		String svcCd = StringUtils.defaultIfEmpty(cfg.getSvcCd(), StringUtils.EMPTY).trim();
		// PROCESS_TYPE : 데이터 안들어오면 F(first)로 처리 - 처리구분
		String processTp = StringUtils.defaultIfEmpty(cfg.getProcessTp(), StringUtils.EMPTY).trim();
		// VAN_TYPE : 데이터 안들어 오면 15로 처리 - SHELFNO/VAN구분
		String vanTp = StringUtils.defaultIfEmpty(cfg.getVanTp(), StringUtils.EMPTY).trim();
		// 소프트그램의 호출을 대비해서 추가 - 입출력매체구분
		String inOutCnlTp = StringUtils.defaultIfEmpty(cfg.getInOutCnlTp(), StringUtils.EMPTY).trim();
		// 전문전송일
		String sendDateTime = StringUtils.defaultIfEmpty(hostCommon.getSendDate(), StringUtils.EMPTY).trim();

		// 공통부 필수 입력필드 체크

		// 전문코드 길이 체크
		if (imsTranCd.length() != 8) {
			//
		}

		// 업무구분 공백인경우 기본값 설정
		if ("".equals(jobTp)) {
			jobTp = "GP";
		}

		// 입력식별코드 필드 길이 체크
		if (inClassCd.length() != 4) {
			//
		}

		// 서비스코드 필드 길이 체크
		if (svcCd.length() != 3) {
			//
		}
		
		// VAN_TYPE 들어 왔는지 체크
        // 현재는 우선 데이터 안들어 오면 47로 처리하도록 함
		//[2026.06.19 최성돈] 신규 망구분코드 적용
		//모바일뱅킹: 
		if (!StringUtils.hasLength(vanTp)) {
			//vanTp = IntegrationConstant.KR_SMB_VAN_TYPE;
			String channelId = StringUtils.defaultIfEmpty(ThreadLocalStoreDelegator.getChannelId(), "");
			if (!StringUtils.hasLength(channelId)) {
				vanTp = IntegrationConstant.KR_SMB_VAN_TYPE;
			} else {
				vanTp = "IB".equals(channelId) ? IntegrationConstant.KR_IB_VAN_TYPE : IntegrationConstant.KR_SMB_VAN_TYPE;
			}
		}

		hostCommon.setImsTranCd(imsTranCd);
		hostCommon.setJobTp(jobTp);
		hostCommon.setInClassCd(inClassCd);
		hostCommon.setSvcCd(svcCd);
		hostCommon.setProcessTp(processTp);
		hostCommon.setVanTp(vanTp);
		hostCommon.setInOutCnlTp(inOutCnlTp);

		RunMode runMode = RuntimeContext.getRunMode();

		// BRANCHNO 는 개발서버에서는 860, 운영서버에서는 019로 고정 (로그인 전)
		// 로그인 후에는 고객 관리점 번호가 들어와야 함.
		// 현재는 우선 데이터 안들어 오면 로그인 전으로 처리하도록 함
		if (!StringUtils.hasLength(branchNo)) {
			hostCommon.setBranchNo(RunMode.PRD.equals(runMode) ? "019" : "860");
		} else {
			if (branchNo.length() != 3) {
				//TOOD 예외처리
			}

			hostCommon.setBranchNo(branchNo);
		}
		
//		//ASIS 대응
//		String regiBranchNum = this.sessionContextManager.isLogin() ? this.sessionContextManager.getLoginValue("RegiBranchNum", String.class) : StringUtils.EMPTY;
//		if (StringUtils.hasLength(regiBranchNum)) {
//			hostCommon.setBranchNo(regiBranchNum);
//		}

		// 전문 전송일시 처리
		String sendHostDate = DateUtils.getCurrentDate(DateUtils.YYYYMMDD);
		String sendHostTime = DateUtils.getCurrentTime(DateUtils.HHMMSS);

		hostCommon.setSendDate(sendHostDate);
		hostCommon.setSendTime(sendHostTime);

		// 휴일근무 임시 거래 때문에 토,일 인 경우 이틀전 날로 세팅한다.
		if (!RunMode.PRD.equals(runMode)) {
			DayOfWeek dayOfWeek = DateUtils.getTodayDayOfWeek();
			switch (dayOfWeek) {
				case SATURDAY:
					hostCommon.setSendDate(DateUtils.getDate("yyyyMMdd", 'D', -1));
					break;
				case SUNDAY:
					hostCommon.setSendDate(DateUtils.getDate("yyyyMMdd", 'D', -2));
					break;
				default:
					break;
			}
		}

		if (StringUtils.hasLength(sendDateTime)) {
			hostCommon.setSendDate(sendDateTime);
		}

		// 전문 거래번호 설정
		String trCd = this.integrationTranNoGenerator.generateId();

		if (log.isDebugEnabled()) {
			log.debug("# 호스트 시스템 전문일련번호 획득: {}", trCd);
		}

		hostCommon.setTrCd(trCd);

		// 호스트 공통부 복사
		OltpCommon copyed = new OltpCommon();
		BeanUtils.copyProperties(hostCommon, copyed);

		return OltpReqHeader.builder()
				.oltpCommon(copyed)
				.build();
	}
}
