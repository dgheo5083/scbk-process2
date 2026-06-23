package com.scbank.process.api.fw.session.adapter;

import com.scbank.process.api.fw.session.ISessionContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 세션 외 변환 세션 어댑터 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
public class GenericSessionAdapter extends AbstractSessionAdapter {

    public GenericSessionAdapter(ISessionMetadataRegistry metadataRegistry) {
        super(metadataRegistry);
    }

    @Override
    public boolean supports(String sessionKey, Object value) {
        return !ISessionContext.SESSION_ATTR_KEY.equals(sessionKey);
    }

    @Override
    protected SessionMetadataInfo resolveMetadata(String sessionKey, Object value) {
        return metadataRegistry.getMetadata(sessionKey).orElse(null);
    }
    
    
    @Override
    public Object wrap(String sessionKey, Object value) {
    	return super.wrap(sessionKey, value);
    }
    
    @Override
    public Object unwrap(String sessionKey, Object value) {
        SessionMetadataInfo meta = resolveMetadata(sessionKey, value);
        if (value == null || meta == null) {
        	return value;
        }
        
        Object unwraped = unwrapRecursive(value, meta);
        try {
        	Object result = objectMapper.convertValue(unwrapRecursive(value, meta), Class.forName(meta.getV4()));
        	return result;
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
        return unwraped;
    }
}
