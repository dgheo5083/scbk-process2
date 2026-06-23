package com.scbank.process.api.svc.shared.components.obs.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * 금결원 오픈뱅킹 JSON 데이터 Wrapped Object
 */
public class ObsHttpJsonObject {

    private JSONObject jsonObject;

    /**
     * 생성자
     * @param jsonObject
     */
    public ObsHttpJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * 
     * @param json
     * @return
     */
    public static ObsHttpJsonObject fromObject(String json) {
        try {
            JSONObject item = new JSONObject(json);
            return new ObsHttpJsonObject(item);
        } catch (Exception e) {
            throw new PRCServiceException(e);
        }
    }

    /**
     * 
     * @param json
     * @return
     */
    public static ObsHttpJsonObject fromObject(JSONObject json) {
        return new ObsHttpJsonObject(json);
    }

    /**
     * 
     * @param name
     * @param obj
     * @return
     */
    public Object getAsObject(String name, Object obj) {
        if (jsonObject.get(name) == null) {
            return obj;
        }

        return jsonObject.get(name);
    }

    /**
     * 
     * @param name
     * @return
     */
    public ObsHttpJsonObject getAsJsonObject(String name) {
        if (jsonObject.get(name) == null) {
            return ObsHttpJsonObject.fromObject(new JSONObject());
        }

        return ObsHttpJsonObject.fromObject(jsonObject.optJSONObject(name));
    }

    /**
     * 
     * @param name
     * @return
     */
    public JSONArray getAsJsonArray(String name) {
        try {
            if (jsonObject.opt(name) == null)
                return new JSONArray();

            return (JSONArray) jsonObject.optJSONArray(name);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    /**
     * 
     * @param name
     * @param defaultValue
     * @return
     */
    public String getAsString(String name, String defaultValue) {
    	try {
    		if (jsonObject.opt(name) == null)
                return defaultValue;

            return StringUtils.defaultIfBlank(jsonObject.opt(name).toString(), String.valueOf(defaultValue));
    	} catch (Exception e) {
    		return defaultValue;
    	}
    }

    /**
     * 
     * @param name
     * @param defaultValue
     * @return
     */
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

    /**
     * 
     * @param name
     * @param defaultValue
     * @return
     */
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

    /**
     * 
     * @return
     */
    public JSONObject getAsJsonObject() {
        if (this.jsonObject == null)
            return new JSONObject();
        return this.jsonObject;
    }

    /**
     * 
     * @return
     */
    public String getAsJsonString() {
        return jsonObject.toString();
    }
}
