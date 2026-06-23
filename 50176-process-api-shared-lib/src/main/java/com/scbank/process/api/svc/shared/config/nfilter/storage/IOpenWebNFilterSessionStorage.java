package com.scbank.process.api.svc.shared.config.nfilter.storage;

import java.util.Map;

/**
 * 
 */
public interface IOpenWebNFilterSessionStorage {
	
	/**
	 * 
	 * @param key
	 * @param sessionData
	 */
	void save(String key, Map<String, Object> sessionData);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	Map<String, Object> read(String key);
}
