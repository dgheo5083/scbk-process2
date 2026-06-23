package com.scbank.process.api.svc.shared.components.fds.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * FDS로그 적재 데이터
 */
@Data
@Builder
public class FdsLogData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String LOG_FORMAT = "%s|%s|%s|%s|%s|%07d|%07d|%-10s|%-15s|%s|%s|%s|%s|%s|%-14s|%-5s|";

	/**
	 * 현재일자
	 */
	private String curDate;
	
	/**
	 * 현재시각
	 */
	private String curTime;
	
	/**
	 * 채널구분
	 */
	private String channelType;
	
	/**
	 * 
	 */
	private String wasNum;
	
	/**
	 * 송/수신 구분
	 */
	private String txDscd;
	
	/**
	 * 전문일련번호
	 */
	private int wasTrnaNo;
	
	/**
	 * 
	 */
	private int egmsTranNo;
	
	/**
	 * 사용자ID
	 */
	private String userId;
	
	/**
	 * 사용자IP
	 */
	private String userIp;
	
	/**
	 * IMS 거래코드
	 */
	private String imsTranCd;
	
	/**
	 * 입력 식별 코드
	 */
	private String inClassCd;
	
	/**
	 * 업무 식별 코드
	 */
	private String jobCd;
	
	/**
	 * 서비스 코드
	 */
	private String svcCd;
	
	/**
	 * 요청 데이터
	 */
	private String data;
	
	/**
	 * FDS 디바이스키
	 */
	private String deviceKey;
	
	/**
	 * App Version
	 */
	private String appVersion;

	@Override
	public String toString() {
		return String.format(LOG_FORMAT
				, curDate
				, curTime
				, channelType
				, wasNum
				, txDscd
				, wasTrnaNo
				, egmsTranNo
				, userId
				, userIp
				, imsTranCd
				, inClassCd
				, jobCd
				, svcCd
				, data
				, deviceKey
				, appVersion
		);
	}
}
