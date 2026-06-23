package com.scbank.process.api.fw.message.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.core.utils.ByteBuffWrap;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 전문 메시지 처리 유틸리티 클래스
 * 
 * @author sungdon.choi
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

    /**
     * 
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 
     * @param parent
     * @param fieldName
     * @param index
     * @return
     */
    public static Object createInstance(Object parent, String fieldName, int index) {
        try {
            Field field = parent.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            // 제네릭 타입 추출
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            Type actualType = pt.getActualTypeArguments()[0];

            if (!(actualType instanceof Class<?> itemClass)) {
                throw new IllegalStateException("리스트 항목 타입을 확인할 수 없습니다: " + fieldName);
            }

            Object instance = itemClass.getDeclaredConstructor().newInstance();

            return instance;

        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR,
                    "[" + fieldName + "] 인덱스 " + index + " 객체 생성 실패", e);
        }
    }

    /**
     * 
     * @param parent
     * @param fieldName
     * @param element
     */
    @SuppressWarnings("unchecked")
    public static void addToListField(Object parent, String fieldName, Object element) {
        try {
            Field field = parent.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            List<Object> list = (List<Object>) field.get(parent);
            if (list == null) {
                list = new ArrayList<>();
                field.set(parent, list);
            }
            list.add(element);
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, "리스트 필드에 요소 추가 실패: " + fieldName, e);
        }
    }

    /**
     * 
     * @param parent
     * @param fieldName
     * @return
     */
    public static Object createInstance(Object parent, String fieldName) {
        try {
            Field field = parent.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Class<?> type = field.getType();
            Object instance = type.getDeclaredConstructor().newInstance();

            field.set(parent, instance);
            return instance;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, "[" + fieldName + "] 인스턴스 생성 실패", e);
        }
    }

    /**
     * 
     * @param target
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object target, String fieldName) {
        if (target == null) {
            return null;
        }

        if (fieldName == null) {
            throw new IllegalArgumentException("fieldName이 null입니다.");
        }

        Class<?> clazz = target.getClass();
        Field field = findField(clazz, fieldName);
        if (field == null) {
            throw new IllegalStateException("[" + clazz.getName() + "]에서 필드 [" + fieldName + "] 를 찾을 수 없습니다.");
        }

        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, "필드 [" + fieldName + "] 접근 실패", e);
        }
    }

    /**
     * 
     * @param parent
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object parent, String fieldName, Object value) {
        if (parent == null || fieldName == null) {
            throw new IllegalArgumentException("parent나 fieldName이 null입니다.");
        }

        Class<?> clazz = parent.getClass();
        Field field = findField(clazz, fieldName);
        if (field == null) {
            throw new IllegalStateException("필드 [" + fieldName + "] 를 " + clazz.getName() + "에서 찾을 수 없습니다.");
        }

        try {
            field.setAccessible(true);
            field.set(parent, value);
        } catch (IllegalAccessException e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR,
                    "필드 [" + fieldName + "] 설정 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 슈퍼 클래스까지 포함하여 필드 탐색
     */
    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 주어진 필드가 List 이고, IMessageObject를 구현한 경우에만 타입 반환.
     *
     * @param field 검사할 필드
     * @return (IMessageObject 구현체) 또는 null
     */
    public static Class<?> getGenericListTypeIfImplements(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return null;
        }

        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type actualType = parameterizedType.getActualTypeArguments()[0];
            if (actualType instanceof Class<?> clazz) {
                if (IMessageObject.class.isAssignableFrom(clazz)) {
                    return clazz;
                }
            }
        }

        return null;
    }

    /**
     * 자바 타입에 따라 MessageType 추론
     *
     * @param clazz 전문 메타데이터 대상 클래스
     */
    public static MessageType fromJavaType(Class<?> clazz) {
        if (clazz == null) {
            return MessageType.NONE;
        }

        if (clazz == String.class) {
            return MessageType.STRING;
        }
        
        if (clazz == Boolean.class || clazz == boolean.class) {
        	return MessageType.BOOLEAN;
        }

        if (clazz == Byte.class || clazz == byte.class) {
            return MessageType.BYTE;
        }

        if (clazz == Short.class || clazz == short.class) {
            return MessageType.SHORT;
        }

        if (clazz == Character.class || clazz == char.class) {
            return MessageType.CHAR;
        }

        if (clazz == Integer.class || clazz == int.class) {
            return MessageType.INTEGER;
        }
        if (clazz == Long.class || clazz == long.class) {
            return MessageType.LONG;
        }
        if (clazz == Double.class || clazz == double.class) {
            return MessageType.DOUBLE;
        }
        if (clazz == Float.class || clazz == float.class) {
            return MessageType.FLOAT;
        }
        if (clazz == BigDecimal.class) {
            return MessageType.BIGDECIMAL;
        }
        if (clazz == byte[].class || clazz == ByteBuffWrap.class) {
            return MessageType.VARIABLE_LENGTH;
        }
        if (List.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(RepeatedField.class)) {
            return MessageType.REPEATED;
        }
        // 기본은 SEGMENT 또는 사용자 객체
        if (!clazz.isPrimitive() &&
                !clazz.getName().startsWith("java.") &&
                IMessageObject.class.isAssignableFrom(clazz)) {
            return MessageType.OBJECT;
        }

        // 파일 업로드/다운로드 관련
        if (clazz == MultipartFile.class) {
            return MessageType.MULTIPART_FILE;
        }
        return MessageType.NONE;
    }

    /**
     * 
     * @param <T>
     * @param parent
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends IMessageObject> T createInstance(T parent, String fieldName) {
        try {
            Field field = parent.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Object nestedInstance = field.getType().getDeclaredConstructor().newInstance();
            field.set(parent, nestedInstance);

            return (T) nestedInstance;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(e);
        }
    }

    /**
     * 
     * @param <T>
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends IMessageObject> T createInstance(Field field) {
        try {
            Class<?> fieldType = field.getType();
            return (T) fieldType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("필드 인스턴스 생성 실패: " + field.getName(), e);
        }
    }

    /**
     * 
     * @param type
     * @return
     */
    public static boolean isPrimitiveType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Long.class
                || type == Short.class
                || type == Byte.class
                || type == Double.class
                || type == Float.class
                || type == Boolean.class
                || type == Character.class
                || type == BigDecimal.class
                || type == BigInteger.class
                || type == LocalDate.class
                || type == LocalDateTime.class
                || type == UUID.class;
    }
}
