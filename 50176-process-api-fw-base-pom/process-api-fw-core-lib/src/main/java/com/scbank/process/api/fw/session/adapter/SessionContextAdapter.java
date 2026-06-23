package com.scbank.process.api.fw.session.adapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.impl.DefaultSessionContext;
import com.scbank.process.api.fw.session.impl.GlobalSessionObject;
import com.scbank.process.api.fw.session.impl.LoginSessionData;
import com.scbank.process.api.fw.session.impl.LoginSessionObject;

/**
 * 
 */
public class SessionContextAdapter extends AbstractSessionAdapter {

    private static final String MA30_SESSION_ATTR_KEY = ISessionContext.SESSION_ATTR_KEY;
    private static final String GLOBAL_SESSION_ATTR_KEY = SessionAdapterConstants.GLOBAL_SESSION_ATTR_KEY;
    private static final String LOGIN_SESSION_ATTR_KEY = SessionAdapterConstants.LOGIN_SESSION_ATTR_KEY;

    public SessionContextAdapter(ISessionMetadataRegistry metadataRegistry, ObjectMapper objectMapper) {
        super(metadataRegistry, objectMapper);
    }

    @Override
    public boolean supports(String sessionKey, Object value) {
        return MA30_SESSION_ATTR_KEY.equals(sessionKey);
    }

    @Override
    protected SessionMetadataInfo resolveMetadata(String sessionKey, Object value) {
        return metadataRegistry.getMetadata(MA30_SESSION_ATTR_KEY).orElse(null);
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
        adapterMeta.put(SessionAdapterConstants.FLD_NM_VERSION_3, meta.getV3());
        adapterMeta.put(SessionAdapterConstants.FLD_NM_VERSION_4, meta.getV4());
        wrapped.put(SessionAdapterConstants.FLD_NM_ADAPTER, adapterMeta);

        SessionMetadataInfo loginMeta = meta.getChild(LOGIN_SESSION_ATTR_KEY);
        SessionMetadataInfo globalMeta = meta.getChild(GLOBAL_SESSION_ATTR_KEY);

        wrapped.put(LOGIN_SESSION_ATTR_KEY, wrapRecursive(session.getLoginSession(), loginMeta));
        wrapped.put(GLOBAL_SESSION_ATTR_KEY, wrapRecursive(session.getGlobalSession(), globalMeta));

        wrapped.put(SessionAdapterConstants.FLD_NM_CLIENT_IP, session.getClientIp());
        wrapped.put(SessionAdapterConstants.FLD_NM_SESSION_ID, session.getSessionId());
        wrapped.put(SessionAdapterConstants.FLD_NM_LOGINED, session.isLogined());

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
        raw.remove(SessionAdapterConstants.FLD_NM_ADAPTER);
        //raw.remove(SessionAdapterConstants.FLD_NM_CLASS);

        LoginSessionObject loginSession = unwrapLoginSession(raw.get(LOGIN_SESSION_ATTR_KEY),
                meta.getChild(LOGIN_SESSION_ATTR_KEY));
        GlobalSessionObject globalSession = unwrapGlobalSession(raw.get(GLOBAL_SESSION_ATTR_KEY),
                meta.getChild(GLOBAL_SESSION_ATTR_KEY));

        DefaultSessionContext session = new DefaultSessionContext();
        session.setLoginSession(loginSession);
        session.setGlobalSession(globalSession);
        session.setClientIp((String) raw.get(SessionAdapterConstants.FLD_NM_CLIENT_IP));
        session.setSessionId((String) raw.get(SessionAdapterConstants.FLD_NM_SESSION_ID));

        return session;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private LoginSessionObject unwrapLoginSession(Object raw, SessionMetadataInfo meta) {
        if (!(raw instanceof Map))
            return null;
        Map<String, Object> map = new LinkedHashMap((Map<?, ?>) raw);
        map.remove(SessionAdapterConstants.FLD_NM_ADAPTER);
        //map.remove(SessionAdapterConstants.FLD_NM_CLASS);

        LoginSessionObject loginSession = new LoginSessionObject();
        Map<String, SessionMetadataInfo> children = flattenChildren(meta.getChildren());

        for (Map.Entry<String, SessionMetadataInfo> entry : children.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.remove(key);
            Object restored = unwrapRecursive(fieldValue, entry.getValue());

            if (SessionAdapterConstants.FLD_NM_LOGIN_DATA.equals(key)) {
                LoginSessionData loginData = unwrapLoginData(fieldValue, entry.getValue());
                loginSession.setLoginData(loginData);
            } else {
                if (restored != null) {
                    loginSession.put(key, restored);
                }
            }
        }

        if (CollectionUtils.isEmpty(map)) {
            return loginSession;
        }

        // 메타데이터에 등록되지 않는 나머지 값도 복원 시킨다.
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.get(key);

            if (fieldValue == null) {
                continue;
            }

            //
            if (SessionAdapterConstants.FLD_NM_LOGINED.equals(key)) {
                loginSession.setLogined((Boolean) fieldValue);
            } else if (SessionAdapterConstants.FLD_NM_USERID.equals(key)) {
                loginSession.setUserId((String) fieldValue);
            } else {
                loginSession.put(key, fieldValue);
            }
        }

        return loginSession;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private LoginSessionData unwrapLoginData(Object raw, SessionMetadataInfo meta) {
        if (!(raw instanceof Map)) {
            return null;
        }

        Map<String, Object> map = new LinkedHashMap((Map<?, ?>) raw);
        map.remove(SessionAdapterConstants.FLD_NM_ADAPTER);
        //map.remove(SessionAdapterConstants.FLD_NM_CLASS);

        LoginSessionData loginData = new LoginSessionData();
        Map<String, SessionMetadataInfo> children = flattenChildren(meta.getChildren());

        for (Map.Entry<String, SessionMetadataInfo> entry : children.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.remove(key);

            if (fieldValue == null) {
                continue;
            }

            Object restored = unwrapRecursive(fieldValue, entry.getValue());
            // LoginData 내부도 setter or put 활용
            loginData.put(key, restored);
        }

        if (CollectionUtils.isEmpty(map)) {
            return loginData;
        }

        // 메타데이터에 등록되지 않는 나머지 값도 복원 시킨다.
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.get(key);

            if (fieldValue == null) {
                continue;
            }

            loginData.put(key, fieldValue);
        }

        return loginData;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private GlobalSessionObject unwrapGlobalSession(Object raw, SessionMetadataInfo meta) {
        if (!(raw instanceof Map))
            return null;
        Map<String, Object> map = new LinkedHashMap((Map<?, ?>) raw);
        map.remove(SessionAdapterConstants.FLD_NM_ADAPTER);
        //map.remove(SessionAdapterConstants.FLD_NM_CLASS);

        GlobalSessionObject globalSession = new GlobalSessionObject();
        Map<String, SessionMetadataInfo> children = flattenChildren(meta.getChildren());

        for (Map.Entry<String, SessionMetadataInfo> entry : children.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.remove(key);
            Object restored = unwrapRecursive(fieldValue, entry.getValue());
            if (restored != null) {
                globalSession.put(key, restored);
            }
        }

        if (CollectionUtils.isEmpty(map)) {
            return globalSession;
        }

        // 메타데이터에 등록되지 않는 나머지 값도 복원 시킨다.
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object fieldValue = map.get(key);

            if (fieldValue == null) {
                continue;
            }

            // LoginData 내부도 setter or put 활용
            globalSession.put(key, fieldValue);
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
