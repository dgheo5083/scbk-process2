package com.scbank.process.api.svc.shared.components.mydata.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;

public class MyDataHttpJsonObject {

	private JSONObject jsonObject;

	public MyDataHttpJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public static MyDataHttpJsonObject fromObject(String json) {
		try {
			JSONObject item = (JSONObject) new JSONObject(json);
			return new MyDataHttpJsonObject(item);
		} catch (Exception e) {
			throw new PRCServiceException(e);
		}
	}

	public static MyDataHttpJsonObject fromObject(JSONObject json) {
		return new MyDataHttpJsonObject(json);
	}

	public Object getAsObject(String name, Object obj) {
		if (jsonObject.opt(name) == null) {
			return obj;
		}

		return jsonObject.opt(name);
	}

	public MyDataHttpJsonObject getAsJsonObject(String name) {
		if (jsonObject.opt(name) == null) {
			return MyDataHttpJsonObject.fromObject(new JSONObject());
		}

		return MyDataHttpJsonObject.fromObject((JSONObject) jsonObject.optJSONObject(name));
	}

	public JSONArray getAsJsonArray(String name) {
		try {
			if (jsonObject.get(name) == null)
				return new JSONArray();

			return jsonObject.optJSONArray(name);
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
			if (jsonObject.get(name) == null)
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
