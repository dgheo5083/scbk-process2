package com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.edmi.base.constant.EDMiConstants;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * EDMi 전문 송신 메시지 인코더 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class EDMiFixedLengthMessageEncoder implements IEDMiMessageEncoder {

    private final ObjectMapper objectMapper;

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        try {
            if (object instanceof EDMiRequestMessage request) {
                String systemId = request.getSystemId();
                String interfaceId = request.getInterfaceId();
                String captureSystem = request.getCaptureSystem();
                Object requestMessage = request.getRequestMessage();
                String trackingId = request.getTrackingId();
                String typeName = request.getTypeName();
                String senderBody = request.getSenderBody();
                String senderDomainBody = request.getSenderDomainBody();
                boolean isHexEncoding = request.isHexEncoding(); // Hex 인코딩 처리여부

                Map<String, Object> message = new HashMap<>();
                message.put(EDMiConstants.FLD_MESSAGE,
                        isHexEncoding ? Hex.encodeHexString((byte[]) requestMessage, false) : requestMessage);

                Map<String, Object> tranData = new HashMap<>();
                tranData.put(EDMiConstants.FLD_TRANDATA, message);

                Map<String, Object> messageEnvelope = EDMiMessageBuilder.builder()
                        .systemId(systemId)
                        .trackingId(trackingId)
                        .serviceBusId(interfaceId)
                        .captureSystem(captureSystem)
                        .messageTypeName(typeName)
                        .messageSenderBody(senderBody)
                        .messageSenderDomainBody(senderDomainBody)
                        .requestMessage(tranData)
                        .build();

                byte[] requestBytes = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(messageEnvelope);

                String defaultCharset = RuntimeContext.getDefaultEncoding();
                String charset = RuntimeContext.getProperty("edmi.gateway.default.charset", defaultCharset);
                template.body(requestBytes, Charset.forName(charset));
            }
        } catch (EncodeException e) {
            throw e;
        } catch (Exception e) {
            throw new EncodeException(e.getMessage(), e);
        }
    }

	@Override
	public boolean supported(Object object, Type bodyType, RequestTemplate template) {
		if (object instanceof EDMiRequestMessage request) {
			String systemId = request.getSystemId();
			return "host".equals(systemId);
		}
		return false;
	}
}
