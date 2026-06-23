package com.scbank.process.api.fw.session.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 세션 데이터를 메타정보 기반으로 wrap/unwrap 처리하는 추상 어댑터.
 *
 * <p>
 * 역할
 * </p>
 * <ul>
 * <li>세션 저장 전: {@link #wrap(String, Object)}로 값에 메타 삽입/필드별 재귀 변환</li>
 * <li>세션 조회 후: {@link #unwrap(String, Object)}로 메타 제거/타입 복원</li>
 * <li>리스트/요소 타입, 자식 필드 메타까지 포함한 재귀 처리</li>
 * </ul>
 *
 * <p>
 * 구현 가이드
 * </p>
 * <ul>
 * <li>{@link #resolveMetadata(String, Object)}에서 주어진 sessionKey/value에 맞는
 * {@link SessionMetadataInfo} 제공</li>
 * <li>ObjectMapper 기반 Map ↔ POJO 변환 사용 (필드명 매핑 전략 유의)</li>
 * </ul>
 */
@Slf4j
public abstract class AbstractSessionAdapter implements ISessionAdapter {

    /** 메타 정보에서 v3 타입(또는 클래스명) 필드 키 */
    protected static final String FLD_NM_V3 = SessionAdapterConstants.FLD_NM_VERSION_3;
    /** 메타 정보에서 v4 타입(또는 클래스명) 필드 키 */
    protected static final String FLD_NM_V4 = SessionAdapterConstants.FLD_NM_VERSION_4;
    /** 어댑터 메타 정보를 담는 공통 키 */
    protected static final String FLD_NM_ADAPTER = SessionAdapterConstants.FLD_NM_ADAPTER;
    /** Jackson activateDefaultTyping 등으로 삽입되는 클래스 키 */
    protected static final String FLD_NM_CLASS = SessionAdapterConstants.FLD_NM_CLASS;

    /** 세션 키별 v3/v4 타입 및 자식/요소 메타 정보를 제공하는 레지스트리 */
    protected final ISessionMetadataRegistry metadataRegistry;
    /** Map ↔ POJO 변환을 위한 ObjectMapper */
    protected final ObjectMapper objectMapper;

    protected AbstractSessionAdapter(ISessionMetadataRegistry metadataRegistry) {
        this(metadataRegistry, new ObjectMapper());
    }

    protected AbstractSessionAdapter(ISessionMetadataRegistry metadataRegistry, ObjectMapper objectMapper) {
        this.metadataRegistry = metadataRegistry;
        this.objectMapper = objectMapper.copy();
    }

    /**
     * sessionKey/value에 대한 메타 정보 해석.
     * <p>
     * 구현체에서 sessionKey, value의 형태(Map/리스트 등)를 감안해
     * 어떤 {@link SessionMetadataInfo}를 적용할지 결정한다.
     * </p>
     */
    protected abstract SessionMetadataInfo resolveMetadata(String sessionKey, Object value);

    @Override
    public Object wrap(String sessionKey, Object value) {
        SessionMetadataInfo meta = resolveMetadata(sessionKey, value);
        if (value == null || meta == null)
            return value;
        return wrapRecursive(value, meta);
    }

    @Override
    public Object unwrap(String sessionKey, Object value) {
        SessionMetadataInfo meta = resolveMetadata(sessionKey, value);
        if (value == null || meta == null)
            return value;
        return unwrapRecursive(value, meta);
    }

    /**
     * 값에 메타를 삽입하고 자식/요소에 대해 재귀적으로 wrap.
     * <ul>
     * <li>List: 요소 메타가 있으면 요소별로 래핑</li>
     * <li>Map/POJO: Map으로 변환 후 _adapter_ 메타 부착 + 자식 필드 재귀 처리</li>
     * </ul>
     */
    @SuppressWarnings({ "unchecked" })
    protected Object wrapRecursive(Object value, SessionMetadataInfo meta) {
        if (value == null)
            return null;

        // 1) 리스트 처리
        if (value instanceof List<?>) {
            if (meta.getElement() != null) {
                List<Object> result = new ArrayList<>();
                for (Object item : (List<?>) value) {
                    result.add(wrapListElement(item, meta.getElement())); // 요소 별 메타 삽입
                }
                return result;
            }
            // 요소 메타 없으면 원본 유지
            return value;
        }

        Map<String, Object> valueMap = null;
        if (value instanceof Map) {
            valueMap = (Map<String, Object>) value;
        } else {
            try {
                String json = objectMapper.writeValueAsString(value);
                valueMap = objectMapper.readValue(json, Map.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }

        }

        // _adapter_ 메타 삽입: 현재 객체의 v3/v4 타입 정보 기록
        Map<String, Object> wrapped = new LinkedHashMap<>();
        Map<String, String> adapterMeta = new HashMap<>();
        adapterMeta.put(FLD_NM_V3, meta.getV3());
        adapterMeta.put(FLD_NM_V4, meta.getV4());
        wrapped.put(FLD_NM_ADAPTER, adapterMeta);

        if (valueMap == null) {
            return wrapped;
        }
        // 자식 필드 재귀 처리 (자식 메타가 있으면 wrapRecursive 적용)
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            String field = entry.getKey();
            Object fieldValue = entry.getValue();
            SessionMetadataInfo childMeta = meta.getChild(field);
            wrapped.put(field, childMeta != null ? wrapRecursive(fieldValue, childMeta) : fieldValue);
        }

        return wrapped;
    }

    /**
     * 리스트 요소 래핑: 요소 객체를 Map으로 변환 후 _adapter_ 메타만 추가하여 반환.
     */
    protected Object wrapListElement(Object item, SessionMetadataInfo.ElementTypeMapping elementMeta) {
        if (item == null)
            return null;

        Map<String, Object> wrapped = new LinkedHashMap<>();
        Map<String, String> adapterMeta = new HashMap<>();
        adapterMeta.put(FLD_NM_V3, elementMeta.getV3());
        adapterMeta.put(FLD_NM_V4, elementMeta.getV4());
        wrapped.put(FLD_NM_ADAPTER, adapterMeta);

        Map<String, Object> itemMap = toObjectMap(item);
        wrapped.putAll(itemMap);
        return wrapped;
    }
    
    /**
     * 
     * @param item
     * @return
     */
    @SuppressWarnings("unchecked")
	private Map<String, Object> toObjectMap(Object item) {
    	try {
    		if (item instanceof Map) {
    			return (Map<String, Object>) item;
    		}
    		
    		return objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {
            });
    	} catch (Exception e) {
    		throw new IllegalArgumentException(e);
    	}
    }

    /**
     * 값에서 메타 제거 및 타입 복원(재귀).
     * <ul>
     * <li>List: 요소 메타를 임시 SessionMetadataInfo로 만들어 요소별로 재귀 unwrap</li>
     * <li>Map: _adapter_ / @class 제거 후 자식 필드 재귀 처리, 필요 시 클래스 변환</li>
     * </ul>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object unwrapRecursive(Object value, SessionMetadataInfo meta) {
        if (value == null || meta == null)
            return value;

        // 1) 리스트 처리
        if (value instanceof List<?>) {
            List<Object> list = (List<Object>) value;
            List<Object> unwrapped = new ArrayList<>();

            // 요소/리스트의 v4 클래스명 (메타 기준)
            String elementV4ClassName = meta.getElement() != null ? meta.getElement().getV4() : null;
            String listV4ClassName = meta.getV4();

            for (Object item : list) {
                // 요소용 임시 메타 생성 (자식 필드 처리용)
                SessionMetadataInfo elementMeta = meta.getElement() != null ? new SessionMetadataInfo() {
                    {
                        setV3(meta.getElement().getV3());
                        setV4(meta.getElement().getV4());
                    }
                } : null;

                Object unwrappedItem = unwrapRecursive(item, elementMeta);

                // 요소를 특정 클래스(예: v4 요소 클래스)로 변환
                if (unwrappedItem instanceof Map && elementV4ClassName != null) {
                    try {
                        // 아마 요소 클래스는 elementV4ClassName을 사용해야 할 가능성이 큼.
                        Class<?> v3ElementClass = Class.forName(listV4ClassName); // <-- 의도 확인 필요
                        unwrappedItem = objectMapper.convertValue(unwrappedItem, v3ElementClass);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }

                unwrapped.add(unwrappedItem);
            }

            // 리스트 자체의 구체 타입으로 변환 (예: ArrayList → 특정 Collection 구현)
            if (listV4ClassName != null) {
                try {
                    Object resultList = convertList(unwrapped, listV4ClassName, elementV4ClassName);
                    return resultList;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            return unwrapped;
        }

        // 2) Map 처리
        if (value instanceof Map) {
            Map<String, Object> map = new LinkedHashMap((Map<?, ?>) value);

            // _adapter_ 메타 추출 및 제거
            Map<String, String> adapterMeta = extractAdapterMeta(map);
            map.remove(FLD_NM_ADAPTER);
            //map.remove(FLD_NM_CLASS);

            // 자식 필드 재귀 처리
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String field = entry.getKey();
                Object fieldValue = entry.getValue();
                SessionMetadataInfo childMeta = meta.getChild(field);
                map.put(field, unwrapRecursive(fieldValue, childMeta));
            }

            // _adapter_에서 v4 클래스명을 읽어 객체로 복원
            if (adapterMeta != null && adapterMeta.containsKey(FLD_NM_V4)) {
                try {
                    String v4ClassName = adapterMeta.get(FLD_NM_V4);
                    Class<?> v4Class = Class.forName(v4ClassName); // <-- 변수명 정리 필요
                    Object result = objectMapper.convertValue(map, v4Class);
                    return result;
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }

            // 메타가 없거나 변환 실패 시 Map 반환
            return map;
        }

        // 3) 그 외 타입은 그대로 반환
        return value;
    }

    /**
     * _adapter_ 메타(Map)를 추출.
     * 없으면 null 반환.
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> extractAdapterMeta(Map<?, ?> map) {
        if (map.containsKey(FLD_NM_ADAPTER)) {
            Object adapter = map.get(FLD_NM_ADAPTER);
            if (adapter instanceof Map) {
                return (Map<String, String>) adapter;
            }
        }
        return null;
    }

    /**
     * 리스트를 지정한 구현체 타입으로 생성하고, 요소를 지정한 클래스 타입으로 변환하여 추가.
     *
     * @param rawList          원본 리스트(언래핑 결과)
     * @param listClassName    리스트 구현체 클래스명 (예: "java.util.ArrayList" 또는 커스텀
     *                         Collection)
     * @param elementClassName 요소 클래스명 (POJO 등). null이면 변환 생략.
     * @return 변환된 리스트 인스턴스(실패 시 원본 rawList 반환)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object convertList(List<?> rawList, String listClassName, String elementClassName) {
        try {
            Class<?> listClass = Class.forName(listClassName, true, Thread.currentThread().getContextClassLoader());
            Class<?> elementClass = Class.forName(elementClassName, true,
                    Thread.currentThread().getContextClassLoader());

            Collection result = (Collection) listClass.getDeclaredConstructor().newInstance();
            for (Object item : rawList) {
                Object converted = (item instanceof Map)
                        ? objectMapper.convertValue(item, elementClass)
                        : item;
                result.add(converted);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return rawList; // 실패 시 원본 유지 (fallback)
        }
    }
}
