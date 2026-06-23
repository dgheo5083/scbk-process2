package com.scbank.process.api.svc.shared.components.frs.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;


/**
 * 금결원 안면인식 JSON 데이터 Wrapped Object
 */
public class FrsHttpJsonObject {
	
	private JSONObject jsonObject;
	
	public FrsHttpJsonObject (JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public static FrsHttpJsonObject fromObject(String json) {
		try {
			JSONObject item = new JSONObject(json);
			return new FrsHttpJsonObject(item);
		} catch (JSONException e) {
            throw new PRCServiceException(e);
        }
	}
	
	public static FrsHttpJsonObject fromObject(JSONObject json) {
		return new FrsHttpJsonObject(json);
	}

	public Object getAsObject(String name, Object obj) {
		if (!jsonObject.has(name) ||jsonObject.opt(name) == null) {
			return obj;
		}
		
		return jsonObject.opt(name);
	}
		
	public FrsHttpJsonObject getAsJsonObject(String name) {
		if (!jsonObject.has(name) ||jsonObject.opt(name) == null) {
			return FrsHttpJsonObject.fromObject(new JSONObject());
		}
		
		return FrsHttpJsonObject.fromObject((JSONObject)jsonObject.optJSONObject(name));
	}
	
	public JSONArray getAsJsonArray(String name) {
		try {
			if (jsonObject.opt(name) == null) return new JSONArray();
			
			return (JSONArray)jsonObject.optJSONArray(name);
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	public String getAsString(String name, String defaultValue) {
		if (!jsonObject.has(name) || jsonObject.opt(name) == null) return defaultValue;
		
		return StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue));
	}
	
	public long getAsLong(String name, long defaultValue) {
		try {
			if (jsonObject.opt(name) == null) return defaultValue;
			
			return Long.parseLong(StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public int getAsInt(String name, int defaultValue) {
		try {
			if (jsonObject.opt(name) == null) return defaultValue;
			
			return Integer.parseInt(StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public JSONObject getAsJsonObject() {
		if (this.jsonObject == null) return new JSONObject();
		return this.jsonObject;
	}
	
	public String getAsJsonString() {
		return jsonObject.toString();
	}
}
