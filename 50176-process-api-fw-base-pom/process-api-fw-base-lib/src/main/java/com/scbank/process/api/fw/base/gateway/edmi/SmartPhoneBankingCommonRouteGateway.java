package com.scbank.process.api.fw.base.gateway.edmi;

import java.util.List;

import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.gateway.EDMIGateway;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//@Component
public class SmartPhoneBankingCommonRouteGateway implements EDMIGateway {

	private final SmartPhoneBankingCommonRoute feignClient;
	
//	@Override
//	public List<CaptureSystem> getSupportedCaptureSystem() {
//		return List.of(CaptureSystem.CSL_OLTP, CaptureSystem.CSL_AMS);
//	}

	@Override
	public EDMiResponseMessage send(EDMiRequestMessage request) {
		return feignClient.send(request);
	}

	@Override
	public List<String> getSupportedTypeNames() {
		return List.of("CoreBanking:smartPhoneBankingCommonRoute");
	}
}
