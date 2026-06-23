package com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;

/**
 * EDMi 전문 수신 메시지 디코더 구현 클래스
 * 
 * @author sungdon.choi
 */
public class EDMiFixedLengthMessageDecoder implements IEDMiMessageDecoder {
	
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) {
            return null;
        }

        try {
            byte[] responseMessageBytes = StreamUtils.copyToByteArray(response.body().asInputStream());
            return EDMiResponseMessage.builder()
                    .responseMessage(responseMessageBytes)
                    .build();
        } catch (DecodeException e) {
            throw e;
        } catch (FeignException e) {
            throw e;
        } catch (Exception e) {
            throw new DecodeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), response.request(), e);
        }
    }

	@Override
	public boolean supported(Response response, Type type) {
		FeignFilterContext ctx = FeignFilterContextHolder.get();
		if (ctx == null) {
			return false;
		}
		
		String systemId = ctx.getSystemId();
		return IntegrationConstant.SYSTEM_ID_HOST.equals(systemId);
	}
}
