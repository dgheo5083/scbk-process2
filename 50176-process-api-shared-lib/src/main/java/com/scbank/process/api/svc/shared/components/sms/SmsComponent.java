package com.scbank.process.api.svc.shared.components.sms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.edmi.dto.oltp.CbSms01S00200Req;
import com.scbank.process.api.edmi.dto.oltp.CbSms01S00200Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.integration.HostClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class SmsComponent {

    private String smsServerIp;
    private int smsServerPort;
    private final HostClient hostClient;

    @PostConstruct
    public void init() {
        this.smsServerIp = PropertiesUtils.getString("sms.server.ip");
        this.smsServerPort = Integer.parseInt(PropertiesUtils.getString("sms.server.port"));
    }

    @ComponentOperation(name = "SMS 발송")
    public String sendMain(SmsRequest request) throws PRCServiceException {

        Socket sendSocket = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        BufferedInputStream bis = null;

        String smsResponse;
        String flag = null;

        String[] smsFieldData = new String[22];
        setSMSFieldData(request, smsFieldData); // SMS Packet 의 각 필드를 setting 하는 메소드

        try {
            sendSocket = new Socket(this.smsServerIp, this.smsServerPort); // 클라이언트측 생성

            bos = new BufferedOutputStream(sendSocket.getOutputStream());
            dos = new DataOutputStream(bos);
            bis = new BufferedInputStream(sendSocket.getInputStream());

            int[] smsFieldLength = setSMSFieldInfo();

            // ///서버에 SMS request 를 send..../
            sendSMSRequest(dos, smsFieldData, smsFieldLength);

            // ////서버로 부터 응답을 받아옴 /////
            smsResponse = reciveSMSResponse(bis);
        } catch (PRCServiceException e) {
            throw e;
        } catch (Throwable e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        } finally {
            try {
                if (dos != null)
                    dos.close();
                if (bos != null)
                    bos.close();
                if (bis != null)
                    bis.close();
                if (sendSocket != null)
                    sendSocket.close();
            } catch (Exception e) {
                throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
            }
        }

        flag = smsResponse.substring(91, 92);
        return flag;
    }

    @ComponentOperation(name = "SMS URL 발송")
    public void sendURLMain(SmsRequest request) throws PRCServiceException {

        Socket sendSocket = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        BufferedInputStream bis = null;

        String[] urlFieldData = new String[25];
        setURLFieldData(request, urlFieldData); // SMS Packet 의 각 필드를 setting 하는 메소드

        try {
            // sendSocket = new Socket( "194.168.1.10", 9090 ) ; //클라이언트측 소캣 생성
            sendSocket = new Socket(this.smsServerIp, this.smsServerPort); // 클라이언트측 소캣 생성

            /******** 스트림 생성 ***********************/
            bos = new BufferedOutputStream(sendSocket.getOutputStream());
            dos = new DataOutputStream(bos);
            bis = new BufferedInputStream(sendSocket.getInputStream());

            int[] urlFieldLength = setURLFieldInfo();

            /**** 서버에 SMS request 를 send.... ****/
            sendURLRequest(dos, urlFieldData, urlFieldLength);

            /**** 서버로 부터 응답을 받아옴 ********/
            reciveURLResponse(bis);
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        } finally {
            try {
                if (dos != null)
                    dos.close();
                if (bos != null)
                    bos.close();
                if (bis != null)
                    bis.close();
                if (sendSocket != null)
                    sendSocket.close();
            } catch (Exception e) {
                throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
            }
        }
    }

    /**
     * SMS Packet 각 필드의 Length를 Setting
     */
    private int[] setSMSFieldInfo() {

        int[] smsFieldLength = new int[22];

        smsFieldLength[0] = 1; // char
        smsFieldLength[1] = 1; // char
        smsFieldLength[2] = 10; // char[10]
        smsFieldLength[3] = 8; // char[8]
        smsFieldLength[4] = 30; // char[30]
        smsFieldLength[5] = 16; // char[16]
        smsFieldLength[6] = 12; // char[12]
        smsFieldLength[7] = 16; // char[16]
        smsFieldLength[8] = 1; // char
        smsFieldLength[9] = 4; // char[4]
        smsFieldLength[10] = 4; // char[4]
        smsFieldLength[11] = 4; // char[4]
        smsFieldLength[12] = 120; // char[120]
        smsFieldLength[13] = 10; // char[10]
        smsFieldLength[14] = 8; // char[8]
        smsFieldLength[15] = 3; // char[3]
        smsFieldLength[16] = 8; // long
        smsFieldLength[17] = 4; // char[4]
        smsFieldLength[18] = 4; // char[4]
        smsFieldLength[19] = 4; // char[4]
        smsFieldLength[20] = 32; // char[32]
        smsFieldLength[21] = 32; // char[32]

        return smsFieldLength;
    }

    /**
     * SMS Packet의 각 필드를 Setting
     *
     * @param request
     * @param smsFieldData
     */
    private void setSMSFieldData(SmsRequest request, String[] smsFieldData) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyMMdd");
        SimpleDateFormat dfTime = new SimpleDateFormat("HHmmss");
        java.util.Date currTime = new java.util.Date(System.currentTimeMillis() + 2000);

        smsFieldData[0] = "B"; // Command
        smsFieldData[1] = "A"; // Type
        smsFieldData[2] = dfDate.format(currTime); // Date
        smsFieldData[3] = dfTime.format(currTime); // Time
        smsFieldData[4] = request.getUserCode();
        smsFieldData[5] = request.getUserName();
        smsFieldData[6] = request.getDeptCode();
        smsFieldData[7] = request.getDeptName();
        smsFieldData[8] = ""; // Status

        // CallPhone처리 '011-123-4567' --> '011', '123', 4567'로 분리
        smsFieldData[9] = request.getCallPhone1();
        smsFieldData[10] = request.getCallPhone2();
        smsFieldData[11] = request.getCallPhone3();

        smsFieldData[12] = request.getCallMessage();
        smsFieldData[13] = request.getRateDate();
        smsFieldData[14] = request.getRateTime();
        smsFieldData[15] = ""; // Dummy
        smsFieldData[16] = request.getMember();

        // ReqPhone처리 '011-123-4567' --> '011', '123', 4567'로 분리
        smsFieldData[17] = request.getReqPhone1();
        smsFieldData[18] = request.getReqPhone2();
        smsFieldData[19] = request.getReqPhone3();
        smsFieldData[20] = request.getCallName();
        smsFieldData[21] = ""; // reserved
    }

    /**
     * SureM.COM서버에 SMS request 를 send.....
     *
     * @param dos
     * @param smsFieldData
     * @param smsFieldLength
     * @throws PRCServiceException
     */
    private void sendSMSRequest(DataOutputStream dos, String[] smsFieldData, int[] smsFieldLength)
            throws PRCServiceException {

        StringBuffer strBuff = new StringBuffer();
        byte[] byteBuff = null;

        try {
            for (int inx = 0; inx < 16; inx++) {
                appendToBuffer(strBuff, smsFieldData[inx], smsFieldLength[inx]);
            }
            log.debug("sendSMSRequest() strBuff1 = [" + strBuff.toString() + "]");

            byteBuff = strBuff.toString().getBytes("EUC-KR");
            dos.write(byteBuff, 0, byteBuff.length);

            strBuff.delete(0, strBuff.length());
            dos.writeInt(Integer.parseInt(smsFieldData[16]));

            for (int inx = 17; inx < 22; inx++) {
                appendToBuffer(strBuff, smsFieldData[inx], smsFieldLength[inx]);
            }
            log.debug("sendSMSRequest() strBuff2 = [" + strBuff.toString() + "]");

            byteBuff = strBuff.toString().getBytes("EUC-KR");
            dos.write(byteBuff, 0, byteBuff.length);

            dos.flush();
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        }
    }

    /**
     * SureM.COM 서버로 부터 SMS response를 receive...
     *
     * @param bis
     * @return
     * @throws PRCServiceException
     */
    private String reciveSMSResponse(BufferedInputStream bis) throws PRCServiceException {
        byte[] byteBuff = new byte[1024];
        String strReceivedMsg = null;

        try {
            int iReadCount = bis.read(byteBuff);
            if (iReadCount > 0) {
                strReceivedMsg = new String(byteBuff, 0, iReadCount, "EUC-KR");
            }

            log.debug("reciveSMSResponse() 응답MSG : " + strReceivedMsg);
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        }

        return strReceivedMsg;
    }

    /**
     * StringBuffer에 각 필드의 데이타를 append
     *
     * @param strBuff
     * @param fieldData
     * @param fieldLength
     * @throws PRCServiceException
     */
    private void appendToBuffer(StringBuffer strBuff, String fieldData, int fieldLength) throws PRCServiceException {
        try {
            int fieldDataLength = fieldData.getBytes("EUC-KR").length;
            strBuff.append(fieldData);

            for (int inx = fieldLength - fieldDataLength; inx > 0; inx--) {
                strBuff.append('\0');
            }
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        }
    }

    /**
     * SMS Packet 각 필드의 Length를 Setting
     *
     * @return
     */
    private int[] setURLFieldInfo() {

        int[] urlFieldLength = new int[25];

        urlFieldLength[0] = 1; // char
        urlFieldLength[1] = 1; // char
        urlFieldLength[2] = 10; // char[10]
        urlFieldLength[3] = 8; // char[8]
        urlFieldLength[4] = 30; // char[30]
        urlFieldLength[5] = 16; // char[16]
        urlFieldLength[6] = 12; // char[12]
        urlFieldLength[7] = 16; // char[16]
        urlFieldLength[8] = 1; // char
        urlFieldLength[9] = 4; // char[4]
        urlFieldLength[10] = 4; // char[4]
        urlFieldLength[11] = 4; // char[4]
        urlFieldLength[12] = 80; // char[80]
        urlFieldLength[13] = 62; // char[62]
        urlFieldLength[14] = 10; // char[10]
        urlFieldLength[15] = 8; // char[8]
        urlFieldLength[16] = 4; // char[4]
        urlFieldLength[17] = 4; // char[4]
        urlFieldLength[18] = 4; // char[4]
        urlFieldLength[19] = 32; // char[32]
        urlFieldLength[20] = 1; // char
        urlFieldLength[21] = 4; // char[4]
        urlFieldLength[22] = 4; // char[4]
        urlFieldLength[23] = 4; // char[4]
        urlFieldLength[24] = 4; // char[4]

        return urlFieldLength;
    }

    /**
     * SMS Packet의 각 필드를 Setting
     *
     * @param request
     * @param urlFieldData
     */
    private void setURLFieldData(SmsRequest request, String[] urlFieldData) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyMMdd");
        SimpleDateFormat dfTime = new SimpleDateFormat("HHmmss");
        java.util.Date currTime = new java.util.Date(System.currentTimeMillis() + 2000);

        urlFieldData[0] = "I"; // Command
        urlFieldData[1] = "C"; // Type
        urlFieldData[2] = dfDate.format(currTime); // Date
        urlFieldData[3] = dfTime.format(currTime); // Time
        urlFieldData[4] = request.getUserCode();
        urlFieldData[5] = request.getUserName();
        urlFieldData[6] = request.getDeptCode();
        urlFieldData[7] = request.getDeptName();
        urlFieldData[8] = ""; // Status

        // CallPhone처리 '011-123-4567' --> '011', '123', 4567'로 분리
        urlFieldData[9] = request.getCallPhone1();
        urlFieldData[10] = request.getCallPhone2();
        urlFieldData[11] = request.getCallPhone3();

        urlFieldData[12] = request.getCallMessage();
        urlFieldData[13] = request.getCallUrl();
        urlFieldData[14] = request.getRateDate();
        urlFieldData[15] = request.getRateTime();

        // ReqPhone처리 '011-123-4567' --> '011', '123', 4567'로 분리
        urlFieldData[16] = request.getReqPhone1();
        urlFieldData[17] = request.getReqPhone2();
        urlFieldData[18] = request.getReqPhone3();
        urlFieldData[19] = request.getCallName();
        urlFieldData[20] = ""; // Dummy
        urlFieldData[21] = request.getMember();
        urlFieldData[22] = ""; // TotalPrice
        urlFieldData[23] = ""; // CallPrice
        urlFieldData[24] = ""; // reserved
    }

    /**
     * SureM.COM서버에 URL request 를 send.....
     *
     * @param dos
     * @param urlFieldData
     * @param urlFieldLength
     * @throws PRCServiceException
     */
    private void sendURLRequest(DataOutputStream dos, String[] urlFieldData, int[] urlFieldLength)
            throws PRCServiceException {

        StringBuffer strBuff = new StringBuffer();
        byte[] byteBuff = null;

        try {
            for (int inx = 0; inx < 25; inx++) {
                appendToBuffer(strBuff, urlFieldData[inx], urlFieldLength[inx]);
            }
            log.debug("sendURLRequest() strBuff1 = [" + strBuff.toString() + "]");

            byteBuff = strBuff.toString().getBytes("EUC-KR");
            dos.write(byteBuff, 0, byteBuff.length);

            strBuff.delete(0, strBuff.length());
            dos.writeInt(Integer.parseInt(urlFieldData[21]));

            appendToBuffer(strBuff, urlFieldData[24], urlFieldLength[24]);
            log.debug("sendURLRequest() strBuff2 = [" + strBuff.toString() + "]");

            byteBuff = strBuff.toString().getBytes("EUC-KR");
            dos.write(byteBuff, 0, byteBuff.length);

            dos.flush();
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        }
    }

    /**
     * SureM.COM 서버로 부터 SMS response를 receive...
     *
     * @param bis
     * @return
     * @throws PRCServiceException
     */
    private String reciveURLResponse(BufferedInputStream bis) throws PRCServiceException {
        byte[] byteBuff = new byte[1024];
        String strReceivedMsg = null;

        try {
            int iReadCount = bis.read(byteBuff);
            if (iReadCount > 0) {
                strReceivedMsg = new String(byteBuff, 0, iReadCount, "EUC-KR");
            }

            log.debug("reciveURLResponse() 응답MSG : " + strReceivedMsg);
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0062", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(SMS발송)");
        }

        return strReceivedMsg;
    }

    @ComponentOperation(name = "발급완료 SMS발송", description = "발급완료 SMS발송", author = "이완주")
    public boolean sendCompleteSMS(String userID, String tsPassword, String regNo, String smsMsg) {
        try {
            OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_SMS01_S002");
            hostCfg.setImsTranCd("TI1SMS01");
            hostCfg.setInClassCd("S002");
            hostCfg.setSvcCd("911");
            hostCfg.setJobTp("GY");

            CbSms01S00200Req cbSms01S00200Req = new CbSms01S00200Req();
            cbSms01S00200Req.setUserID(userID); // 사용자아이디
            cbSms01S00200Req.setCustomGB("IB9"); // 전문종별 IB1IB비정형
            cbSms01S00200Req.setTransGB("001"); // 거래구분 001공인인증서(재발급) 002고객정보관리변경 003고객휴대폰번호변경 004고객이메일변경 005이체한도변경(감액)
                                                // 006출금계좌 등록 007출금계좌 해지
            cbSms01S00200Req.setJuminNumgb(""); // 주민번호구분. "":개인,개인사업자 2:사업자
            cbSms01S00200Req.setPerBusNo(regNo); // 실명번호
            cbSms01S00200Req.setJoribGB("Y"); // 사용자 메세지 조립YN
            cbSms01S00200Req.setJoribMsg(com.scbank.process.api.fw.core.utils.StringUtils.toFullChar(smsMsg)); // 사용자
                                                                                                               // 조립메세지
            cbSms01S00200Req.setTSPassword(tsPassword); // 통신비밀번호

            this.hostClient.sendOltp(hostCfg, cbSms01S00200Req, CbSms01S00200Res.class);
        } catch (Exception e) {
        	log.error("발급완료 SMS발송 오류 무시", e);
        }
        return true;
    }

    @ComponentOperation(name = "발급완료 SMS발송", description = "발급완료 SMS발송", author = "이완주")
    public boolean sendCompleteSMS(Map<String, String> input) {
        String UserID = StringUtils.defaultIfEmpty(input.get("UserID"), "");
        String RegNo = StringUtils.defaultIfEmpty(input.get("RegNo"), "");
        String SMSMsg = StringUtils.defaultIfEmpty(input.get("SMSMsg"), "");
        String TSPassword = StringUtils.defaultIfEmpty(input.get("TSPassword"), "99999999");
        String TransGB = StringUtils.defaultIfEmpty(input.get("TransGB"), "001"); // default 공동인증서 발급
        String CustomGB = StringUtils.defaultIfEmpty(input.get("CustomGB"), "IB9"); // default 전문종별 IB1IB비정형
        String JOB_TYPE = StringUtils.defaultIfEmpty(input.get("JOB_TYPE"), "GY"); // default 전문종별 IB1IB비정형

        try {
            OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_SMS01_S002");
            hostCfg.setImsTranCd("TI1SMS01");
            hostCfg.setInClassCd("S002");
            hostCfg.setSvcCd("911");
            hostCfg.setJobTp(JOB_TYPE);

            CbSms01S00200Req cbSms01S00200Req = new CbSms01S00200Req();
            cbSms01S00200Req.setUserID(UserID); // 사용자아이디
            cbSms01S00200Req.setCustomGB(CustomGB); // 전문종별 IB1IB비정형
            cbSms01S00200Req.setTransGB(TransGB); // 거래구분 001공인인증서(재발급) 002고객정보관리변경 003고객휴대폰번호변경 004고객이메일변경
                                                  // 005이체한도변경(감액)
                                                  // 006출금계좌 등록 007출금계좌 해지
            cbSms01S00200Req.setJuminNumgb(""); // 주민번호구분. "":개인,개인사업자 2:사업자
            cbSms01S00200Req.setPerBusNo(RegNo); // 실명번호
            cbSms01S00200Req.setJoribGB("Y"); // 사용자 메세지 조립YN
            cbSms01S00200Req.setJoribMsg(com.scbank.process.api.fw.core.utils.StringUtils.toFullChar(SMSMsg)); // 사용자
                                                                                                               // 조립메세지
            cbSms01S00200Req.setTSPassword(TSPassword); // 통신비밀번호

            this.hostClient.sendOltp(hostCfg, cbSms01S00200Req, CbSms01S00200Res.class);
        } catch (Exception e) {
        	log.error("발급완료 SMS발송 오류 무시", e);
        }

        return true;
    }

}
