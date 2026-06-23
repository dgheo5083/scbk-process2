package com.scbank.process.api.fw.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionUtils {

    private ReflectionUtils() {

    }

    /**
     * 지정된 객체에서 특정 필드 값을 가져옵니다.
     *
     * @param target    대상 객체
     * @param fieldName 필드명
     * @param <T>       기대하는 필드 타입
     * @return 필드 값 (없거나 접근 불가 시 null)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object target, String fieldName) {
        if (target == null || fieldName == null)
            return null;
        try {
            Field field = findField(target.getClass(), fieldName);
            if (field == null)
                return null;
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            log.warn("필드 값 가져오기 실패: {}.{} - {}", target.getClass().getSimpleName(), fieldName, e.getMessage());
            return null;
        }
    }

    /**
     * 지정된 객체의 필드에 값을 설정합니다.
     *
     * @param target    대상 객체
     * @param fieldName 필드명
     * @param value     설정할 값
     */
    public static void setFieldValue(Object target, String fieldName, Object value) {
        if (target == null || fieldName == null)
            return;
        try {
            Field field = findField(target.getClass(), fieldName);
            if (field == null)
                return;
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            log.warn("필드 값 설정 실패: {}.{} - {}", target.getClass().getSimpleName(), fieldName, e.getMessage());
        }
    }

    /**
     * 지정된 객체의 리스트 필드를 가져옵니다.
     *
     * @param target    대상 객체
     * @param fieldName 필드명
     * @return 리스트 객체 (없거나 오류 시 빈 리스트)
     */
    public static List<Object> getListField(Object target, String fieldName) {
        Object value = getFieldValue(target, fieldName);
        if (value instanceof List<?> list) {
            return new ArrayList<>(list); // 변경 가능하게 복사
        }
        return new ArrayList<>();
    }

    /**
     * 리스트 필드에 데이터를 병합합니다.
     *
     * @param target    대상 객체
     * @param fieldName 리스트 필드명
     * @param toAdd     추가할 리스트
     */
    public static void mergeListField(Object target, String fieldName, List<?> toAdd) {
        if (toAdd == null || toAdd.isEmpty())
            return;
        List<Object> original = getListField(target, fieldName);
        original.addAll(toAdd);
        setFieldValue(target, fieldName, original);
    }

    /**
     * 상속 계층에서 필드를 탐색합니다.
     */
    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 
     * @param type
     * @return
     */
    public static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                Number.class.isAssignableFrom(type) ||
                type == Boolean.class;
    }

    /**
     * 
     * @param field
     * @return
     */
    public static boolean isListType(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    /**
     * 
     * @param field
     * @return
     */
    public static Class<?> getListItemType(Field field) {
        if (!(field.getGenericType() instanceof ParameterizedType)) {
            return Object.class;
        }

        ParameterizedType pType = (ParameterizedType) field.getGenericType();
        Type actual = pType.getActualTypeArguments()[0];
        if (actual instanceof Class) {
            return (Class<?>) actual;
        }
        return Object.class;
    }

}
