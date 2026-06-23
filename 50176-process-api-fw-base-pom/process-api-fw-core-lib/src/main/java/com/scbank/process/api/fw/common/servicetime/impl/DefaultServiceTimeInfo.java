package com.scbank.process.api.fw.common.servicetime.impl;

import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;

import lombok.Builder;
import lombok.Data;

/**
 * 서비스 이용시간 정보 구현 클래스
 */
@Data
@Builder
public class DefaultServiceTimeInfo implements IServiceTimeInfo {

	private static final long serialVersionUID = 1L;

	/**
	 * 서비스 이용시간 그룹
	 */
	private ServiceTimeGroup group;

	/**
	 * 서비스 이용시간 코드 (메뉴ID/전문ID)
	 */
	private String code;

	/**
	 * 서비스 이용시간 체크여부 (Y: 체크함, N: 체크안함)
	 */
	private String chkYn;

	/**
	 * 설명
	 */
	private String description;

	/**
	 * 서비스 이용시간 불가 이후 이동할 페이지
	 */
	private String nextPage;

	/**
	 * 이동 페이지 파라미터 정의
	 */
	private String nextPageParameter;

	/**
	 * 서비스 이용 시작 시간
	 */
	private String startTime;

	/**
	 * 서비스 이용 종료 시간
	 */
	private String endTime;

}
