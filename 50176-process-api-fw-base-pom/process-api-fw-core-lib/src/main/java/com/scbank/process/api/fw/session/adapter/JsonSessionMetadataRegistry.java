package com.scbank.process.api.fw.session.adapter;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSessionMetadataRegistry implements ISessionMetadataRegistry {

    private final Map<String, SessionMetadataInfo> metadataMap;

    public JsonSessionMetadataRegistry(InputStream jsonInputStream) {
        this.metadataMap = loadMetadata(jsonInputStream);
    }

    private Map<String, SessionMetadataInfo> loadMetadata(InputStream is) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, SessionMetadataInfo>> typeRef = new TypeReference<Map<String, SessionMetadataInfo>>() {
            };
            return mapper.readValue(is, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("세션 메타데이터 로딩 실패", e);
        }
    }

    @Override
    public Optional<SessionMetadataInfo> getMetadata(String sessionKey) {
        return Optional.ofNullable(metadataMap.get(sessionKey));
    }

    @Override
    public Optional<String> getV3Type(String sessionKey) {
        return getMetadata(sessionKey).map(SessionMetadataInfo::getV3);
    }

    @Override
    public Optional<String> getV4Type(String sessionKey) {
        return getMetadata(sessionKey).map(SessionMetadataInfo::getV4);
    }

    @Override
    public Optional<SessionMetadataInfo.ElementTypeMapping> getElementType(String sessionKey) {
        return getMetadata(sessionKey).map(SessionMetadataInfo::getElement);
    }
}
