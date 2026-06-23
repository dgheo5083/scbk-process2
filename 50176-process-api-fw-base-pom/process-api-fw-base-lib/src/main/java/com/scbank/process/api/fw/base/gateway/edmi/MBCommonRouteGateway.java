package com.scbank.process.api.fw.base.gateway.edmi;

import java.util.List;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.gateway.EDMIGateway;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MBCommonRouteGateway implements EDMIGateway {

	private final MBCommonRoute feignClient;

	@Override
	public List<String> getSupportedTypeNames() {
		return List.of("Risk:mbcommonroute");
	}
	
	@Override
	public EDMiResponseMessage send(EDMiRequestMessage request) {
		return this.feignClient.send(request);
	}
}
