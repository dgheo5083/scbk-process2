package com.scbank.process.api.svc.common.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.common.dao.Ma30BbsNoticeMgtDao;
import com.scbank.process.api.svc.common.dao.dto.DetailNoticeRecordParameter;
import com.scbank.process.api.svc.common.dao.dto.DetailNoticeRecordResult;
import com.scbank.process.api.svc.common.dao.dto.ListNoticeRecordParameter;
import com.scbank.process.api.svc.common.dao.dto.ListNoticeRecordResult;
import com.scbank.process.api.svc.common.dao.dto.RecordCountParameter;
import com.scbank.process.api.svc.common.dao.dto.RecordCountResult;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscGetNoticeDetailRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscGetNoticeDetailResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscListNoticeRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscListNoticeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "공지사항 컴포넌트")
public class SupportCustomerCenterComponent {

    private final Ma30BbsNoticeMgtDao ma30BbsNoticeMgtDao;

    /**
     * 공지사항 레코드 갯수 조회
     *
     * @param getNoticeRecordCount
     * @return
     * @throws Exception
     * @description MA3TRNSTR001_302S
     */
    @ComponentOperation(name = "공지사항 레코드 갯수 조회 [ASIS:MA3CSTNTC001_103S]", author = "송지섭")
    public SupCscListNoticeResponse getNoticeRecordCount(SupCscListNoticeRequest request) {
        SupCscListNoticeResponse response = new SupCscListNoticeResponse();
        RecordCountParameter parameter = new RecordCountParameter();
        parameter.setLanguage(StringUtils.defaultIfEmpty(request.getLanguage(), "LN1001"));
        parameter.setCtgry_cd("CM1001");

        RecordCountResult result = ma30BbsNoticeMgtDao.selectRecordCount(parameter);
        response.setCountRecord(result.getCnt());

        return response;
    }

    /**
     * 공지사항 목록 조회
     *
     * @param listNoticeRecord
     * @return
     * @throws Exception
     * @description MA3CSTNTC001_102S
     */
    @ComponentOperation(name = "공지사항 목록 조회 [ASIS:MA3CSTNTC001_102S]", author = "송지섭")
    public SupCscListNoticeResponse listNoticeRecord(SupCscListNoticeRequest request) throws PRCServiceException {
        SupCscListNoticeResponse response = new SupCscListNoticeResponse();
        ListNoticeRecordParameter parameter = new ListNoticeRecordParameter();
        List<ListNoticeRecordResult> resNoticeSelect = new ArrayList<ListNoticeRecordResult>();

        String currentDate = DateUtils.getCurrentDate("yyyyMMddHH"); // 현재 날짜
        String language = StringUtils.defaultIfEmpty(request.getLanguage(), "LN1001");

        parameter.setNowdate(currentDate);
        parameter.setLanguage(language);
        parameter.setCtgry_cd("CM1001");
        parameter.setPaging(request.getPaging());
        parameter.setPageSize(request.getPageSize());

        try {
            resNoticeSelect = ma30BbsNoticeMgtDao.selectListNoticeRecord(parameter);
            if (resNoticeSelect.size() > 0) {
                response.setNoticeSelect(resNoticeSelect);
            } else {
                response.setNoticeSelect(null);
            }
        } catch (PRCServiceException e) {
            throw e;
        }

        return response;

    }

    /**
     * 공지사항 상세 조회
     *
     * @param getNoticeDetailFromTable
     * @return
     * @throws Exception
     * @description MA3CSTNTC001_202S
     */
    @ComponentOperation(name = "공지사항 상세 조회 [ASIS:MA3CSTNTC001_202S]", author = "송지섭")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    public SupCscGetNoticeDetailResponse getNoticeDetailFromTable(SupCscGetNoticeDetailRequest request)
            throws PRCServiceException {
        SupCscGetNoticeDetailResponse response = new SupCscGetNoticeDetailResponse();
        DetailNoticeRecordParameter parameter = new DetailNoticeRecordParameter();
        List<DetailNoticeRecordResult> noticeDetailSelect = new ArrayList<DetailNoticeRecordResult>();

        int seqNo = 0;
        seqNo = request.getSeqno();

        parameter.setLanguage(request.getLanguage());
        parameter.setCtgry_cd("CM1001");
        parameter.setSeqno(request.getSeqno());

        // 상세조회
        noticeDetailSelect = ma30BbsNoticeMgtDao.selectDetailNoticeRecord(parameter);

        try {
            // 조회수 카운트
            int noticeCountUpdate = ma30BbsNoticeMgtDao.updateNoticeCount(parameter);
            log.debug("################## noticeCountUpdate ############ :" + noticeCountUpdate);
        } catch (PRCServiceException e) {
            log.error("Exception updateNoticeCount: {}", e.getMessage());
        } finally {
            if (noticeDetailSelect.size() > 0) {
                for (int i = 0; i < noticeDetailSelect.size(); i++) {

                    int valSEQNO = noticeDetailSelect.get(i).getSeqno();

                    if (seqNo == valSEQNO) {
                        String valTITLE = noticeDetailSelect.get(i).getTitle();
                        String valWDATE = noticeDetailSelect.get(i).getWdate();
                        String valCONTENTS = noticeDetailSelect.get(i).getContents();
                        valCONTENTS = getReplaceText(valCONTENTS).replaceAll("\\n", "");

                        response.setContents(valCONTENTS);
                        response.setTitle(valTITLE);
                        response.setWdate(valWDATE);
                        response.setSeqno(valSEQNO);
                        response.setFxdBulltnFlg(noticeDetailSelect.get(i).getFxdBulltnFlg());

                        if ((i == 0) && (noticeDetailSelect.size() == 1)) {
                            response.setAfterSeqno("N");
                            response.setAfterTitle("N");
                            response.setAfterFxdBulltnFlg("N");
                            response.setBeforSeqno("N");
                            response.setBeforTitle("N");

                            break;
                        }
                        if (i == 0) {
                            response.setAfterSeqno("N");
                            response.setAfterTitle("N");
                            response.setAfterFxdBulltnFlg("N");
                            response.setBeforSeqno(String.valueOf(noticeDetailSelect.get(i + 1).getSeqno()));
                            response.setBeforTitle(noticeDetailSelect.get(i + 1).getTitle());

                            break;
                        }
                        if (i == noticeDetailSelect.size() - 1) {
                            response.setBeforSeqno("N");
                            response.setBeforTitle("N");
                            response.setAfterSeqno(String.valueOf(noticeDetailSelect.get(i - 1).getSeqno()));
                            response.setAfterTitle(noticeDetailSelect.get(i - 1).getTitle());
                            response.setAfterFxdBulltnFlg(noticeDetailSelect.get(i - 1).getFxdBulltnFlg());

                            break;
                        }

                        response.setAfterSeqno(String.valueOf(noticeDetailSelect.get(i - 1).getSeqno()));
                        response.setAfterTitle(noticeDetailSelect.get(i - 1).getTitle());
                        response.setAfterFxdBulltnFlg(noticeDetailSelect.get(i - 1).getFxdBulltnFlg());
                        response.setBeforSeqno(String.valueOf(noticeDetailSelect.get(i + 1).getSeqno()));
                        response.setBeforTitle(noticeDetailSelect.get(i + 1).getTitle());

                        break;
                    }

                }
            }
        }

        return response;

    }

    @ComponentOperation(name = "HTML Unescape 처리")
    public static String getReplaceText(String inString) {

        String outString = inString;
        outString = outString.replaceAll("&amp;", "&");
        outString = outString.replaceAll("&&", "\r\n");
        outString = outString.replaceAll("&lt;", "<");
        outString = outString.replaceAll("&gt;", ">");
        outString = outString.replaceAll("&qout;", "\"");
        outString = outString.replaceAll("&quot;", "\"");
        outString = outString.replaceAll("&#039;", "\'");

        return outString;
    }

}
