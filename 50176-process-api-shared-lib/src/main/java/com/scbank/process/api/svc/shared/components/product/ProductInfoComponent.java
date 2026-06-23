package com.scbank.process.api.svc.shared.components.product;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.product.dao.Ma30FnanclPrdctControlDao;
import com.scbank.process.api.svc.shared.components.product.dao.dto.PrdctAvailableInfoInqiryResult;
import com.scbank.process.api.svc.shared.components.product.dto.PmsProductAvailableInfoRequest;
import com.scbank.process.api.svc.shared.components.product.dto.PmsProductAvailableInfoResponse;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class ProductInfoComponent {
	
	private final IpinsideComponent ipinsideComponent;
	private final Ma30FnanclPrdctControlDao ma30FnanclPrdctControlDao;
	private final IHolidayManager iHolidayManager;
	
	@ComponentOperation(name = "상품별 이용가능 여부를 체크한다.(PMS 상품정보 연동) [MA3CMMBIZ031_100S]")
	public PmsProductAvailableInfoResponse getPmsProductAvailableInfo(PmsProductAvailableInfoRequest request) {
		PmsProductAvailableInfoResponse response = new PmsProductAvailableInfoResponse();
		
		String flagIsAbroadYn = StringUtils.defaultIfEmpty(request.getFlagIsAbroadYn(),SessionUtils.getSessionValue("flagIsAbroadYN"));
		String prdctCd = StringUtils.defaultString(request.getTargetPrdctCd());
		String errorCode = StringUtils.defaultString(request.getErrorCode());
		String nextPage = StringUtils.defaultString(request.getNextPage());
		String nextParam = StringUtils.defaultString(request.getNextParam());
		String throwExceptionYn = StringUtils.defaultIfEmpty(request.getThrowExceptionYn(),"Y");
		
		String lowAgeYN = "N";						//나이제한 여부
		String foreignerYN = "N";					//외국인 여부
		String resultAvailableDay = "";				//이용가능 날짜(365일, 평일, 휴일/공휴일)
		String resultAvailableTime = "";			//이용가능 시간(24시간, 직접입력)
		String resultAvailableAge = "";				//이용가능 나이(19세 이상, 17세 이상, 14세 이상, 제한없음)
		String resultAvailableAbroadIp = "";		//해외IP 가능여부(해외IP차단, 해외IP허용)
		String checkAvailableDay = "";				//이용가능 날짜 체크 결과(Y:정상, N:불가, "":데이터미존재)
		String checkAvailableTime = "";				//이용가능 시간 체크 결과(Y:정상, N:불가, "":데이터미존재)
		String checkAvailableAge = "";				//이용가능 나이 체크 결과(Y:정상, N:불가, "":데이터미존재)
		String checkAvailableAbroadIp = "";			//해외IP 허용여부 체크 결과(Y:정상, N:불가, "":데이터미존재)
		String checkDayExceptionMessage = "";		//이용가능 날짜 체크 exception 메시지
		String checkTimeExceptionMessage = "";		//이용가능 시간 체크 exception 메시지
		String checkAgeExceptionMessage = "";		//이용가능 나이 체크 exception 메시지
		String checkAbroadExceptionMessage = "";	//해외IP 허용여부 체크 exception 메시지
		
		String userAge = ""; // 사용자 나이
		
		if(StringUtils.isEmpty(flagIsAbroadYn)) {
			//해외접속여부 체크
			flagIsAbroadYn = ipinsideComponent.getAbroadYn();
		}
		
		if(StringUtils.isNotEmpty(prdctCd)) {
			PrdctAvailableInfoInqiryResult prdctAbleInfoRes = ma30FnanclPrdctControlDao.selectPrdctAvailableInfo(prdctCd);
			
			if(prdctAbleInfoRes != null) {
				resultAvailableDay = StringUtils.defaultString(prdctAbleInfoRes.getUseDateFlg());
				resultAvailableTime = StringUtils.defaultString(prdctAbleInfoRes.getUseTimeFlg());
				resultAvailableAge = StringUtils.defaultString(prdctAbleInfoRes.getUseAgeFlg());
				resultAvailableAbroadIp = StringUtils.defaultString(prdctAbleInfoRes.getUseAbroadFlg());
				
				if(StringUtils.isNotEmpty(resultAvailableDay) || StringUtils.isNotEmpty(resultAvailableTime)
						|| StringUtils.isNotEmpty(resultAvailableAge) || StringUtils.isNotEmpty(resultAvailableAbroadIp)) {
					boolean isHoliday = iHolidayManager.isHoliday(DateUtils.getCurrentDate());
					String curHhMm = DateUtils.getCurrentDate(DateUtils.HHMM);
					//18nMessageManager i18nManager = I18nMessageManager.getInstance();
					checkAvailableDay = StringUtils.isNotEmpty(resultAvailableDay) ? "Y" : "";
					checkAvailableTime = StringUtils.isNotEmpty(resultAvailableTime) ? "Y" : "";
					
					//이용가능 날짜 체크
					if(StringUtils.isNotEmpty(resultAvailableDay) && "PC3002".equals(resultAvailableDay) && isHoliday) {	//평일(PC3002)만 허용인데, 현재 휴일인 경우
						checkAvailableDay = "N";		//이용가능 날짜 체크 결과 업데이트
						checkDayExceptionMessage = "아쉽지만, 지금은 이용할 수 없어요.<br/>이용 가능한 시간을 확인해보세요.";
						//Exception 발생 시 throw할지 여부
						if("Y".equals(throwExceptionYn)) {
							PRCServiceException prcExcception = new PRCServiceException(StringUtils.defaultIfEmpty(errorCode, "MA3CMM0054"));
							
							prcExcception.setNextPage(StringUtils.defaultIfEmpty(nextPage, "MA3CSTSTI004"));
							prcExcception.setErrorMessage(checkDayExceptionMessage);
							/* prcExcception.setNextPageParameters(nextParam); */
							
							throw prcExcception;
						}
					}
					
					String startTime = StringUtils.defaultString(prdctAbleInfoRes.getUseTimeStart());
					String endTime = StringUtils.defaultString(prdctAbleInfoRes.getUseTimeEnd());
					
					//이용가능시간 체크
					if(StringUtils.isNotEmpty(resultAvailableTime) && "PC4002".equals(resultAvailableTime)) {	//직접입력(PC4002)인 경우
						if(StringUtils.isNotEmpty(curHhMm) && StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
							if(Integer.parseInt(curHhMm) < Integer.parseInt(startTime) || Integer.parseInt(curHhMm) > Integer.parseInt(endTime)) {
								checkAvailableTime = "N";		//이용가능 시간 체크 결과 업데이트
								checkTimeExceptionMessage = "아쉽지만, 지금은 이용할 수 없어요.<br/>이용 가능한 시간을 확인해보세요.";
								//Exception 발생 시 throw할지 여부
								if("Y".equals(throwExceptionYn)) {
									PRCServiceException prcExcception = new PRCServiceException(StringUtils.defaultIfEmpty(errorCode, "MA3CMM0028"));
									prcExcception.setNextPage(StringUtils.defaultIfEmpty(nextPage, "MA3CSTSTI004"));
									prcExcception.setErrorMessage(checkTimeExceptionMessage);
									/* prcExcception.setNextPageParameters(nextParam); */
									
									throw prcExcception;
								}
							}
						}
					}
					
					String perBusNo = StringUtils.defaultIfEmpty(request.getPerBusNo(), SessionUtils.getSessionValue("PerBusNo"));
					
					//이용가능 나이 체크
					if(StringUtils.isNotEmpty(resultAvailableAge) && StringUtils.isNotEmpty(perBusNo)) {
						checkAvailableAge = "Y";
						
						String checkAge = "";
						switch(resultAvailableAge) {
							case "PC5001": checkAge = "19";	break;	//19세 이상
							case "PC5002": checkAge = "17";	break;	//17세 이상
							case "PC5003": checkAge = "14";	break;	//14세 이상
							case "PC5004": checkAge = "0";	break;	//제한없음
						}
						
						Map<String, String> checkAgeInfo = BankingBizUtils.getForeignerAndUnderAge(perBusNo, checkAge);
						lowAgeYN = checkAgeInfo.get("isLowAge");
						foreignerYN = checkAgeInfo.get("isForeigner");
						userAge = checkAgeInfo.get("userAge");
						
						if("Y".equals(lowAgeYN)) {
							checkAvailableAge = "N";		//이용가능 나이 체크 결과 업데이트
							Object[] messageArguments = {checkAge};
							
							//TODO 오류메시지 추가
							checkAgeExceptionMessage = "";
//							checkAgeExceptionMessage = MessageFormat.format(i18nManager.getContentsMessage(ctx, "MA3CMM011027"), messageArguments);
							//Exception 발생 시 throw할지 여부
							if("Y".equals(throwExceptionYn)) {
								//문구 추가 필요 - 이용가능 나이제한에 해당하는 경우
								PRCServiceException prcExcception = new PRCServiceException(StringUtils.defaultIfEmpty(errorCode, "MA3CMM011027"));
								prcExcception.setNextPage(StringUtils.defaultIfEmpty(nextPage, "MA3PRDALL001"));
								prcExcception.setErrorMessage(checkAgeExceptionMessage);
								/* prcExcception.setNextPageParameters(nextParam); */
								
								throw prcExcception;
							}
						}
					}
					
					//해외IP 허용여부 체크
					//해외IP 가능여부(PC6001:해외IP차단, PC6002:해외IP허용)
					if(StringUtils.isNotEmpty(resultAvailableAbroadIp)) {
						checkAvailableAbroadIp = "Y";
						if("PC6001".equals(resultAvailableAbroadIp) && "Y".equals(flagIsAbroadYn)) {	//해외IP차단(PC6001)인데, 해외IP접속인 경우
							checkAvailableAbroadIp = "N";		//해외IP 허용여부 체크 결과 업데이트
							
							//TODO 오류메시지 추가
							checkAbroadExceptionMessage = "해외 IP를 통해서는 해당 상품을 가입할 수 없습니다.";
//							checkAbroadExceptionMessage = i18nManager.getContentsMessage(ctx, "MA3CMM011028");
							//Exception 발생 시 throw할지 여부
							if("Y".equals(throwExceptionYn)) {
								PRCServiceException prcExcception = new PRCServiceException(StringUtils.defaultIfEmpty(errorCode, "MA3CMM011028"));
								prcExcception.setNextPage(StringUtils.defaultIfEmpty(nextPage, "MA3PRDALL001"));
								prcExcception.setErrorMessage(checkAgeExceptionMessage);
								/* prcExcception.setNextPageParameters(nextParam); */
								
								throw prcExcception;
							}
						}
					}
				}
			}
		}
		
		//이용가능일자/시간 체크가 되지 않았으면 기존 거래 가능시간 체크방식을 사용(PMS정보를 가져오지 못한 경우 - 상품코드 미전달, PMS 데이터 미존재)
//		if((StringUtils.isEmpty(resultAvailableDay) && StringUtils.isEmpty(resultAvailableTime))) {
//			UsetimeManager useTimeManager = UsetimeManager.getInstance();
//			String menuId = PRCSharedUtils.getMenuId();
//			String checkTimeCode = menuId;
//			String forceCheckCode = StringUtils.nvl(request.getForceCheckCode());
//			
//			if(StringUtils.isNotEmpty(forceCheckCode)) {
//				String forceCheckCode_CHK = forceCheckCode;
//				//forceCheckCode에 _CHK가 붙어있지 않은 경우 붙인 후 usetime-config에 해당 ID 이용시간 정보 존재여부 체크
//				if(!forceCheckCode.endsWith("_CHK")) {
//					forceCheckCode_CHK = String.join("_", forceCheckCode, "CHK");
//				}
//				UsetimeItem usetimeItem = useTimeManager.getUsetimeItem(UsetimeGroup.getGroup("menu"), forceCheckCode_CHK);
//				if (usetimeItem != null) {
//					checkTimeCode = forceCheckCode_CHK;
//				} else {
//					checkTimeCode = forceCheckCode;
//				}
//			} else {		//forceCheckCode가 없는 경우는 현재 메뉴ID로 체크
//				String menuId_CHK = String.join("_", menuId, "CHK");
//				UsetimeItem usetimeItem = useTimeManager.getUsetimeItem(UsetimeGroup.getGroup("menu"), menuId_CHK);
//				//해당 ID 이용시간 정보 존재여부 체크
//				if (usetimeItem != null) {
//					checkTimeCode = menuId_CHK;
//				} else {
//					checkTimeCode = menuId;
//				}
//			}
//			
//			try {
//				SCBKBizUtil.isCheckTranTime(checkTimeCode, "menu", "");
//			} catch (SCBKAppException e) {
//				throw e;
//			} catch (Throwable t) {
//				SCBKLogger.error("@@@ LGS MA3CMMBIZ031_100S SCBKBizUtil.isCheckTranTime error occured >> sessionId["+ctx.getSessionId()+"]uri["+ctx.getRequestURI()+"]checkTimeCode["+checkTimeCode+"]prdctCd["+prdctCd+"]forceCheckCode["+forceCheckCode+"]");
//				throw new SCBKAppException("MA3CMM0030", "처리중 오류가 발생하였습니다.", t);
//			}
//		}
		
		response.setAvailableDay(resultAvailableDay);
		response.setAvailableTime(resultAvailableTime);
		response.setAvailableAge(resultAvailableAge);
		response.setAvailableAbroadIp(resultAvailableAbroadIp);
		response.setCheckDay(checkAvailableDay);
		response.setCheckTime(checkAvailableTime);
		response.setCheckAbroadIP(checkAvailableAbroadIp);
		response.setCheckAge(checkAvailableAge);
		response.setCheckAbroadIP(checkAvailableAbroadIp);
		response.setFlagIsAbroadYn(flagIsAbroadYn);
		response.setIsLowAge(lowAgeYN);
		response.setIsForeigner(foreignerYN);

		if("N".equals(throwExceptionYn)) {
			response.setDayErrorMessage(checkDayExceptionMessage);
			response.setTimeErrorMessage(checkTimeExceptionMessage);
			response.setAgeErrorMessage(checkAgeExceptionMessage);
			response.setAbroadErrorMessage(checkAbroadExceptionMessage);
		}
		
		response.setUserAge(userAge);
		
		return response;
	}
}
