package com.scbank.process.api.fw.common.servicetime;

import java.io.Serializable;

/**
 * 서비스 이용시간 정보 인터페이스
 */
public interface IServiceTimeInfo extends Serializable {

	/**
	 * 이용시간 관리그룹 열거형 상수를 가져온다.
	 * 
	 * @return
	 */
	ServiceTimeGroup getGroup();

	/**
	 * 이용시간 관리그룹 하위 메뉴ID/전문ID를 가져온다.
	 * 
	 * @return
	 */
	String getCode();

	/**
	 * 체크여부를 가져온다.
	 * 
	 * @return
	 */
	String getChkYn();

	/**
	 * 설명을 가져온다.
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 이동 페이지
	 * 
	 * @return
	 */
	String getNextPage();

	/**
	 * 이동 페이지 파라미터
	 * 
	 * @return
	 */
	String getNextPageParameter();

	/**
	 * 이용 시작시간
	 * 
	 * @return
	 */
	String getStartTime();

	/**
	 * 이용 종료시간
	 * 
	 * @return
	 */
	String getEndTime();
}
