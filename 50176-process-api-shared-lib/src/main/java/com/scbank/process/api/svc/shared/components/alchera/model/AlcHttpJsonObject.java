package com.scbank.process.api.svc.shared.components.alchera.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;

/**
 * 알체라 API 요청/응답 JSON Wrapper 클래스
 */
public class AlcHttpJsonObject {

	private JSONObject jsonObject;

	public AlcHttpJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public static AlcHttpJsonObject fromObject(String json) {
		try {
			JSONObject item = (JSONObject) new JSONObject(json);
			return new AlcHttpJsonObject(item);
		} catch (Exception e) {
			throw new PRCServiceException(e);
		}
	}

	public static AlcHttpJsonObject fromObject(JSONObject json) {
		return new AlcHttpJsonObject(json);
	}

	public Object getAsObject(String name, Object obj) {
		if (jsonObject.opt(name) == null) {
			return obj;
		}

		return jsonObject.opt(name);
	}

	public AlcHttpJsonObject getAsJsonObject(String name) {
		if (jsonObject.opt(name) == null) {
			return AlcHttpJsonObject.fromObject(new JSONObject());
		}

		return AlcHttpJsonObject.fromObject((JSONObject) jsonObject.optJSONObject(name));
	}

	public JSONArray getAsJsonArray(String name) {
		try {
			if (jsonObject.opt(name) == null)
				return new JSONArray();

			return (JSONArray) jsonObject.optJSONArray(name);
		} catch (Exception e) {
			return new JSONArray();
		}
	}

	public String getAsString(String name, String defaultValue) {
		if (jsonObject.opt(name) == null)
			return defaultValue;

		return StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue));
	}

	public JSONObject getAsJsonObject() {
		if (this.jsonObject == null)
			return new JSONObject();
		return this.jsonObject;
	}

	public String getAsJsonString() {
		return jsonObject.toString();
	}
}
