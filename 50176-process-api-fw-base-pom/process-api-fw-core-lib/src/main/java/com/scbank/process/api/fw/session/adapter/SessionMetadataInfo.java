package com.scbank.process.api.fw.session.adapter;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 세션 키에 대한 v3/v4 타입 및 element 타입 정보 매핑
 */
@Data
public class SessionMetadataInfo {

    private String v3;
    private String v4;

    /** 리스트나 배열 요소 타입 매핑 */
    private ElementTypeMapping element;

    private List<Map<String, SessionMetadataInfo>> children;

    public SessionMetadataInfo getChild(String key) {
        if (children == null)
            return null;
        for (Map<String, SessionMetadataInfo> map : children) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    @Data
    public static class ElementTypeMapping {

        /** element의 v3 타입 (예: co.kr.ma30.session.Record) */
        private String v3;

        /** element의 v4 타입 (예: java.util.Map) */
        private String v4;
    }
}
