package com.scbank.process.api.svc.shared.components.lms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.lms.dto.LmsRequest;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
public class LmsComponent {

    private String lmsServerIp;
    private int lmsServerPort;

    private String member; // Client측 key일련번호
    private String userCode; // 사용자 발신 코드

    @PostConstruct
    public void init() {
        this.lmsServerIp = PropertiesUtils.getString("sms.server.ip");
        this.lmsServerPort = Integer.parseInt(PropertiesUtils.getString("lms.server.port"));

        this.member = "0"; // Client측 key일련번호
        this.userCode = "smstest"; // 사용자 발신 코드
    }

    @ComponentOperation(name = "LMS 발송")
    public String sendMain(LmsRequest request) throws PRCServiceException {

        String lmsResponse;

        Socket sendSocket = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;

        String[] lmsFieldData = new String[27];
        setLmsFieldData(request, lmsFieldData); // SMS Packet 의 각 필드를 setting 하는 메소드

        try {
            sendSocket = new Socket(this.lmsServerIp, this.lmsServerPort);

            bos = new BufferedOutputStream(sendSocket.getOutputStream());
            dos = new DataOutputStream(bos);
            bis = new BufferedInputStream(sendSocket.getInputStream());

            int[] lmsFieldLength = setLmsFieldInfo();

            ///// 서버에 SMS request 를 send..../
            sendLmsRequest(dos, lmsFieldData, lmsFieldLength);

            ////// 서버로 부터 응답을 받아옴 /////
            lmsResponse = reciveLmsResponse(bis);
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0063", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(LMS발송)");
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
                throw new PRCServiceException("MA3CMM0063", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(LMS발송)");
            }
        }

        String flag = "O";
        try {
            if (lmsResponse != null) {
                flag = lmsResponse.substring(94, 95);
            }
            log.info("LMS flag 값 	= [" + flag + "]");
            log.info("LMS return 값 	= [" + lmsResponse + "]");
        } catch (Exception e) {
            // TODO: handle exception
        }

        /*
         * lmsResponse 'O'가 아닐 경우 데이타값을 다시한번 확인 바람
         * szStatus='P'; 이통사번호오류
         * szStatus='N'; 전화번호오류
         * szStatus='I'; 사용자ID오류
         * szStatus='D'; 회사코드오류
         * szStatus='M'; 메시지가 NULL인 경우
         * SzStatus='T'; 예약일자 이상
         * szStatus='I'; 등록된 회사가 아니거나 회원이 아니다.
         * szStatus='C'; 잔액초과
         * szStatus='O'; 호스트 성공 리턴코드
         * szStatus='X'; 호스트 실패 리턴코드
         */

        return flag;
    }

    /**
     * SMS Packet 각 필드의 Length를 Setting
     * 
     * @return
     */
    private int[] setLmsFieldInfo() {
        int[] lmsFieldLength = new int[27];

        lmsFieldLength[0] = 1; // char
        lmsFieldLength[1] = 1; // char
        lmsFieldLength[2] = 10; // char[10]
        lmsFieldLength[3] = 8; // char[8]
        lmsFieldLength[4] = 30; // char[30]
        lmsFieldLength[5] = 16; // char[16]
        lmsFieldLength[6] = 12; // char[12]
        lmsFieldLength[7] = 16; // char[16]
        lmsFieldLength[8] = 1; // char status
        lmsFieldLength[9] = 4; // char[4]
        lmsFieldLength[10] = 4; // char[4]
        lmsFieldLength[11] = 4; // char[4]
        lmsFieldLength[12] = 120; // char[120] 제목
        lmsFieldLength[13] = 2000; // char[120] 메세지
        lmsFieldLength[14] = 10; // char[10]
        lmsFieldLength[15] = 8; // char[8]
        lmsFieldLength[16] = 3; // char[3]
        lmsFieldLength[17] = 16; // long
        lmsFieldLength[18] = 4; // char[4]
        lmsFieldLength[19] = 4; // char[4]
        lmsFieldLength[20] = 4; // char[4]
        lmsFieldLength[21] = 32; // char[32]
        lmsFieldLength[22] = 1; // char[1]
        lmsFieldLength[23] = 512; // char[512]
        lmsFieldLength[24] = 512; // char[512]
        lmsFieldLength[25] = 512; // char[512]
        lmsFieldLength[26] = 32; // char[32]

        return lmsFieldLength;
    }

    /**
     * SMS Packet의 각 필드를 Setting
     * 
     * @param request
     * @return
     */
    private String[] setLmsFieldData(LmsRequest request, String[] lmsFieldData) {

        SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dfTime = new SimpleDateFormat("HHmmss");
        java.util.Date currTime = new java.util.Date();

        lmsFieldData[0] = "B"; // Command
        lmsFieldData[1] = "A"; // Type
        lmsFieldData[2] = dfDate.format(currTime); // 메세지발송일자(YYYYMMDD) char[10]
        lmsFieldData[3] = dfTime.format(currTime); // 메세지발송시간(HHMMSS) char[10]
        lmsFieldData[4] = this.userCode; // 사용자(발신자)코드 행번(WM_ID)사용함 char[30]
        lmsFieldData[5] = request.getMessageCode(); // 사용자(발신자)명 메세지코드
        lmsFieldData[6] = request.getDeptCode(); // DeptCode
        lmsFieldData[7] = request.getDeptName(); // DeptName

        if (StringUtils.isBlank(request.getRateDate()) || StringUtils.isBlank(request.getRateTime())) {
            lmsFieldData[8] = ""; // Status
        } else {
            lmsFieldData[8] = "R"; // Status
        }

        // CallPhone처리 '011-123-4567' --> '011', '123', 4567'로 분리
        lmsFieldData[9] = request.getCallPhone1(); // 수신전화번호 1
        lmsFieldData[10] = request.getCallPhone2(); // 수신전화번호 2
        lmsFieldData[11] = request.getCallPhone3(); // 수신전화번호 3

        lmsFieldData[12] = request.getSubject(); // 제목
        lmsFieldData[13] = request.getCallMessage(); // 메세지
        lmsFieldData[14] = request.getRateDate(); // 예약일자
        lmsFieldData[15] = request.getRateTime(); // 예약시간
        lmsFieldData[16] = ""; // Dummy
        lmsFieldData[17] = this.member; // 클라이언트측 일련 번호

        // ReqPhone처리 '011-123-4567' --> '011', '123', 4567'로 분리
        lmsFieldData[18] = request.getReqPhone1(); // reqphone1
        lmsFieldData[19] = request.getReqPhone2(); // reqphone2
        lmsFieldData[20] = request.getReqPhone3(); // reqphone3
        lmsFieldData[21] = request.getCallName(); // 수신자명 주민번호 char[32]
        lmsFieldData[22] = "0"; // 파일카운트
        lmsFieldData[23] = ""; // 파일경로1
        lmsFieldData[24] = ""; // 파일경로2
        lmsFieldData[25] = ""; // 파일경로3
        lmsFieldData[26] = ""; // reserved

        return lmsFieldData;
    }

    /**
     * 서버에 LMS request 를 send.....
     * 
     * @param dos
     * @param lmsFieldData
     * @param lmsFieldLength
     * @throws PRCServiceException
     */
    private void sendLmsRequest(DataOutputStream dos, String[] lmsFieldData, int[] lmsFieldLength)
            throws PRCServiceException {

        StringBuffer strBuff = new StringBuffer();
        byte[] byteBuff = null;

        try {
            for (int inx = 0; inx < 27; inx++) {
                appendToBuffer(strBuff, lmsFieldData[inx], lmsFieldLength[inx]);
            }
            log.debug("sendLMSRequest() len[" + strBuff.length() + "], strBuff = [" + strBuff.toString() + "]");

            byteBuff = strBuff.toString().getBytes("EUC-KR");
            dos.write(byteBuff, 0, byteBuff.length);

            dos.flush();
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0063", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(LMS발송)");
        }
    }

    private String reciveLmsResponse(BufferedInputStream bis) throws PRCServiceException {
        byte[] byteBuff = new byte[1024];
        String strReceivedMsg = null;

        try {
            int iReadCount = bis.read(byteBuff);
            if (iReadCount > 0) {
                strReceivedMsg = new String(byteBuff, 0, iReadCount, "EUC-KR");
            }

            log.info("reciveLmsResponse 응답MSG : " + strReceivedMsg);
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0063", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(LMS발송)");
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
                strBuff.append(' ');
            }
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0063", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(LMS발송)");
        }
    }
}
