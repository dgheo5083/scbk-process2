package com.scbank.process.api.svc.common.service.support;

import java.util.List;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.Ma30BbsEventMgtDao;
import com.scbank.process.api.svc.common.dao.Ma30BbsFaqMgtDao;
import com.scbank.process.api.svc.common.dao.Ma30BbsNoticeMgtDao;
import com.scbank.process.api.svc.common.dao.Ma30FnanclPrdctCmmnDao;
import com.scbank.process.api.svc.common.dao.TmbObjUsrMgtDao;
import com.scbank.process.api.svc.common.dao.dto.EventResult;
import com.scbank.process.api.svc.common.dao.dto.FaqResult;
import com.scbank.process.api.svc.common.dao.dto.NoticeParameter;
import com.scbank.process.api.svc.common.dao.dto.NoticeResult;
import com.scbank.process.api.svc.common.dao.dto.ProductResult;
import com.scbank.process.api.svc.common.dao.dto.PushJoinAlarmListResult;
import com.scbank.process.api.svc.common.dao.dto.PushJoinStatusResult;
import com.scbank.process.api.svc.common.service.support.dto.search.SupSchGetCustomerInfoRequest;
import com.scbank.process.api.svc.common.service.support.dto.search.SupSchGetCustomerInfoResponse;
import com.scbank.process.api.svc.common.service.support.dto.search.SupSchGetSearchKeywordRequest;
import com.scbank.process.api.svc.common.service.support.dto.search.SupSchGetSearchKeywordResponse;
import com.scbank.process.api.svc.shared.utils.FormatUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-메뉴검색,전체메뉴", url = "/support/search")
public class SupportSearchService {

	// 세션
	private final ISessionContextManager sessionManager;

	// DBIO
	private final Ma30FnanclPrdctCmmnDao ma30FnanclPrdctCmmnDao;
	private final Ma30BbsEventMgtDao ma30BbsEventMgtDao;
	private final Ma30BbsNoticeMgtDao ma30BbsNoticeMgtDao;
	private final Ma30BbsFaqMgtDao ma30BbsFaqMgtDao;
	private final TmbObjUsrMgtDao tmbObjUsrMgtDao;

	@ServiceEndpoint(url = "/getCustomerInfo", name = "전체메뉴 고객정보 조회")
	public SupSchGetCustomerInfoResponse getCustomerInfo(IServiceContext serviceContext, SupSchGetCustomerInfoRequest request) {
		// ASIS :: MA3MANMOR001_101S

		String finalUseDate = FormatUtils.getFrmDate(StringUtils.nvl(sessionManager.getLoginValue("FinalUseDate", String.class), "-"));
		String finalUseTime = FormatUtils.getFrmDate(StringUtils.nvl(sessionManager.getLoginValue("FinalUseTime", String.class), ""));
		String loginType = sessionManager.getLoginValue("ConnectType", String.class); // 로그인 타입 구분 1:cert, 2:id/pw, 8:BlockChainLogin 뱅크사인, 9:디지털인증
		String breezePushJoinYN = sessionManager.getLoginValue("BreezePushJoinYN", String.class);	// PUSH 가입 여부 체크

		// 2022.07.18 현대카드PJ : BC카드/현대카드 보유 여부에 대한 값 정의
		String hdCardYN = sessionManager.getGlobalValue("HDCardYN", String.class);
		String bcCardYN = sessionManager.getGlobalValue("BCCardYN", String.class);

		// 응답 객체
		SupSchGetCustomerInfoResponse response = new SupSchGetCustomerInfoResponse();
		response.setLstCnnCtnDt(finalUseDate + " " + finalUseTime); // 최근 접속시각 조회
		response.setHdCardYn(hdCardYN);
		response.setBcCardYn(bcCardYN);
		response.setLoginType(loginType);
		response.setBreezePushJoinYN(breezePushJoinYN);

		// 푸시알림 UX 개선 및 푸시알림 휴일 서비스 개발(sprint27) -2024.03.27
		// 전체 메뉴 인입 시 기기변경 체크 로직
		try {
			String userId = sessionManager.getLoginValue("UserID", String.class);

			List<PushJoinStatusResult> pushJoinYnList = tmbObjUsrMgtDao.selectPushJoinStatus(userId);
			if (!pushJoinYnList.isEmpty()) {
				if (pushJoinYnList.size() > 0) {
					for (int i = 0; i < 1; i++) {
						response.setSerno(pushJoinYnList.get(i).getSerNo()); // 고객일련번호
					}
				}
			}

			// DB조회 결과 저장
			List<PushJoinAlarmListResult> pushJoinAlarmYnList = null;
			pushJoinAlarmYnList = tmbObjUsrMgtDao.selectPushJoinAlarmList(userId);
			if (pushJoinAlarmYnList != null && !pushJoinAlarmYnList.isEmpty()) {
				for (int i = 0; i < pushJoinAlarmYnList.size(); i++) {
					if (i == 0) {
						if (pushJoinAlarmYnList.get(i).getCnfrmNo() == null
								|| "".equals(pushJoinAlarmYnList.get(i).getCnfrmNo())) {
							response.setCnfrmNo("");
						} else {
							response.setCnfrmNo(pushJoinAlarmYnList.get(i).getCnfrmNo());
						}
					}
				}
			} else {
				/*
				 * 등록된 계좌가 없을 경우 기기변경 되었다고 나오는 버그 수정
				 * 푸쉬등록 정보를 가져와 등록된 기기토큰 값을 출력한다.
				 */
				PushJoinStatusResult pushJoinAlarmYn = null;
				pushJoinAlarmYn = tmbObjUsrMgtDao.selectPushJoinAlarm(userId);
				if (pushJoinAlarmYn != null) {
					if (pushJoinAlarmYn.getCnfrmNo() == null || "".equals(pushJoinAlarmYn.getCnfrmNo())) {
						response.setCnfrmNo("");
					} else {
						response.setCnfrmNo(pushJoinAlarmYn.getCnfrmNo());
					}
				}
			}

		} catch (Exception e) {

		}

		return response;
	}

	@ServiceEndpoint(url = "/getSearchKeyword", name = "메뉴검색 검색어 검색")
	public SupSchGetSearchKeywordResponse getSearchKeyword(IServiceContext serviceContext, SupSchGetSearchKeywordRequest request) {
		// ASIS :: MA3MANNTF001_102S

		String searchWord = StringUtils.nvl(request.getSearchWord(), "");
		List<ProductResult> productList = null; // 상품 조회
		List<EventResult> eventList = null; // 이벤트 조회
		List<NoticeResult> noticeList = null; // 공지사항 조회
		List<FaqResult> faqList = null; // FAQ 조회

		String langCode = serviceContext.locale().getLanguage();
		String loctnCd = "LN1001";
		if (langCode.equals("ko")) {
			loctnCd = "LN1001";
		}else {
			loctnCd = "LN1002";
		}

		productList = ma30FnanclPrdctCmmnDao.selectProduct(searchWord, loctnCd);
		eventList = ma30BbsEventMgtDao.selectEvent(searchWord, loctnCd);

		NoticeParameter parameter = NoticeParameter.builder()
				.searchWord(searchWord)
				.loctnCd(loctnCd)
				.ctgryCd("CM1001")
				.build();
		noticeList = ma30BbsNoticeMgtDao.selectNoticeList(parameter);

		faqList = ma30BbsFaqMgtDao.selectFaq(searchWord, loctnCd);

		log.debug("productList :: {}", productList);
		log.debug("eventList :: {}", eventList);
		log.debug("noticeList :: {}", noticeList);
		log.debug("faqList :: {}", faqList);

		// 응답 객체
		SupSchGetSearchKeywordResponse response = new SupSchGetSearchKeywordResponse();
		response.setProductOut(productList);
		response.setEventOut(eventList);
		response.setNoticeOut(noticeList);
		response.setFaqOut(faqList);

		return response;
	}

}