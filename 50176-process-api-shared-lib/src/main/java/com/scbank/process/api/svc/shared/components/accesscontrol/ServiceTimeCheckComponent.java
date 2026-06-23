package com.scbank.process.api.svc.shared.components.accesscontrol;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeManager;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.ServiceTimeCheckRequest;
import com.scbank.process.api.svc.shared.components.emergency.EmergencyIncidentManager;
import com.scbank.process.api.svc.shared.components.emergency.dto.EmergencyIncidentInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent (name = "서비스 이용시간 체크 컴포넌트", author = "sungdon.choi")
public class ServiceTimeCheckComponent {

	/**
	 * 서비스 이용시간 매니저 컴포넌트
	 */
	private final IServiceTimeManager serviceTimeManager;
	
	/**
	 * 긴급장애 매니저 컴포넌트
	 */
	private final EmergencyIncidentManager emergencyIncidentManager;
	
	@ComponentOperation(name = "메뉴/전문ID 서비스 이용시간 체크", description = "", author = "sungdon.choi")
	public void checkServiceTime(ServiceTimeCheckRequest request) {
		
		String type = StringUtils.defaultIfEmpty(request.getType(), "menu");
		String code = StringUtils.defaultIfEmpty(request.getCode(), "");
		String forceCheckCode = StringUtils.defaultIfEmpty(request.getForceCheckCode(), "");
		
		//***************************************
		//1. 시스템 점검시간 확인
		//***************************************
		this.checkSystemTime();
		
		//***************************************
		//2. 긴급장애 체크
		//***************************************
		this.checkEmgncyIncident(type, code);
		
		//***************************************
		//3. 서비스별 이용시간 체크
		//***************************************
		
		//업무에서 강제로 설정한 이용시간 체크코드가 있으면. 변경
		if (StringUtils.hasText(forceCheckCode) && "menu".equals(type)) {
			code = forceCheckCode;
		}
		
		this.checkServiceTime(type, code);
	}
	
	/**
     * 이용가능시간 체크 무시 메뉴ID 여부 확인
     * @param request
     * @return
     */
	@ComponentOperation(name = "이용가능시간 체크 무시 메뉴ID 여부 확인", author = "sungdon.choi")
    public boolean isCheckIgnoreMenuId(String menuId) {
    	if (StringUtils.isEmpty(menuId)) {
    		return false;
    	}
    	
    	List<String> checkIgnoreMenuIdList = Arrays.asList(PropertiesUtils.getString("PAGECHECK_IGNORE_MENU_ID", "").split("\\|"));
    	return checkIgnoreMenuIdList.stream().anyMatch(v -> menuId.equals(v));
    }
	
	/**
	 * 이용가능시간 체크 무시 API URI 여부 확인
	 * @param uri 현재 요청 URI
	 * @return
	 */
	@ComponentOperation(name = "이용가능시간 체크 무시 API URI 여부 확인", author = "sungdon.choi")
	public boolean isCheckIgnoreRequestUri(String uri) {
		List<String> checkIgnoreUriList = Arrays.asList(PropertiesUtils.getString("PAGECHECK_IGNORE_URI_LIST", "").split("\\|"));
		return checkIgnoreUriList.stream().anyMatch(v -> uri.equals(v));
	}
	
	/**
	 * 시스템 점검시간 체크
	 * @return 시스템 점검시간 체크 결과 Y: 통과
	 */
	private void checkSystemTime() {
		//시스템 점검 시간인지 확인
		String sysCheckTime = PropertiesUtils.getString("S_CK_TIME");
		if (StringUtils.isEmpty(sysCheckTime)) {
			return;
		}
		
		String [] timeArr = sysCheckTime.split("\\,");
		if (timeArr.length != 2) {
			return;
		}
		
		String startHHmm = timeArr[0];
		String endHHmm = timeArr[1];
		int curHHmm = Integer.parseInt(DateUtils.getCurrentDate("HHmm"));
		//시스템 점검시간 조건 체크
		if (curHHmm < Integer.parseInt(endHHmm) || curHHmm > Integer.parseInt(startHHmm)) {
			//이용시간안내 메뉴류 이동한다.
			PRCServiceException ex = new PRCServiceException("PRCCMM0027", "아쉽지만, 지금은 이용할 수 없어요.<br/>이용 가능한 시간을 확인해보세요.");
			//ex.setNextPage("MA3CSTSTI004");
			ex.setNextPage("SVCSUP003"); //TOBE 이용시간 안내페이지
			throw ex;
		}
	}
	
	/**
	 * 긴급장애 체크
	 * @param type 메뉴/전문 타입
	 * @param code 메뉴식별자 또는 전문식별자 문자열
	 */
	private void checkEmgncyIncident(String type, String code) {
		String disablType = "MN";
		if ("message".equals(type)) {
			disablType = "MS";
		} 
		
		List<EmergencyIncidentInfo> list = emergencyIncidentManager.get(disablType, code);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		
		for (EmergencyIncidentInfo info : list) {
			String startTm = info.getStartTm();
			String endTm = info.getEndTm();
			
			String today = startTm.length() == 8 ? today = DateUtils.getCurrentDate("yyyyMMdd") : DateUtils.getCurrentDate("yyyyMMddHHmmss");
			int sCom = today.compareTo(startTm);
			int eCom = today.compareTo(endTm);
			if ((sCom >= 0) && (eCom <= 0)) {
				String errorCode = StringUtils.defaultString(info.getErrCd());
				String errorMessage = "";
				if (StringUtils.isEmpty(errorCode)) {
					errorCode = "PRCCMM0053";
					errorMessage = "긴급한 장애가 발생해서 서비스를 이용할 수 없습니다.<br />잠시 후 다시 시도해 주세요.";
				}
				
				PRCServiceException e = new PRCServiceException(errorCode, errorMessage);
				e.addErrorPageParameter("DISORDER_YN", "Y");
				
				throw e;
			}
		}
	}
	
	/**
	 * 서비스별 이용시간 체크
	 * @param type 메뉴/전문
	 * @param code 서비스별 아이디
	 */
	private void checkServiceTime(String type, String code) {
		int curHHmm = Integer.parseInt(DateUtils.getCurrentDate("HHmm"));
		boolean checkHoliday = isHoliday();
		
		IServiceTimeInfo info = serviceTimeManager.getServiceTime(ServiceTimeGroup.of(type), code);
		if (info == null) {
			return;
		}
		
		String startTm = StringUtils.defaultIfEmpty(info.getStartTime(), StringUtils.EMPTY);
		String endTm = StringUtils.defaultIfEmpty(info.getEndTime(), StringUtils.EMPTY);
		String chckYn = StringUtils.defaultIfEmpty(info.getChkYn(), StringUtils.EMPTY);
		String nextPage = StringUtils.defaultIfEmpty(info.getNextPage(), StringUtils.EMPTY);
		String nextPageParameter = StringUtils.defaultIfEmpty(info.getNextPageParameter(), StringUtils.EMPTY);
		
		if (!(startTm.length() == 4 && endTm.length() == 4)) {
			return;
		}
		
		String errorCode = "";
		String errorMessage = "";
		// 휴일거래가 가능하지 않고 휴일인 경우에는 false 리턴
		if ("N".equals(chckYn) && checkHoliday) {
			errorCode = "PRCCMM0054";
			errorMessage = "아쉽지만, 지금은 이용할 수 없어요.<br/>이용 가능한 시간을 확인해보세요.";
		} else {
			if (curHHmm < Integer.parseInt(startTm) || curHHmm > Integer.parseInt(endTm)) {
				errorCode = "PRCCMM0028";
				errorMessage = "아쉽지만, 지금은 이용할 수 없어요.<br/>이용 가능한 시간을 확인해보세요.";
			}
		}
		
		if (!StringUtils.hasText(errorCode)) {
			return;
		}
		
		PRCServiceException e = new PRCServiceException(errorCode, errorMessage);
		e.setNextPage(nextPage);
		if (StringUtils.hasText(nextPageParameter)) {
			for (String parameter : nextPageParameter.split("\\&")) {
				String arr [] = parameter.split("\\=");
				String name = arr[0];
				String value = arr[1];
				e.addNextPageParameter(name, value);
			}
		}
		
		throw e;
	}
	
	/**
	 * 공휴일여부를 반환한다.
	 * @return 공휴일여부
	 */
	private boolean isHoliday() {
		return isHoliday(DateUtils.getCurrentDate());
	}
	
	/**
	 * 공휴일여부를 반환한다.
	 * @param date yyyyMMdd 형식의 날짜
	 * @return 공휴일여부
	 */
	private boolean isHoliday(String date) {
		IHolidayManager holidayManager = RuntimeContext.getBean(IHolidayManager.class);
		return holidayManager == null ? false : holidayManager.isHoliday(date);
	}
}
