package com.scbank.process.api.svc.shared.components.clickToCall;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallRequest;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
public class ClickToCallComponent {

    private String clickToCallServerIP = "10.61.13.204";
    private int clickToCallServerPort = 10010;

    private final int PACKET_LENGTH = 1305; // ClickToCall 서비스 전체 Byte는 1305 고정이다.

    private final int SOH_LENGTH = 1;
    private final int MESSAGE_LENGTH = 4;
    private final int PERBUSNO_LENGTH = 13;
    private final int CUSTNAME_LENGTH = 20;
    private final int SERVICEPATH_LENGTH = 20;
    private final int CALLGROUP_LENGTH = 5;
    private final int CUSTTELNO_LENGTH = 12;
    private final int URL_LENGTH = 200;
    private final int COMMAND_LENGTH = 1024;
    private final int RESULTCODE_LENGTH = 2;
    private final int ERRORCODE_LENGTH = 3;
    private final int EXT_LENGTH = 1;

    @PostConstruct
    public void init() {
        clickToCallServerIP = PropertiesUtils.getString("clickToCall.server.ip");
        clickToCallServerPort = PropertiesUtils.getInt("clickToCall.server.port");
    }

    @ComponentOperation
    public ClickToCallResponse send(ClickToCallRequest request) throws PRCServiceException {
        log.debug("##### ClickToCallRequest : {}", request.toString());

        ClickToCallResponse clickToCallResponse = new ClickToCallResponse();

        byte[] byteBuff = new byte[1305]; // Byte 고정임

        StringBuffer strBuff = new StringBuffer();
        int offset = 0;

        try (Socket sendSocket = new Socket(clickToCallServerIP, clickToCallServerPort);
                BufferedOutputStream bos = new BufferedOutputStream(sendSocket.getOutputStream());
                BufferedInputStream bis = new BufferedInputStream(sendSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(bos);) {

            makeBytes20Msg(byteBuff, offset, "1".getBytes(), SOH_LENGTH);
            offset += SOH_LENGTH;
            makeBytes20Msg(byteBuff, offset, String.valueOf(PACKET_LENGTH).getBytes(), MESSAGE_LENGTH);
            offset += MESSAGE_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getPerBusNo().getBytes(), PERBUSNO_LENGTH);
            offset += PERBUSNO_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getCustName().getBytes("EUC-KR"), CUSTNAME_LENGTH);
            offset += CUSTNAME_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getServicePath().getBytes("EUC-KR"), SERVICEPATH_LENGTH);
            offset += SERVICEPATH_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getCallGroup().getBytes(), CALLGROUP_LENGTH);
            offset += CALLGROUP_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getCustTelNo().getBytes(), CUSTTELNO_LENGTH);
            offset += CUSTTELNO_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getUrl().getBytes("EUC-KR"), URL_LENGTH);
            offset += URL_LENGTH;
            makeBytes20Msg(byteBuff, offset, request.getCommand().getBytes("EUC-KR"), COMMAND_LENGTH);
            offset += COMMAND_LENGTH;
            makeBytes20Msg(byteBuff, offset, "".getBytes(), RESULTCODE_LENGTH);
            offset += RESULTCODE_LENGTH;
            makeBytes20Msg(byteBuff, offset, "".getBytes(), ERRORCODE_LENGTH);
            offset += ERRORCODE_LENGTH;
            makeBytes20Msg(byteBuff, offset, "3".getBytes(), EXT_LENGTH);
            offset += EXT_LENGTH;

            int offset2 = 0; // LOG용으로 씀~
            // [이하] 데이터 로그
            log.debug("#### ClickToCall request Data Start ####");
            log.debug("### ClickToCall request SOH 	       : {}",
                    new String(byteBuff, offset2, SOH_LENGTH, "EUC-KR"));
            offset2 += SOH_LENGTH;

            log.debug("### ClickToCall request MessageLength : {}",
                    new String(byteBuff, offset2, MESSAGE_LENGTH, "EUC-KR"));
            offset2 += MESSAGE_LENGTH;

            log.debug("### ClickToCall request PerBusNo1     : {}",
                    new String(byteBuff, offset2, PERBUSNO_LENGTH, "EUC-KR"));
            offset2 += PERBUSNO_LENGTH;

            log.debug("### ClickToCall request CustName      : {}",
                    new String(byteBuff, offset2, CUSTNAME_LENGTH, "EUC-KR"));
            offset2 += CUSTNAME_LENGTH;

            log.debug("### ClickToCall request ServicePath   : {}",
                    new String(byteBuff, offset2, SERVICEPATH_LENGTH, "EUC-KR"));
            offset2 += SERVICEPATH_LENGTH;

            log.debug("### ClickToCall request CallGroup     : {}",
                    new String(byteBuff, offset2, CALLGROUP_LENGTH, "EUC-KR"));
            offset2 += CALLGROUP_LENGTH;

            log.debug("### ClickToCall request CustTelno     : {}",
                    new String(byteBuff, offset2, CUSTTELNO_LENGTH, "EUC-KR"));
            offset2 += CUSTTELNO_LENGTH;

            log.debug("### ClickToCall request URL           : {}",
                    new String(byteBuff, offset2, URL_LENGTH, "EUC-KR"));
            offset2 += URL_LENGTH;

            log.debug("### ClickToCall request Command       : {}",
                    new String(byteBuff, offset2, COMMAND_LENGTH, "EUC-KR"));
            offset2 += COMMAND_LENGTH;

            log.debug("### ClickToCall request ResultCode    : {}",
                    new String(byteBuff, offset2, RESULTCODE_LENGTH, "EUC-KR"));
            offset2 += RESULTCODE_LENGTH;

            log.debug("### ClickToCall request ErrorCode     : {}",
                    new String(byteBuff, offset2, ERRORCODE_LENGTH, "EUC-KR"));
            offset2 += ERRORCODE_LENGTH;

            log.debug("###PSH ClickToCall request EXT    	   : {}",
                    new String(byteBuff, offset2, EXT_LENGTH, "EUC-KR"));
            offset2 += EXT_LENGTH;

            log.debug("#### ClickToCall request Data END ####");
            // [이상] 데이터 로그
            log.debug("######## byteBuffLength : [{}]", byteBuff.length);

            dos.write(byteBuff, 0, byteBuff.length);
            dos.flush();

            log.debug("###PSH finally : [{}]", clickToCallResponse.toString());
            clickToCallResponse = receive(bis);
            log.debug("###PSH clickToCallResponse : [{}]", clickToCallResponse.toString());
        } catch (Exception e) {
            throw new PRCServiceException("", "전화상담 호출 실패입니다.");
        }

        return clickToCallResponse;
    }

    @ComponentOperation
    public ClickToCallResponse receive(BufferedInputStream bis) throws Exception {
        byte[] byteBuff = new byte[1305];
        ClickToCallResponse response = new ClickToCallResponse();
        try {

            int iReadCount = bis.read(byteBuff);

            log.debug("#### iReadCount : {}", iReadCount);

            int offset2 = 0;
            log.debug("#### ClickToCall response Data Start ####");

            response.setSoh(new String(byteBuff, offset2, SOH_LENGTH, "EUC-KR").trim()); // 패킷 시작
            response.setMessageLength(new String(byteBuff, offset2, MESSAGE_LENGTH, "EUC-KR").trim()); // 패킷 전체 길이
            response.setPerBusNo(new String(byteBuff, offset2, PERBUSNO_LENGTH, "EUC-KR").trim()); // 고객번호
            response.setCustName(new String(byteBuff, offset2, CUSTNAME_LENGTH, "EUC-KR").trim()); // 고객명
            response.setServicePath(new String(byteBuff, offset2, SERVICEPATH_LENGTH, "EUC-KR").trim()); // 서비스요청 채널
            response.setCallGroup(new String(byteBuff, offset2, CALLGROUP_LENGTH, "EUC-KR").trim()); // 상담요청 채널
            response.setCustTelNo(new String(byteBuff, offset2, CUSTTELNO_LENGTH, "EUC-KR").trim()); // 상담요청 전화번호
            response.setUrl(new String(byteBuff, offset2, URL_LENGTH, "EUC-KR").trim()); // 상담후 URL
            response.setCommand(new String(byteBuff, offset2, COMMAND_LENGTH, "EUC-KR").trim()); // 상담내용
            response.setResultCode(new String(byteBuff, offset2, RESULTCODE_LENGTH, "EUC-KR").trim()); // 결과코드
            response.setErrorCode(new String(byteBuff, offset2, ERRORCODE_LENGTH, "EUC-KR").trim()); // 에러코드
            response.setExt(new String(byteBuff, offset2, EXT_LENGTH, "EUC-KR").trim()); // 패킷 끝

            log.debug("#### ClickToCall response Data END ####");

            log.debug("#### clickToCallReceiveMessage() 응답 MSG : " + response.toString());
        } catch (Exception e) {
            throw e;
        }

        return response;
    }

    /**
     * ByteUtil data외offset -> 0x20으로 채움
     * 
     * @param _msg
     * @param _offset
     * @param _data
     * @param _len
     */
    private void makeBytes20Msg(byte[] _msg, int _offset, byte[] _data, int _len) {

        if (_data == null) {
            makeNullBytes(_msg, _offset, _len);
            return;
        }

        for (int i = 0; i < _data.length; i++) {
            _msg[_offset++] = _data[i];
        }

        if (_data.length < _len) {
            for (int i = 0; i < (_len - _data.length); i++) {
                _msg[_offset++] = (byte) 0x20;
            }
        }
    }

    private void makeNullBytes(byte[] _msg, int _offset, int _len) {
        for (int i = 0; i < _len; i++) {
            _msg[_offset++] = 0;
        }
    }
}
