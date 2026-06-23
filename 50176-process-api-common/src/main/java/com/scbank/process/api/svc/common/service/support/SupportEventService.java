package com.scbank.process.api.svc.common.service.support;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.Ma30BbsEventEntDao;
import com.scbank.process.api.svc.common.dao.dto.EventApplyCountParameter;
import com.scbank.process.api.svc.common.dao.dto.EventApplyParameter;
import com.scbank.process.api.svc.common.service.support.dto.event.SupEvtApplyEventRequest;
import com.scbank.process.api.svc.common.service.support.dto.event.SupEvtApplyEventResponse;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.FormatUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-이벤트", url = "/support/event", author = "송지섭")
public class SupportEventService {

    // 세션
    private final ISessionContextManager sessionManager;

    private final Ma30BbsEventEntDao ma30BbsEventEntDao;

    /**
     * 이벤트 응모
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3MSCEVT001_102S
     */
    @ServiceEndpoint(url = "/applyEvent", name = "이벤트 응모 [ASIS:MA3MSCEVT001_102S]", author = "송지섭")
    public SupEvtApplyEventResponse findBranchAtm(IServiceContext ctx,
            SupEvtApplyEventRequest input) {
        SupEvtApplyEventResponse response = new SupEvtApplyEventResponse();
        String UserID = "";
        String hpNum = StringUtils.defaultIfEmpty(input.getHpNum(), "");
        String telNum = "";
        String USER_EMAIL = StringUtils.defaultIfEmpty(input.getEmail(), "");
        String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
        String userSex = "";
        String userBday = "";
        String userCifNo = "";

        if (sessionManager.isLogin()) {
            UserID = sessionManager.getLoginValue("UserID", String.class);
            telNum = sessionManager.getLoginValue("TeleOne", String.class)
                    + sessionManager.getLoginValue("TeleTwo", String.class)
                    + sessionManager.getLoginValue("TeleThree", String.class);
            userCifNo = sessionManager.getLoginValue("UserCifNo", String.class);
        } else {
            // 제휴이벤트는 로그인세션 없으므로 하드코딩
            UserID = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("UserID2", String.class), "FIRST999");
        }

        String EVNT_ID = StringUtils.defaultIfEmpty(input.getEvntId(), "");
        String BBS_MK_CD = StringUtils.defaultIfEmpty(input.getBbsMkCd(), "003");

        EventApplyCountParameter eventApplyCountParameter = new EventApplyCountParameter();
        eventApplyCountParameter.setBbs_mk_cd(BBS_MK_CD);
        eventApplyCountParameter.setUser_hp(hpNum);
        eventApplyCountParameter.setEvnt_id(EVNT_ID);
        eventApplyCountParameter.setUser_cifno(userCifNo);

        int existResult = ma30BbsEventEntDao.selectEventApplyCount(eventApplyCountParameter);

        if (existResult > 0) { // 이미 진행건이 있는 경우
            response.setResult("exist");
            return response;
        }

        if (perBusNo != null && perBusNo != "") {

            String genderGubun = FormatUtils.getGender(perBusNo); // 성별
            if ("1".equals(genderGubun)) {
                userSex = "M";
            } else {
                userSex = "F";
            }
            userBday = CommonBizUtils.getBirthday(perBusNo); // 생년월일

            // int tmpGender = Integer.parseInt(perBusNo.substring(6, 7));

            // String tmpBDAY = perBusNo.substring(0, 6);
            // if (tmpGender <= 2) {
            // userBday = "19" + tmpBDAY;
            // } else {
            // userBday = "20" + tmpBDAY;
            // }

            // if (tmpGender == 1 || tmpGender == 3) {
            // userSex = "M";
            // } else {
            // userSex = "F";
            // }
        }

        EventApplyParameter eventApplyParameter = new EventApplyParameter();
        eventApplyParameter.setUser_nm(StringUtils.defaultIfEmpty(input.getCustName(), "")); // PMS쪽과 키값 맞추고 진행하기.
        eventApplyParameter.setUser_sex(userSex);
        eventApplyParameter.setUser_bday(userBday);
        eventApplyParameter.setUser_id(UserID);
        eventApplyParameter.setEvnt_id(EVNT_ID);
        eventApplyParameter.setUser_hp(hpNum);
        eventApplyParameter.setUser_tel(telNum);
        eventApplyParameter.setUser_email(USER_EMAIL);
        eventApplyParameter.setUser_post(StringUtils.defaultIfEmpty(input.getZipCode(), ""));
        eventApplyParameter.setUser_addr1(StringUtils.defaultIfEmpty(input.getAddress1(), ""));
        eventApplyParameter.setUser_addr2(StringUtils.defaultIfEmpty(input.getAddress2(), ""));
        eventApplyParameter.setBbs_mk_cd(BBS_MK_CD);
        eventApplyParameter.setUser_cifno(userCifNo);

        int result = ma30BbsEventEntDao.insertEventApply(eventApplyParameter);

        response.setResult(String.valueOf(result));

        return response;
    }

}
