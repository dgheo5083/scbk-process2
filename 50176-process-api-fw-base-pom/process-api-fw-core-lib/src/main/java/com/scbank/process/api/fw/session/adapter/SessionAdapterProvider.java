package com.scbank.process.api.fw.session.adapter;

import java.util.List;

/**
 * 세션 키에 따라 적절한 ISessionAdapter를 선택하여 wrap/unwrap을 위임하는 Provider
 */
public class SessionAdapterProvider implements ISessionAdapterProvider {

    private final List<ISessionAdapter> adapters;

    public SessionAdapterProvider(List<ISessionAdapter> adapters) {
        this.adapters = adapters;
    }

    @Override
    public Object wrap(String sessionKey, Object value) {
        ISessionAdapter sessionAdapter = findAdapter(sessionKey, value);
        return sessionAdapter != null ? sessionAdapter.wrap(sessionKey, value) : value;
    }

    @Override
    public Object unwrap(String sessionKey, Object value) {
        ISessionAdapter sessionAdapter = findAdapter(sessionKey, value);
        return sessionAdapter != null ? sessionAdapter.unwrap(sessionKey, value) : value;
    }

    /**
     * 세션 키와 값에 대해 supports()를 만족하는 어댑터를 찾아 반환한다.
     *
     * @param sessionKey 세션 키 (예: "ma30.fw.session", "ACCT_LIST")
     * @param value      세션에 저장된 실제 객체
     * @return 지원 가능한 어댑터가 있으면 반환, 없으면 null
     */
    private ISessionAdapter findAdapter(String sessionKey, Object value) {
        if (adapters == null)
            return null;
        return adapters.stream()
                .filter(adapter -> adapter.supports(sessionKey, value))
                .findFirst()
                .orElse(null);
    }
}
