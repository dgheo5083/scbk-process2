package com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class EDMiMappingMessageEncoder implements IEDMiMessageEncoder {

    /**
     * 
     */
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

                Map<String, Object> messageEnvelope = EDMiMessageBuilder.builder()
                        .systemId(systemId)
                        .trackingId(trackingId)
                        .serviceBusId(interfaceId)
                        .captureSystem(captureSystem)
                        .messageTypeName(typeName)
                        .messageSenderBody(senderBody)
                        .messageSenderDomainBody(senderDomainBody)
                        .requestMessage(this.objectMapper.readValue((byte[])requestMessage, Map.class))
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
			return !"host".equals(systemId);
		}
		return false;
	}
}
