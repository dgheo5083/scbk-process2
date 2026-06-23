package com.scbank.process.api.svc.shared.config.nfilter.storage.impl;

import java.util.HashMap;
import java.util.Map;

import com.scbank.process.api.svc.shared.config.nfilter.storage.IOpenWebNFilterSessionStorage;

public class DefaultOpenWebNFilterSessionStorage implements IOpenWebNFilterSessionStorage {

	private Map<String, Object> storage = new HashMap<>();
	
	@Override
	public void save(String key, Map<String, Object> sessionData) {
		storage.put(key, sessionData);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> read(String key) {
		if (!storage.containsKey(key)) {
			return null;
		}
		return (Map<String, Object>) storage.get(key);
	}
}
