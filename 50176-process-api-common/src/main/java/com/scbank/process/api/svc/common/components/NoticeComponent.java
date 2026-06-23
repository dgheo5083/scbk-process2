package com.scbank.process.api.svc.common.components;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.Ma30FwNoticeMgtDao;
import com.scbank.process.api.svc.common.dao.dto.PmsNoticeResult;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class NoticeComponent {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    private final Ma30FwNoticeMgtDao ma30FwNoticeMgtDao;

    public SetEmgSearchResponse getPmsNotice(String ctrgyCd) {

        SetEmgSearchResponse response = new SetEmgSearchResponse();

        String idcardNctSessionYn = StringUtils
                .parseString(sessionManager.getGlobalValue("idcardNctSessionYN", String.class), "N");
        String mobileLicNctSessionYn = StringUtils
                .parseString(sessionManager.getGlobalValue("mobileLicNctSessionYN", String.class), "N");
        String faceNctSessionYn = StringUtils
                .parseString(sessionManager.getGlobalValue("faceNctSessionYN", String.class), "N");

        if (StringUtils.isBlank(ctrgyCd)) {
            response.setNtcShowYn("N");
        } else {

            try {
                if (("Z04005".equals(ctrgyCd) && "Y".equals(idcardNctSessionYn))
                        || ("Z04006".equals(ctrgyCd) && "Y".equals(mobileLicNctSessionYn))
                        || ("Z04009".equals(ctrgyCd) && "Y".equals(faceNctSessionYn))) {
                    response.setNtcShowYn("N");
                } else {

                    // List<TokenAuthResult> tokenAuthResult =
                    // this.usimAuthMgtDao.selectTokenAuth(tokenAuthParams);

                    PmsNoticeResult pmsNoticeResult = this.ma30FwNoticeMgtDao.selectPmsNotice(ctrgyCd);

                    if (pmsNoticeResult != null) {

                        String pmsRootUrl = PropertiesUtils.getString("PMS_RESOURCE_ROOT");

                        if (StringUtils.isNotBlank(pmsNoticeResult.getAttfileIcoUrl())
                                && StringUtils.isNotBlank(pmsNoticeResult.getAttfileIcoImg())) {
                            response.setNtcImgUrl(
                                    pmsRootUrl + pmsNoticeResult.getAttfileIcoUrl()
                                            + pmsNoticeResult.getAttfileIcoImg());
                        }

                        response.setNtcPopupYn(StringUtils.parseString(pmsNoticeResult.getPopupYn(), "N"));
                        response.setNtcBtnFlg(StringUtils.parseString(pmsNoticeResult.getBtnFlg(), "C"));
                        response.setNtcBtnUrl(StringUtils.parseString(pmsNoticeResult.getBtnUrl()));
                        response.setNtcBtnName(StringUtils.parseString(pmsNoticeResult.getBtnName().trim()));
                        response.setNtcTitle(StringUtils.defaultIfBlank(pmsNoticeResult.getTitle(), "알림"));
                        response.setNtcContents(StringUtils.parseString(pmsNoticeResult.getContents()));

                        if (StringUtils.isBlank(response.getNtcContents())) {
                            response.setNtcShowYn("N");
                        } else {
                            response.setNtcShowYn("Y");
                        }

                    } else {
                        response.setNtcShowYn("N");
                    }

                    // 긴급공지 조회한경우 한세션 재조회X (Z04005:신분증촬영, Z04006:모바일신분증, Z04009: 안면인식)
                    if ("Z04005".equals(ctrgyCd)) {
                        sessionManager.setGlobalValue("idcardNctSessionYN", "Y");
                    } else if ("Z04006".equals(ctrgyCd)) {
                        sessionManager.setGlobalValue("mobileLicNctSessionYN", "Y");
                    } else if ("Z04009".equals(ctrgyCd)) {
                        sessionManager.setGlobalValue("faceNctSessionYN", "Y");
                    }

                }
            } catch (PRCServiceException e) {
                e.printStackTrace();
                response.setNtcShowYn("N");
            }

        }

        return response;

    }

}
