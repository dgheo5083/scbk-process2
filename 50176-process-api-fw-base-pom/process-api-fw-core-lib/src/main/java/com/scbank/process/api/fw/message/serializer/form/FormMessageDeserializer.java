package com.scbank.process.api.fw.message.serializer.form;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.serializer.AbstractMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.IMessageDeserializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * application/x-www-form-urlencoded 메시지를 {@link IMessageObject}로 역직렬화하는
 * Deserializer입니다.
 * <p>
 * 단일 필드 또는 List&lt;원시타입&gt;만 처리하며, 중첩 객체는 무시합니다.
 * 프레임워크의 메타데이터 구조와 {@link IMessageFieldConverter} 기반 컨버터를 통해 정형화된 구조로 파싱합니다.
 * 
 * @author sungdon.choi
 * @since 2025.04.30
 */
@Slf4j
public class FormMessageDeserializer extends AbstractMessageDeserializer implements IMessageDeserializer {

    /**
     * 지정된 대상 타입에 대해 메타데이터를 조회하고 바이트 배열을 역직렬화합니다.
     * 
     * @param source     form 데이터가 담긴 byte 배열
     * @param targetType 대상 DTO 타입
     * @param ctx        메시지 변환 컨텍스트
     * @return 역직렬화된 DTO 객체
     * @throws Exception 역직렬화 중 오류 발생 시
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
            throws Exception {
        IIntegrationMessageMetadata metadata = this.findIntegrationMessageMetadata(targetType);
        return this.deserialize(source, metadata, ctx);
    }

    /**
     * 주어진 메타데이터를 기반으로 byte 배열을 form 파라미터로 변환하고 객체로 매핑합니다.
     * 
     * @param source   form 데이터 byte 배열
     * @param metadata 대상 메타데이터
     * @param ctx      메시지 컨텍스트
     * @return 역직렬화된 DTO 객체
     * @throws Exception 변환 중 오류 발생 시
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] source, IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        String encoding = ctx.getDefaultEncoding();
        String form = new String(source, encoding);
        Map<String, List<String>> parameters = parseParams(form, encoding);
        return readFromMap(parameters, metadata, ctx);
    }

    /**
     * 파라미터 맵을 DTO 객체로 매핑합니다.
     * 
     * @param paramMap 파싱된 form 파라미터 맵
     * @param metadata 메타데이터 정의
     * @param ctx      메시지 컨텍스트
     * @param <T>      IMessageObject 타입
     * @return 역직렬화된 DTO 객체
     */
    @SuppressWarnings("unchecked")
    protected <T extends IMessageObject> T readFromMap(
            Map<String, List<String>> paramMap,
            IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        Class<T> clazz = (Class<T>) metadata.getTargetClass();
        T obj = (T) clazz.getDeclaredConstructor().newInstance();
        this.readFromMap(paramMap, obj, metadata.getChildren(), ctx);
        return obj;
    }

    /**
     * 메타데이터 리스트에 따라 각 필드를 순회하며 파라미터 맵의 값을 DTO 필드에 설정합니다.
     * 
     * @param parameters   form 파라미터 맵
     * @param data         대상 객체
     * @param metadataList 필드 메타데이터 목록
     * @param ctx          메시지 컨텍스트
     */
    protected <T extends IMessageObject> void readFromMap(
            Map<String, List<String>> parameters,
            T data,
            List<IMessageMetadata> metadataList,
            MessageContext ctx) throws Exception {
        if (CollectionUtils.isEmpty(metadataList)) {
            return;
        }

        for (IMessageMetadata metadata : metadataList) {
            this.readFromMap(parameters, data, (IMessageFieldMetadata) metadata, ctx);
        }
    }

    /**
     * 단일 필드 또는 반복 필드를 판별하여 적절한 매핑 메서드를 호출합니다.
     * 
     * @param parameters form 파라미터 맵
     * @param data       대상 객체
     * @param metadata   필드 메타데이터
     * @param ctx        메시지 컨텍스트
     */
    protected <T extends IMessageObject> void readFromMap(
            Map<String, List<String>> parameters,
            T data,
            IMessageFieldMetadata metadata,
            MessageContext ctx) throws Exception {
        switch (metadata.getType()) {
            case REPEATED -> this.readRepeatedFieldFromMap(parameters, metadata, data, ctx);
            case STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                this.readPrimitiveTypeFromMap(parameters, metadata, data, ctx);
            default -> {
                log.error("Unexpected value: " + metadata.getId() + "/" + metadata.getType());
            }
        }
    }

    /**
     * 반복 필드 (List 형태)를 파라미터에서 읽고 변환하여 객체에 설정합니다.
     *
     * @param parameters    form 파라미터 맵
     * @param fieldMetadata 반복 필드 메타데이터
     * @param parent        상위 객체
     * @param ctx           메시지 컨텍스트
     * @param <T>           IMessageObject 타입
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readRepeatedFieldFromMap(
            Map<String, List<String>> parameters,
            IMessageFieldMetadata fieldMetadata,
            T parent,
            MessageContext ctx) throws Exception {

        List<?> array = parameters.get(fieldMetadata.getId());
        if (array == null) {
            return;
        }

        List<? extends IMessageMetadata> children = fieldMetadata.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        int repeatCount = this.getRepeatCount(fieldMetadata, parent, ctx);
        if (repeatCount <= 0) {
            repeatCount = array.size();
        }

        Class<?> genericType = fieldMetadata.getRepeatedGenericType();
        if (MessageUtils.isPrimitiveType(genericType)) {
            for (int i = 0; i < repeatCount && i < array.size(); i++) {
                Object obj = array.get(i);
                ctx.pushIndex(i); // 트레이스 및 Path 추적

                IMessageFieldMetadata childMetadata = (IMessageFieldMetadata) fieldMetadata.getChildren().get(0);
                IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(childMetadata.getType());

                if (converter == null) {
                    throw new IllegalStateException("지원하지 않는 타입: " + childMetadata.getType());
                }

                Object value = converter.read(obj, childMetadata, ctx);

                // Path 트레이싱 저장
                String resolvedPath = childMetadata.getPath().replace("[*]", "[" + i + "]");
                ctx.addPathValue(resolvedPath, value);

                ctx.popIndex();

                MessageUtils.addToListField(parent, fieldMetadata.getId(), value);
            }
        }
    }

    /**
     * 단일 필드 값을 파라미터에서 읽고 변환하여 객체에 설정합니다.
     *
     * @param parameters form 파라미터 맵
     * @param metadata   단일 필드 메타데이터
     * @param parent     대상 객체
     * @param ctx        메시지 컨텍스트
     * @param <T>        IMessageObject 타입
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T extends IMessageObject> void readPrimitiveTypeFromMap(
            Map<String, List<String>> parameters,
            IMessageFieldMetadata metadata,
            T parent, MessageContext ctx) {
        try {
            // 필드 타입과 컨버터 조회
            MessageType messageType = metadata.getType();
            IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(messageType);
            if (converter == null) {
                throw new IllegalStateException("지원하지 않는 타입: " + messageType);
            }

            List<String> values = parameters.get(metadata.getId());
            if (values == null) {
                return;
            }

            String soruce = values.get(0);
            // 값 변환
            Object value = converter.read(soruce, metadata, ctx);
            // DTO 필드에 값 세팅
            MessageUtils.setFieldValue(parent, metadata.getFieldName(), value);

            ctx.addPathValue(metadata.getPath(), value);

        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_DESERIALIZATION_FAILED,
                    "필드 [" + metadata.getId() + "] 읽기 실패", e);
        }
    }

    /**
     * application/x-www-form-urlencoded 문자열을 파싱하여 Map<String, List<String>> 형태로
     * 변환합니다.
     * 
     * @param form     원본 문자열 (key1=val1&key2=val2 형식)
     * @param encoding 인코딩 (예: UTF-8)
     * @return 파라미터 이름과 값 리스트를 매핑한 Map
     * @throws UnsupportedEncodingException 지정된 인코딩이 지원되지 않을 경우
     */
    private Map<String, List<String>> parseParams(String form, String encoding) throws UnsupportedEncodingException {
        Map<String, List<String>> map = new LinkedHashMap<>();
        String[] pairs = form.split("&");

        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx < 0)
                continue;

            String name = URLDecoder.decode(pair.substring(0, idx), encoding);
            String value = URLDecoder.decode(pair.substring(idx + 1), encoding);

            map.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        }

        return map;
    }
}
