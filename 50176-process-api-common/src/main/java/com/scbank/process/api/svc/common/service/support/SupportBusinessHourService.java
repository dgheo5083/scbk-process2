package com.scbank.process.api.svc.common.service.support;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H97700Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H97700Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.common.dao.Ma30BbsEventMgtDao;
import com.scbank.process.api.svc.common.dao.dto.BusinessHourParameter;
import com.scbank.process.api.svc.common.dao.dto.BusinessHourResult;
import com.scbank.process.api.svc.common.mapper.SupportBusinessHourMapper;
import com.scbank.process.api.svc.common.service.support.dto.businesshour.SupBzhGetBusinessHourRequest;
import com.scbank.process.api.svc.common.service.support.dto.businesshour.SupBzhGetBusinessHourResponse;
import com.scbank.process.api.svc.common.service.support.dto.businesshour.SupBzhGetWorkingDayRequest;
import com.scbank.process.api.svc.common.service.support.dto.businesshour.SupBzhGetWorkingDayResponse;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-이용시간안내", url = "/support/businessHour", author = "김기주")
public class SupportBusinessHourService {

    // 전문
    private final HostClient hostClient;

    // Mapper
    private final SupportBusinessHourMapper supportBusinessHourMapper;

    // dao
    private final Ma30BbsEventMgtDao ma30BbsEventMgtDao;

    /**
     * 영업일 조회
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3ACTADS001_000S
     */
    @ServiceEndpoint(url = "/getWorkingDay", name = "영업일 조회 [ASIS:MA3ACTADS001_000S]", author = "김기주")
    public SupBzhGetWorkingDayResponse getWorkingDay(IServiceContext ctx,
            SupBzhGetWorkingDayRequest input) throws PRCServiceException {

        log.debug("#####################_LIH_MA3ACADSS001_000S_inputData ::" + input + "::");

        SupBzhGetWorkingDayResponse output = new SupBzhGetWorkingDayResponse();
        CbIbk01H97700Req scbkSendData = new CbIbk01H97700Req();

        OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H977");

        String yiyoguhs = StringUtils.defaultIfEmpty(input.getYiYOGUHs(), "+");
        String yiyogu = StringUtils.defaultIfEmpty(input.getYiYOGU(), "01"); // 기본 익영업일 지정
        String yisilja = StringUtils.defaultIfEmpty(input.getYiSILJA(), DateUtils.getCurrentDate());

        hostRequestOptions.setImsTranCd("TI1IBK01");
        hostRequestOptions.setInClassCd("H977");
        hostRequestOptions.setSvcCd("977");
        hostRequestOptions.setCaptureSystem("OLTP");

        scbkSendData.setYIUSID(SessionUtils.getSessionValue("UserID", String.class)); // 이용자번호
        scbkSendData.setYIPASS(SessionUtils.getSessionValue("TSPassword", String.class)); // 통신비밀번호
        scbkSendData.setYIYOGUHs(yiyoguhs); // 요구일수BIT (+, -)
        scbkSendData.setYIYOGU(yiyogu); // 요구일수((+,-) 숫자 2자리
        scbkSendData.setYISILJA(yisilja); // 시작일자

        OltpResponse<CbIbk01H97700Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, scbkSendData,
                CbIbk01H97700Res.class);

        output = this.supportBusinessHourMapper.toSupBzhGetWorkingDayResponse(hostResponse.getResponse());

        log.debug("#####################_LIH_MA3ACADSS001_000S_outputData ::" + output + "::");
        return output;
    }

    /**
     * 이용시간 안내 조회
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3CSTSTI004_101S
     */
    @ServiceEndpoint(url = "/getBusinessHour", name = "이용시간 안내 조회 [ASIS:MA3CSTSTI004_101S]", author = "송지섭")
    public SupBzhGetBusinessHourResponse getBusinessHour(IServiceContext ctx,
            SupBzhGetBusinessHourRequest input) {

        SupBzhGetBusinessHourResponse response = new SupBzhGetBusinessHourResponse();

        String LOCTN_CD = "ko".equalsIgnoreCase(ctx.locale().getLanguage()) ? "LN1001" : "LN1002"; // 국문:LN1001,
                                                                                                   // 영문:LN1002
        // 이용시간안내 PMS연동 부분 추가
        /*
         * 카테고리코드 정의
         * SHM001(서비스전환시간), SHM002(금융상품), SHM003(이체/납부), SHM004(계좌), SHM005(대출),
         * SHM006(펀드/보험), SHM007(외환)
         * SHM008(현대카드), SHM009(BC카드), SHM010(편리한서비스), SHM011(혜택), SHM012(인증/보안),
         * SHM013(고객센터)
         */
        BusinessHourParameter parameter = new BusinessHourParameter();
        parameter.setLanguage(LOCTN_CD);
        List<BusinessHourResult> list = ma30BbsEventMgtDao.selectBusinessHour(parameter);
        List<BusinessHourResult> deDuplicatedList = new ArrayList<>();
        if (list != null) {
            // 카테고리별로 중복이 있는경우 먼저 뽑힌 데이터로 사용
            Set<String> seenCategories = new HashSet<>();
            for (BusinessHourResult item : list) {
                String cgtryCd = item.getCgtryCd();
                // 이미 있으면 false
                if (seenCategories.add(cgtryCd)) {
                    item.setContents(getReplaceText(item.getContents()));
                    item.setCtgryNm(item.getCtgryNm());
                    deDuplicatedList.add(item);
                }
            }
            deDuplicatedList.sort(Comparator.comparing(BusinessHourResult::getCgtryCd));
        }
        response.setBusinessHourResultList(deDuplicatedList);

        return response;

    }

    /* HTML Unescape 처리 */
    private String getReplaceText(String inString) {

        String outString = inString;
        outString = outString.replaceAll("&amp;", "&");
        outString = outString.replaceAll("&&", "\r\n");
        outString = outString.replaceAll("&lt;", "<");
        outString = outString.replaceAll("&gt;", ">");
        outString = outString.replaceAll("&quot;", "\"");
        outString = outString.replaceAll("&#039;", "\'");

        return outString;
    }

}
