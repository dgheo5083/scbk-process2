package com.scbank.process.api.fw.session.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.impl.DefaultSessionContext;
import com.scbank.process.api.fw.session.impl.GlobalSessionObject;
import com.scbank.process.api.fw.session.impl.LoginSessionData;
import com.scbank.process.api.fw.session.impl.LoginSessionObject;

public class SessionContextRevokeAdapter extends AbstractSessionAdapter {

    public SessionContextRevokeAdapter(ISessionMetadataRegistry metadataRegistry) {
        super(metadataRegistry);
    }

    @Override
    public boolean supports(String sessionKey, Object value) {
        return ISessionContext.SESSION_ATTR_KEY.equals(sessionKey);
    }

    @Override
    protected SessionMetadataInfo resolveMetadata(String sessionKey, Object value) {
        return metadataRegistry.getMetadata(ISessionContext.SESSION_ATTR_KEY).orElse(null);
    }

    @Override
    public Object wrap(String sessionKey, Object value) {
        if (!(value instanceof ISessionContext))
            return value;

        DefaultSessionContext session = (DefaultSessionContext) value;
        SessionMetadataInfo meta = resolveMetadata(sessionKey, value);
        if (meta == null)
            return value;

        Map<String, Object> wrapped = new LinkedHashMap<>();

        Map<String, String> adapterMeta = new HashMap<>();
        adapterMeta.put("v3", meta.getV3());
        adapterMeta.put("v4", meta.getV4());
        wrapped.put("_adapter_", adapterMeta);

        SessionMetadataInfo loginMeta = meta.getChild("loginSession");
        SessionMetadataInfo globalMeta = meta.getChild("globalSession");

        wrapped.put("loginSession", wrapRecursive(session.getLoginSession(), loginMeta));
        wrapped.put("globalSession", wrapRecursive(session.getGlobalSession(), globalMeta));

        wrapped.put("clientIp", session.getClientIp());
        wrapped.put("sessionId", session.getSessionId());
        wrapped.put("logined", session.isLogined());

        return wrapped;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object unwrap(String sessionKey, Object value) {
        if (!(value instanceof Map))
            return value;

        SessionMetadataInfo meta = resolveMetadata(sessionKey, value);
        if (meta == null)
            return value;

        Map<String, Object> raw = new LinkedHashMap((Map<?, ?>) value);
        raw.remove("_adapter_");
        raw.remove("@class");

        LoginSessionObject loginSession = unwrapLoginSession(raw.get("loginSession"), meta.getChild("loginSession"));
        GlobalSessionObject globalSession = unwrapGlobalSession(raw.get("globalSession"),
                meta.getChild("globalSession"));

        DefaultSessionContext session = new DefaultSessionContext();
        session.setLoginSession(loginSession);
        session.setGlobalSession(globalSession);
        session.setClientIp((String) raw.get("clientIp"));
        session.setSessionId((String) raw.get("sessionId"));

        return session;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private LoginSessionObject unwrapLoginSession(Object raw, SessionMetadataInfo meta) {
        if (!(raw instanceof Map))
            return null;
        Map<String, Object> map = new LinkedHashMap((Map<?, ?>) raw);
        map.remove("_adapter_");
        map.remove("@class");

        LoginSessionObject loginSession = new LoginSessionObject();
        Map<String, SessionMetadataInfo> children = flattenChildren(meta.getChildren());

        for (Map.Entry<String, SessionMetadataInfo> entry : children.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.get(key);
            Object restored = unwrapRecursive(fieldValue, entry.getValue());

            if ("loginData".equals(key)) {
                LoginSessionData loginData = unwrapLoginData(fieldValue, entry.getValue());
                loginSession.setLoginData(loginData);
            } else {
                loginSession.put(key, (Serializable) restored);
            }
        }

        return loginSession;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private LoginSessionData unwrapLoginData(Object raw, SessionMetadataInfo meta) {
        if (!(raw instanceof Map))
            return null;
        Map<String, Object> map = new LinkedHashMap((Map<?, ?>) raw);
        map.remove("_adapter_");
        map.remove("@class");

        LoginSessionData loginData = new LoginSessionData();
        Map<String, SessionMetadataInfo> children = flattenChildren(meta.getChildren());

        for (Map.Entry<String, SessionMetadataInfo> entry : children.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.get(key);

            if (fieldValue == null) {
                continue;
            }

            Object restored = unwrapRecursive(fieldValue, entry.getValue());
            // LoginData 내부도 setter or put 활용
            loginData.put(key, (Serializable) restored);
        }

        return loginData;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private GlobalSessionObject unwrapGlobalSession(Object raw, SessionMetadataInfo meta) {
        if (!(raw instanceof Map))
            return null;
        Map<String, Object> map = new LinkedHashMap((Map<?, ?>) raw);
        map.remove("_adapter_");
        map.remove("@class");

        GlobalSessionObject globalSession = new GlobalSessionObject();
        Map<String, SessionMetadataInfo> children = flattenChildren(meta.getChildren());

        for (Map.Entry<String, SessionMetadataInfo> entry : children.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.get(key);
            Object restored = unwrapRecursive(fieldValue, entry.getValue());
            globalSession.put(key, (Serializable) restored);
        }

        return globalSession;
    }

    private Map<String, SessionMetadataInfo> flattenChildren(List<Map<String, SessionMetadataInfo>> childrenList) {
        Map<String, SessionMetadataInfo> flat = new LinkedHashMap<>();
        for (Map<String, SessionMetadataInfo> childMap : childrenList) {
            flat.putAll(childMap);
        }
        return flat;
    }
}
