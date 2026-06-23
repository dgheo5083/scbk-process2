package com.scbank.process.api.svc.shared.components.ars;

import static com.scbank.process.api.svc.shared.components.ars.utils.SyncAtByteUtils.makeBytes20Msg;
import static com.scbank.process.api.svc.shared.components.ars.utils.SyncAtByteUtils.readBytes;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.Account_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.ArAmount_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.ArBankName_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.ArClientName_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.ArrayCnt_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.AuthTelNo_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.BANK_CR_PACKET_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.ClientName_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.HEADER_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.InAmount_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.InBankName_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.InClientName_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.ResultCode_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.Ssn_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.SvcManChange_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.TargetService_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.TotalAmount_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.TotalCnt_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.TranId_LENGTH;
import static com.scbank.process.api.svc.shared.constants.SyncAtConstants.WorkCd_LENGTH;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleRequest;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleRequest.DepositInfo;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class SyncAtComponent {

        private final String defaultCharset = "EUC-KR";

        /**
         * 
         * @param request
         * @return
         */
        @ComponentOperation(name = "ARS 인증")
        public SyncAtClientCycleResponse syncAtClientCycle(SyncAtClientCycleRequest request) {

                byte[] requestBytes = this.makeRequestBytes(request);

                String SYNCAT_GW_IP = PropertiesUtils.getString("SYNCAT_GW_IP");
                String SYNCAT_GW_PORT = PropertiesUtils.getString("SYNCAT_GW_PORT");

                byte[] responseBytes = null;

                try (
                                Socket socket = new Socket(SYNCAT_GW_IP, Integer.parseInt(SYNCAT_GW_PORT));
                                InputStream in = socket.getInputStream();
                                OutputStream out = socket.getOutputStream();) {

                        socket.setSoTimeout(130 * 1000); // 기존 90초 -> 2channel 강화후 130초로 늘림 //

                        out.write(requestBytes, 0, requestBytes.length);
                        // out.flush();

                        responseBytes = new byte[BANK_CR_PACKET_LENGTH];
                        int readBytes = in.read(responseBytes, 0, responseBytes.length);

                        log.debug("Syncat 응답 메시지 길이: {}", readBytes);

                        SyncAtClientCycleResponse response = this.buildResponse(responseBytes);
                        return response;
                } catch (Exception e) {
                        throw new PRCServiceException("MA3CMM0061", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(전화승인)");
                }
        }

        /**
         * 요청 byte 배열 데이터를 생성한다.
         * 
         * @param request 업무 서비스에서 넘어온 ARS 요청 데이터
         * @return ARS 요청 데이터를 byte 배열로 변환환 결과
         */
        private byte[] makeRequestBytes(SyncAtClientCycleRequest request) {
                int offset = 0;
                String authTelNo = request.getAuthTelNo();
                String account = request.getAccount();
                String targetService = request.getTargetService();
                String tranId = request.getTranId();
                String workCode = request.getWorkCode();
                String svcManChange = request.getSvcManChange();
                String ssn = request.getSsn();
                String clientName = request.getClientName();
                String inBankName = request.getInBankName();
                String inClientName = request.getInClientName();
                String inAmount = request.getInAmount();
                String totalCnt = request.getTotalCnt();
                String totalAmount = request.getTotalAmount();
                String arrayCnt = request.getArrayCnt();
                String arBankName = ""; /* 40Byte 받는사람 은행명 반복 */
                String arClientName = ""; /* 40Byte 받는사람 이름 반복 */
                String arAmount = ""; /* 20Byte 이체금액 반복 */
                int count = 0;

                if (StringUtils.isNotEmpty(arrayCnt)) {
                        count = Integer.parseInt(arrayCnt);// 반복횟수에 따라 Byte Set
                }

                int reqCount = HEADER_LENGTH +
                                TranId_LENGTH + Account_LENGTH +
                                AuthTelNo_LENGTH + TargetService_LENGTH;

                if (StringUtils.isEmpty(ssn)) {
                        ssn = "";
                }

                reqCount += Ssn_LENGTH;

                log.debug("Syncat ssn : [" + ssn + "]");

                if ("05".equals(targetService)) {
                        reqCount += ClientName_LENGTH + InBankName_LENGTH +
                                        InClientName_LENGTH + InAmount_LENGTH +
                                        TotalCnt_LENGTH + TotalAmount_LENGTH +
                                        ArrayCnt_LENGTH +
                                        (count * (ArBankName_LENGTH + ArClientName_LENGTH +
                                                        ArAmount_LENGTH));
                }

                log.debug("Syncat Length->" + reqCount);

                if ("02".equals(targetService) || "03".equals(targetService) || "04".equals(targetService)
                                || "05".equals(targetService) || "07".equals(targetService)) {
                        reqCount = reqCount + WorkCd_LENGTH;
                }

                if ("07".equals(targetService)) {
                        reqCount = reqCount + SvcManChange_LENGTH;
                }

                byte[] requestBytes = new byte[reqCount];

                try {
                        makeBytes20Msg(requestBytes, offset, String.valueOf(reqCount).getBytes(defaultCharset),
                                        HEADER_LENGTH); // 패킷 길이
                        offset += HEADER_LENGTH;
                        makeBytes20Msg(requestBytes, offset, targetService.getBytes(defaultCharset),
                                        TargetService_LENGTH); // TargetService
                        offset += TargetService_LENGTH;
                        makeBytes20Msg(requestBytes, offset, account.getBytes(defaultCharset),
                                        Account_LENGTH); // Account
                        offset += Account_LENGTH;
                        makeBytes20Msg(requestBytes, offset, tranId.getBytes(defaultCharset),
                                        TranId_LENGTH); // TranId
                        offset += TranId_LENGTH;
                        makeBytes20Msg(requestBytes, offset, authTelNo.getBytes(defaultCharset),
                                        AuthTelNo_LENGTH); // AuthTelNo
                        offset += AuthTelNo_LENGTH;

                        makeBytes20Msg(requestBytes, offset, ssn.getBytes(defaultCharset), Ssn_LENGTH); // Ssn
                        offset += Ssn_LENGTH;

                        if ("05".equals(targetService)) {
                                makeBytes20Msg(requestBytes, offset, clientName.getBytes(defaultCharset),
                                                ClientName_LENGTH); // ClientName
                                offset += ClientName_LENGTH;
                        }

                        if ("02".equals(targetService) || "03".equals(targetService) || "04".equals(targetService)
                                        || "05".equals(targetService) || "07".equals(targetService)) {
                                makeBytes20Msg(requestBytes, offset, workCode.getBytes(defaultCharset),
                                                WorkCd_LENGTH); // WorkCd
                                offset += WorkCd_LENGTH;
                        }

                        if ("07".equals(targetService)) {
                                makeBytes20Msg(requestBytes, offset, svcManChange.getBytes(defaultCharset),
                                                SvcManChange_LENGTH); // SvcManChange
                                offset += SvcManChange_LENGTH;
                        }

                        if ("05".equals(targetService)) {
                                makeBytes20Msg(requestBytes, offset, inBankName.getBytes(defaultCharset),
                                                InBankName_LENGTH); // InBankName
                                offset += InBankName_LENGTH;
                                makeBytes20Msg(requestBytes, offset, inClientName.getBytes(defaultCharset),
                                                InClientName_LENGTH); // InClientName
                                offset += InClientName_LENGTH;
                                makeBytes20Msg(requestBytes, offset, inAmount.getBytes(defaultCharset),
                                                InAmount_LENGTH); // InAmount
                                offset += InAmount_LENGTH;
                                makeBytes20Msg(requestBytes, offset, totalCnt.getBytes(defaultCharset),
                                                TotalCnt_LENGTH); // TotalCnt
                                offset += TotalCnt_LENGTH;
                                makeBytes20Msg(requestBytes, offset, totalAmount.getBytes(defaultCharset),
                                                TotalAmount_LENGTH); // TotalAmount
                                offset += TotalAmount_LENGTH;
                                makeBytes20Msg(requestBytes, offset, arrayCnt.getBytes(defaultCharset),
                                                ArrayCnt_LENGTH); // ArrayCnt
                                offset += ArrayCnt_LENGTH;
                        }

                        log.debug("request Array 추가전->" + new String(requestBytes, 0, requestBytes.length));

                        if (count > 1) { // 1이상만 상세내역 보내줌.
                                List<DepositInfo> depositList = request.getDepositList();
                                for (int i = 0; i < count; i++) {
                                        DepositInfo deposit = depositList.get(i);
                                        // Vector로 들어온 데이터 차례대로 꺼내옴
                                        arBankName = deposit.getBankName();
                                        arClientName = deposit.getClientName();
                                        arAmount = deposit.getAmount();
                                        makeBytes20Msg(requestBytes, offset, arBankName.getBytes(defaultCharset),
                                                        ArBankName_LENGTH); // 반복_입금은행
                                        offset += ArBankName_LENGTH;
                                        makeBytes20Msg(requestBytes, offset, arClientName.getBytes(defaultCharset),
                                                        ArClientName_LENGTH); // 반복_입금자명
                                        offset += ArClientName_LENGTH;
                                        makeBytes20Msg(requestBytes, offset, arAmount.getBytes(defaultCharset),
                                                        ArAmount_LENGTH); // 반복_입금금액
                                        offset += ArAmount_LENGTH;
                                }
                        }

                        return requestBytes;
                } catch (Exception e) {
                        throw new PRCServiceException("MA3CMM0061", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(전화승인)");
                }
        }

        /**
         * 응답 바이트 배열을 ARS 인증 응답 VO로 변환한다.
         * 
         * @param responseBytes 응답 바이트 배열
         * @return 응답 바이트 배열을 ARS 인증 응답 VO로 변환한 결과
         */
        private SyncAtClientCycleResponse buildResponse(byte[] responseBytes) {
                int offset = 0;

                String header = "";
                String tranId = "";
                String resultCode = "";

                header = new String(readBytes(responseBytes, offset, HEADER_LENGTH)).trim();
                offset += HEADER_LENGTH;
                tranId = new String(readBytes(responseBytes, offset, TranId_LENGTH)).trim();
                offset += TranId_LENGTH;
                resultCode = new String(readBytes(responseBytes, offset, ResultCode_LENGTH))
                                .trim();
                offset += ResultCode_LENGTH;

                log.debug("# ARS 응답 정보: {}, {}, {}", header, tranId, resultCode);

                return SyncAtClientCycleResponse.builder()
                                .tranId(tranId)
                                .resultCode(resultCode)
                                .build();
        }

        @ComponentOperation(name = "ARS 인증")
        public Map<String, Object> syncAtClientCycle(Map<String, Object> params) {
                int offset = 0;
                String authTelNo = (String) params.get("authTelNo");
                String account = (String) params.get("account");
                String targetService = (String) params.get("targetService");
                String tranId = (String) params.get("tranId");
                String workCode = (String) params.get("workCode");
                String svcManChange = (String) params.get("svcManChange");
                String ssn = (String) params.get("ssn"); /* 40Byte 보내는 사람 이름 */
                String clientName = (String) params.get("clientName"); /* 40Byte 보내는 사람 이름 */
                String inBankName = (String) params.get("inBankName"); /* 40Byte 받는사람 은행명 */
                String inClientName = (String) params.get("inClientName"); /* 40Byte 받는사람 이름 */
                String inAmount = (String) params.get("inAmount"); /* 20Byte 이체금액 */
                String totalCnt = (String) params.get("totalCnt"); /* 03Byte 총 건수 */
                String totalAmount = (String) params.get("totalAmount"); /* 20Byte 총 금액 */
                String arrayCnt = (String) params.get("arrayCnt"); /* 03Byte 갯수 */
                String arBankName = ""; /* 40Byte 받는사람 은행명 반복 */
                String arClientName = ""; /* 40Byte 받는사람 이름 반복 */
                String arAmount = ""; /* 20Byte 이체금액 반복 */
                int count = 0;

                if (arrayCnt != null && arrayCnt != "") {
                        count = Integer.parseInt(arrayCnt);// 반복횟수에 따라 Byte Set
                }

                int req_count = HEADER_LENGTH +
                                TranId_LENGTH + Account_LENGTH +
                                AuthTelNo_LENGTH + TargetService_LENGTH;

                if (ssn == null)
                        ssn = "";

                req_count += Ssn_LENGTH;

                log.debug("Syncat ssn : [" + ssn + "]");

                if (targetService.equals("05")) {
                        req_count += ClientName_LENGTH + InBankName_LENGTH +
                                        InClientName_LENGTH + InAmount_LENGTH +
                                        TotalCnt_LENGTH + TotalAmount_LENGTH +
                                        ArrayCnt_LENGTH +
                                        (count * (ArBankName_LENGTH + ArClientName_LENGTH +
                                                        ArAmount_LENGTH));
                }

                log.debug("Syncat Length->" + req_count);

                if (targetService.equals("02") || targetService.equals("03") || targetService.equals("04")
                                || targetService.equals("05") || targetService.equals("07")) {
                        req_count = req_count + WorkCd_LENGTH;
                }

                if (targetService.equals("07")) {
                        req_count = req_count + SvcManChange_LENGTH;
                }

                byte[] request = new byte[req_count];

                try {
                        makeBytes20Msg(request, offset, ("" + req_count).getBytes(defaultCharset),
                                        HEADER_LENGTH); // 패킷 길이
                        offset += HEADER_LENGTH;
                        makeBytes20Msg(request, offset, targetService.getBytes(defaultCharset),
                                        TargetService_LENGTH); // TargetService
                        offset += TargetService_LENGTH;
                        makeBytes20Msg(request, offset, account.getBytes(defaultCharset), Account_LENGTH); // Account
                        offset += Account_LENGTH;
                        makeBytes20Msg(request, offset, tranId.getBytes(defaultCharset), TranId_LENGTH); // TranId
                        offset += TranId_LENGTH;
                        makeBytes20Msg(request, offset, authTelNo.getBytes(defaultCharset),
                                        AuthTelNo_LENGTH); // AuthTelNo
                        offset += AuthTelNo_LENGTH;

                        makeBytes20Msg(request, offset, ssn.getBytes(defaultCharset), Ssn_LENGTH); // Ssn
                        offset += Ssn_LENGTH;

                        if (targetService.equals("05")) {
                                makeBytes20Msg(request, offset, clientName.getBytes(defaultCharset),
                                                ClientName_LENGTH); // ClientName
                                offset += ClientName_LENGTH;
                        }
                        if (targetService.equals("02") || targetService.equals("03") || targetService.equals("04")
                                        || targetService.equals("05") || targetService.equals("07")) {
                                makeBytes20Msg(request, offset, workCode.getBytes(defaultCharset),
                                                WorkCd_LENGTH); // WorkCd
                                offset += WorkCd_LENGTH;
                        }
                        if (targetService.equals("07")) {
                                makeBytes20Msg(request, offset, svcManChange.getBytes(defaultCharset),
                                                SvcManChange_LENGTH); // SvcManChange
                                offset += SvcManChange_LENGTH;
                        }
                        if (targetService.equals("05")) {
                                makeBytes20Msg(request, offset, inBankName.getBytes(defaultCharset),
                                                InBankName_LENGTH); // InBankName
                                offset += InBankName_LENGTH;
                                makeBytes20Msg(request, offset, inClientName.getBytes(defaultCharset),
                                                InClientName_LENGTH); // InClientName
                                offset += InClientName_LENGTH;
                                makeBytes20Msg(request, offset, inAmount.getBytes(defaultCharset),
                                                InAmount_LENGTH); // InAmount
                                offset += InAmount_LENGTH;
                                makeBytes20Msg(request, offset, totalCnt.getBytes(defaultCharset),
                                                TotalCnt_LENGTH); // TotalCnt
                                offset += TotalCnt_LENGTH;
                                makeBytes20Msg(request, offset, totalAmount.getBytes(defaultCharset),
                                                TotalAmount_LENGTH); // TotalAmount
                                offset += TotalAmount_LENGTH;
                                makeBytes20Msg(request, offset, arrayCnt.getBytes(defaultCharset),
                                                ArrayCnt_LENGTH); // ArrayCnt
                                offset += ArrayCnt_LENGTH;
                        }

                        log.debug("request Array 추가전->" + new String(request, 0, request.length));

                        if (count > 1) { // 1이상만 상세내역 보내줌.
                                for (int i = 0; i < count; i++) {
                                        // Vector로 들어온 데이터 차례대로 꺼내옴
                                        arBankName = (String) params.get("ArBankName" + i);
                                        arClientName = (String) params.get("ArClientName" + i);
                                        arAmount = (String) params.get("ArAmount" + i);
                                        makeBytes20Msg(request, offset, arBankName.getBytes(defaultCharset),
                                                        ArBankName_LENGTH); // 반복_입금은행
                                        offset += ArBankName_LENGTH;
                                        makeBytes20Msg(request, offset, arClientName.getBytes(defaultCharset),
                                                        ArClientName_LENGTH); // 반복_입금자명
                                        offset += ArClientName_LENGTH;
                                        makeBytes20Msg(request, offset, arAmount.getBytes(defaultCharset),
                                                        ArAmount_LENGTH); // 반복_입금금액
                                        offset += ArAmount_LENGTH;
                                }
                        }
                } catch (Exception e) {
                        throw new PRCServiceException("MA3CMM0061", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(전화승인)");
                }

                log.debug("request value-->" + new String(request, 0, request.length));

                Socket socket = null;
                InputStream in = null;
                OutputStream out = null;
                byte[] response = null;

                try {

                        String SYNCAT_GW_IP = PropertiesUtils.getString("SYNCAT_GW_IP");
                        String SYNCAT_GW_PORT = PropertiesUtils.getString("SYNCAT_GW_PORT");

                        socket = new Socket(SYNCAT_GW_IP, Integer.parseInt(SYNCAT_GW_PORT));
                        socket.setSoTimeout(130 * 1000); // 기존 90초 -> 2channel 강화후 130초로 늘림 //

                        in = socket.getInputStream();
                        out = socket.getOutputStream();

                        out.write(request, 0, request.length);

                        response = new byte[BANK_CR_PACKET_LENGTH];
                        in.read(response, 0, response.length);
                } catch (Exception e) {
                        throw new PRCServiceException("MA3CMM0061", "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(전화승인)");
                } finally {
                        try {
                                if (in != null)
                                        in.close();
                                if (out != null)
                                        out.close();
                                if (socket != null)
                                        socket.close();
                        } catch (Exception e) {
                                throw new PRCServiceException("MA3CMM0061",
                                                "고객님께서 요청하신 거래를 처리하는중 오류가 발생하였습니다.<br />(전화승인)");
                        }
                }

                int offset2 = 0;

                String res_header = "";
                String res_tranId = "";
                String resultCode = "";

                res_header = new String(readBytes(response, offset2, HEADER_LENGTH)).trim();
                offset2 += HEADER_LENGTH;
                res_tranId = new String(readBytes(response, offset2, TranId_LENGTH)).trim();
                offset2 += TranId_LENGTH;
                resultCode = new String(readBytes(response, offset2, ResultCode_LENGTH)).trim();
                offset2 += ResultCode_LENGTH;

                Map<String, Object> result = new HashMap<String, Object>();
                result.put("tranId", res_tranId);
                result.put("resultCode", resultCode);
                return result;
        }

}
