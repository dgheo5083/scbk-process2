package com.scbank.process.api.fw.base.gateway.edmi;

import java.util.List;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.gateway.EDMIGateway;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MBOltpCommonRouteGateway implements EDMIGateway {

	private final MBOltpCommonRoute feignClient;
	
//	@Override
//	public List<CaptureSystem> getSupportedCaptureSystem() {
//		return List.of(CaptureSystem.OLTP_HEX_STRING, CaptureSystem.VMS);
//	}

	@Override
	public EDMiResponseMessage send(EDMiRequestMessage request) {
		return this.feignClient.send(request);
	}

	@Override
	public List<String> getSupportedTypeNames() {
		return List.of("CoreBanking:mbOLTPCommonRoute");
	}
}
