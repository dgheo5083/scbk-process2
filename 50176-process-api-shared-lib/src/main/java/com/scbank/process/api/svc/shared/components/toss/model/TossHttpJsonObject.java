package com.scbank.process.api.svc.shared.components.toss.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;


public class TossHttpJsonObject {

	private JSONObject jsonObject;

	public TossHttpJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public static TossHttpJsonObject fromObject(String json) {
		try {
			JSONObject item = (JSONObject) new JSONObject(json);
			return new TossHttpJsonObject(item);
		} catch (Exception e) {
			throw new PRCServiceException(e);
		}
	}

	public static TossHttpJsonObject fromObject(JSONObject json) {
		return new TossHttpJsonObject(json);
	}

	public Object getAsObject(String name, Object obj) {
		if (jsonObject.opt(name) == null) {
			return obj;
		}

		return jsonObject.opt(name);
	}

	public TossHttpJsonObject getAsJsonObject(String name) {
		if (jsonObject.opt(name) == null) {
			return TossHttpJsonObject.fromObject(new JSONObject());
		}

		return TossHttpJsonObject.fromObject(jsonObject.optJSONObject(name));
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

	public long getAsLong(String name, long defaultValue) {
		try {
			if (jsonObject.opt(name) == null)
				return defaultValue;

			return Long.parseLong(
					StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public int getAsInt(String name, int defaultValue) {
		try {
			if (jsonObject.opt(name) == null)
				return defaultValue;

			return Integer.parseInt(
					StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
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
