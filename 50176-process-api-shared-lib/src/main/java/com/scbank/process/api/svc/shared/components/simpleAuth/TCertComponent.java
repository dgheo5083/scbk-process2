package com.scbank.process.api.svc.shared.components.simpleAuth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthRequest;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthResponse;
import com.scbank.process.api.svc.shared.utils.RandomKeyUtils;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TCertComponent {

    public final int TCERT_TCP_MSG_SIZE_AUTH = 177;
    public final int TCERT_TCP_RSP_MSG_SIZE_TOTAL = 179;

    public SimpleAuthResponse sendTauth(SimpleAuthRequest request) {
        return sendAuthMsg(request);
    }

    public SimpleAuthResponse sendAuthMsg(SimpleAuthRequest request) {

        SimpleAuthResponse response = new SimpleAuthResponse();

        String ip = PropertiesUtils.getString("RAON_ADAPTER_IP");
        int port = PropertiesUtils.getInt("RAON_ADAPTER_PORT");
        int timeout = PropertiesUtils.getInt("RAON_ADAPTER_TIMEOUT");

        log.info("Server IP : " + ip + ", Port : " + port);

        SocketAddress socketAddress = new InetSocketAddress(ip, port);

        Socket socket = null;
        DataOutputStream output = null;
        DataInputStream input = null;

        try {

            socket = new Socket();
            socket.setSoTimeout(timeout);
            socket.connect(socketAddress, timeout);

            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            byte[] reqMsgByteArr = makeAuthMsg(request);

            output.write(reqMsgByteArr);

            byte[] buffer = new byte[1024];
            byte[] rspMsgByteArr = new byte[TCERT_TCP_RSP_MSG_SIZE_TOTAL + 4];

            while (true) {
                int count = input.read(buffer);
                if (count == TCERT_TCP_RSP_MSG_SIZE_TOTAL + 4) {
                    break;
                }
            }

            System.arraycopy(buffer, 0, rspMsgByteArr, 0, TCERT_TCP_RSP_MSG_SIZE_TOTAL + 4);
            log.debug("간편인증 HTTP 응답 메시지 수신 성공");

            String repStrMsg = new String(rspMsgByteArr);
            log.debug("rspMsg : length : " + repStrMsg.length() + ", [" + repStrMsg + "]");

            int startIdx = 4;

            // 고객ID
            String custId = getString(repStrMsg, startIdx, 20);
            startIdx += 20;
            log.debug("cust_id : [" + custId + "]");

            // 서비스ID
            String serviceId = getString(repStrMsg, startIdx, 20);
            startIdx += 20;
            log.debug("svc_id : [" + serviceId + "]");

            // 거래번호
            String trId = getString(repStrMsg, startIdx, 20);
            startIdx += 20;
            log.debug("tr_id : [" + trId + "]");

            // 요청일시
            String reqDate = getString(repStrMsg, startIdx, 14);
            startIdx += 14;
            log.debug("req_date : [" + reqDate + "]");

            // 응답코드
            String resultCode = getString(repStrMsg, startIdx, 4);
            startIdx += 4;
            log.debug("result_code : [" + resultCode + "]");

            // 메시지
            String resultMsg = getString(repStrMsg, startIdx, 100);
            startIdx += 100;
            log.debug("result_msg : [" + resultMsg + "]");

            // 인증결과(Y/N)
            String authYn = getString(repStrMsg, startIdx, 1);
            startIdx += 1;
            log.debug("auth_yn : [" + authYn + "]");

            response.setCustId(custId);
            response.setServiceid(serviceId);
            response.setTrId(trId);
            response.setReqDate(reqDate);
            response.setResultCode(resultCode);
            response.setResultMsg(resultMsg);
            response.setAuthYn(authYn);

        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0060", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(간편인증)");
        } finally {

            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return response;

    }

    public byte[] makeAuthMsg(SimpleAuthRequest request) {

        String custId = PropertiesUtils.getString("RAON_CUST_ID"); // 고객ID
        String serviceId = PropertiesUtils.getString("RAON_CUST_ID"); // 서비스ID

        byte[] msgByteArr = new byte[TCERT_TCP_MSG_SIZE_AUTH + 4];

        for (int i = 0; i < msgByteArr.length; i++) {
            msgByteArr[i] = ' ';
        }

        int msgSize = 0;

        request.setCustId(custId);
        request.setServiceId(serviceId);
        request.setTrId(RandomKeyUtils.getKey(20));
        request.setReqDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        request.setAuthType("1");

        // 0. 메시지 길이
        System.arraycopy("0177".getBytes(), 0, msgByteArr, 0, 4);
        msgSize += 4;

        // 1. 고객ID
        if (StringUtils.isNotBlank(request.getCustId())) {
            System.arraycopy(request.getCustId().getBytes(), 0, msgByteArr, msgSize,
                    request.getCustId().getBytes().length);
        }
        msgSize += 20;

        // 2.서비스ID
        if (StringUtils.isNotBlank(request.getServiceId())) {
            System.arraycopy(request.getServiceId().getBytes(), 0, msgByteArr, msgSize,
                    request.getServiceId().getBytes().length);
        }
        msgSize += 20;

        // 3.거래번호
        if (StringUtils.isNotBlank(request.getTrId())) {
            System.arraycopy(request.getTrId().getBytes(), 0, msgByteArr, msgSize, request.getTrId().getBytes().length);
        }
        msgSize += 20;

        // 4.요청일시
        if (StringUtils.isNotBlank(request.getReqDate())) {
            System.arraycopy(request.getReqDate().getBytes(), 0, msgByteArr, msgSize,
                    request.getReqDate().getBytes().length);
        }
        msgSize += 14;

        // 5.통신사구분
        if (StringUtils.isNotBlank(request.getTelecomType())) {
            System.arraycopy(request.getTelecomType().getBytes(), 0, msgByteArr, msgSize,
                    request.getTelecomType().getBytes().length);
        }
        msgSize += 1;

        // 6.인증타입
        if (StringUtils.isNotBlank(request.getAuthType())) {
            System.arraycopy(request.getAuthType().getBytes(), 0, msgByteArr, msgSize,
                    request.getAuthType().getBytes().length);
        }
        msgSize += 1;

        // 7.전화번호
        if (StringUtils.isNotBlank(request.getCtn())) {
            System.arraycopy(request.getCtn().getBytes(), 0, msgByteArr, msgSize, request.getCtn().getBytes().length);
        }
        msgSize += 11;

        // 8.USIM 일련번호
        if (StringUtils.isNotBlank(request.getUiccid())) {
            System.arraycopy(request.getUiccid().getBytes(), 0, msgByteArr, msgSize,
                    request.getUiccid().getBytes().length);
        }
        msgSize += 19;

        // 9.망 식별번호
        if (StringUtils.isNotBlank(request.getImsi())) {
            System.arraycopy(request.getImsi().getBytes(), 0, msgByteArr, msgSize, request.getImsi().getBytes().length);
        }
        msgSize += 15;

        // 10.단말 일련번호
        if (StringUtils.isNotBlank(request.getImei())) {
            System.arraycopy(request.getImei().getBytes(), 0, msgByteArr, msgSize, request.getImei().getBytes().length);
        }
        msgSize += 14;

        // 11.단말OS
        if (StringUtils.isNotBlank(request.getMos())) {
            System.arraycopy(request.getMos().getBytes(), 0, msgByteArr, msgSize, request.getMos().getBytes().length);
        }
        msgSize += 1;

        // 12.생년월일
        if (StringUtils.isNotBlank(request.getBirthday())) {
            System.arraycopy(request.getBirthday().getBytes(), 0, msgByteArr, msgSize,
                    request.getBirthday().getBytes().length);
        }
        msgSize += 8;

        // 13.성별
        if (StringUtils.isNotBlank(request.getGender())) {
            System.arraycopy(request.getGender().getBytes(), 0, msgByteArr, msgSize,
                    request.getGender().getBytes().length);
        }
        msgSize += 1;

        // 14.성명
        if (StringUtils.isNotBlank(request.getName())) {
            System.arraycopy(request.getName().getBytes(), 0, msgByteArr, msgSize, request.getName().getBytes().length);
        }
        msgSize += 30;

        // 15.개인정보 이용 동의 여부
        if (StringUtils.isNotBlank(request.getPrivacySharingAgreeYn())) {
            System.arraycopy(request.getPrivacySharingAgreeYn().getBytes(), 0, msgByteArr, msgSize,
                    request.getPrivacySharingAgreeYn().getBytes().length);
        }
        msgSize += 1;

        // 16.개인정보 제3자 제공 동의 여부
        if (StringUtils.isNotBlank(request.getThirdPartyProvisionAgreeYn())) {
            System.arraycopy(request.getThirdPartyProvisionAgreeYn().getBytes(), 0, msgByteArr, msgSize,
                    request.getThirdPartyProvisionAgreeYn().getBytes().length);
        }
        msgSize += 1;

        return msgByteArr;

    }

    public String getString(String str, int start, int length) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes();
        byte[] value = new byte[length];

        for (int i = 0; i < length; i++) {
            value[i] = bytes[start + i];
        }

        return new String(value);
    }

}
