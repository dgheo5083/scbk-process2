package com.scbank.process.api.fw.base.gateway.edmi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.gateway.edmi.base.gateway.EDMIGateway;

/**
 * 
 */
@Component
public class EDMIGatewayRegistry {

	private Map<String, EDMIGateway> registry = new HashMap<>();

	/**
	 * 
	 * @param gateways
	 */
	public EDMIGatewayRegistry(List<EDMIGateway> gateways) {
		for (EDMIGateway gateway: gateways) {
			List<String> supported = gateway.getSupportedTypeNames();
			
			if (supported == null || supported.isEmpty()) {
				throw new IllegalStateException("EDMIGateway hgs no supported capture system:" + gateway.getClass().getName());
			}
			
			for (String typeName: supported) {
				EDMIGateway prev = registry.put(typeName, gateway);
				if (prev != null) {
					throw new IllegalStateException("Dupilcated EDMIGateway mapping for typeName:" + typeName 
							+ " prev=" + prev.getClass().getName()
							+ " new=" + gateway.getClass().getName());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param system
	 * @return
	 */
	public EDMIGateway resolve(String typeName) {
		EDMIGateway gateway = registry.get(typeName);
		if (gateway == null) {
			throw new IllegalArgumentException("No EDMIGateway  registered for typeName=" + typeName);
		}
		return gateway;
	}
}