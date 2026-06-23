package com.scbank.process.api.fw.message.serializer.multipart;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

/**
 * {@code multipart/form-data} 요청을 {@link IMessageObject}로 역직렬화하는 Deserializer
 * 구현체입니다.
 * 
 * Spring의 {@link MultipartHttpServletRequest}를 통해 파라미터 및 파일을 추출하고,
 * 프레임워크 메타데이터 기반으로 필드를 DTO에 주입합니다.
 * 
 * 지원하는 필드 타입:
 * <ul>
 * <li>원시 타입(String, Integer 등)</li>
 * <li>List&lt;원시 타입&gt;</li>
 * <li>MultipartFile(byte[] 바인딩)</li>
 * <li>List&lt;MultipartFile(byte[])&gt;</li>
 * </ul>
 * 중첩 객체는 현재 미지원입니다.
 *
 * @author sungdon.choi
 */
public class MultipartMessageDeserializer extends AbstractMessageDeserializer implements IMessageDeserializer {

    /**
     * 클래스 기반 메타데이터를 통해 multipart 데이터를 역직렬화합니다.
     *
     * @param ignored    multipart는 byte[] 사용 안 함
     * @param targetType 역직렬화 대상 클래스
     * @param ctx        메시지 컨텍스트
     * @return 역직렬화된 메시지 객체
     * @throws Exception 처리 중 오류 발생 시
     */
    @Override
    public <T extends IMessageObject> T deserialize(byte[] ignored, Class<T> targetType, MessageContext ctx)
            throws Exception {
        IIntegrationMessageMetadata metadata = this.findIntegrationMessageMetadata(targetType);
        return this.deserialize(ignored, metadata, ctx);
    }

    /**
     * 메타데이터 기반으로 multipart 데이터를 역직렬화합니다.
     *
     * @param ignored  multipart는 byte[] 사용 안 함
     * @param metadata 통합 메시지 메타데이터
     * @param ctx      메시지 컨텍스트
     * @return 역직렬화된 메시지 객체
     * @throws Exception 처리 중 오류 발생 시
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends IMessageObject> T deserialize(byte[] ignored, IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) attributes.getRequest();

        Class<T> targetType = (Class<T>) metadata.getTargetClass();
        T instance = targetType.getDeclaredConstructor().newInstance();

        readFromRequest(request, instance, metadata.getChildren(), ctx);

        return instance;
    }

    /**
     * 메타데이터 목록을 기반으로 multipart 요청에서 데이터를 추출하여 DTO에 주입합니다.
     *
     * @param request      MultipartHttpServletRequest 객체
     * @param data         역직렬화 대상 DTO 인스턴스
     * @param metadataList 필드 메타데이터 목록
     * @param ctx          메시지 처리 컨텍스트
     * @param <T>          IMessageObject 구현 클래스 타입
     * @throws Exception 변환 중 오류 발생 시
     */
    protected <T extends IMessageObject> void readFromRequest(MultipartHttpServletRequest request, T data,
            List<IMessageMetadata> metadataList, MessageContext ctx) throws Exception {
        if (CollectionUtils.isEmpty(metadataList))
            return;
        for (IMessageMetadata metadata : metadataList) {
            readFromRequest(request, data, (IMessageFieldMetadata) metadata, ctx);
        }
    }

    /**
     * 메타데이터 목록을 기반으로 multipart 요청에서 데이터를 추출하여 DTO에 주입합니다.
     *
     * @param request  MultipartHttpServletRequest 객체
     * @param data     역직렬화 대상 DTO 인스턴스
     * @param metadata 필드 메타데이터
     * @param ctx      메시지 처리 컨텍스트
     * @param <T>      IMessageObject 구현 클래스 타입
     * @throws Exception 변환 중 오류 발생 시
     */
    protected <T extends IMessageObject> void readFromRequest(MultipartHttpServletRequest request, T data,
            IMessageFieldMetadata metadata, MessageContext ctx) throws Exception {
        MessageType type = metadata.getType();
        switch (type) {
            case STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                readPrimitiveField(request, data, metadata, ctx);
            case REPEATED -> readRepeatedField(request, data, metadata, ctx);
            case MULTIPART_FILE -> readMultipartFile(request, data, metadata, ctx);
            case OBJECT -> {
                // 중첩 객체는 현재 미지원
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        }
    }

    /**
     * 단일 파라미터 필드를 읽어 변환한 후 DTO에 주입합니다.
     *
     * @param request  Multipart 요청 객체
     * @param data     DTO 인스턴스
     * @param metadata 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     * @param <T>      IMessageObject 구현 클래스
     * @throws Exception 변환 또는 주입 실패 시
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readPrimitiveField(MultipartHttpServletRequest request, T data,
            IMessageFieldMetadata metadata, MessageContext ctx) throws Exception {
        String param = request.getParameter(metadata.getId());
        if (param == null)
            return;

        IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(metadata.getType());
        if (converter == null)
            throw new IllegalStateException("지원하지 않는 타입: " + metadata.getType());

        Object value = converter.read(param, metadata, ctx);
        MessageUtils.setFieldValue(data, metadata.getFieldName(), value);
        ctx.addPathValue(metadata.getPath(), value);
    }

    /**
     * 반복 필드를 처리하며, 원시 타입 리스트 또는 파일 리스트인지 판단 후 분기합니다.
     *
     * @param request  Multipart 요청 객체
     * @param data     DTO 인스턴스
     * @param metadata 반복 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     * @param <T>      IMessageObject 구현 클래스
     * @throws Exception 변환 또는 주입 실패 시
     */
    protected <T extends IMessageObject> void readRepeatedField(MultipartHttpServletRequest request, T data,
            IMessageFieldMetadata metadata, MessageContext ctx) throws Exception {
        if (isMultipartFileList(metadata)) {
            readRepeatedMultipartFile(request, data, metadata, ctx);
        } else {
            readRepeatedPrimitiveField(request, data, metadata, ctx);
        }
    }

    /**
     * 메타데이터가 다중 파일 업로드(byte[] 리스트)인지를 판단합니다.
     */
    protected boolean isMultipartFileList(IMessageFieldMetadata metadata) {
        Class<?> repeatedGenericType = metadata.getRepeatedGenericType();
        return (repeatedGenericType == byte[].class || repeatedGenericType == MultipartFile.class) &&
                metadata.getType() == MessageType.REPEATED;
    }

    /**
     * 반복되는 원시 타입 파라미터 값을 읽고 리스트 형태로 DTO에 주입합니다.
     *
     * @param request  Multipart 요청 객체
     * @param data     DTO 인스턴스
     * @param metadata 반복 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     * @param <T>      IMessageObject 구현 클래스
     * @throws Exception 변환 또는 주입 실패 시
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readRepeatedPrimitiveField(MultipartHttpServletRequest request, T data,
            IMessageFieldMetadata metadata, MessageContext ctx) throws Exception {
        String[] values = request.getParameterValues(metadata.getId());
        if (values == null || values.length == 0)
            return;

        List<IMessageMetadata> children = metadata.getChildren();
        if (CollectionUtils.isEmpty(children))
            return;

        IMessageFieldMetadata childMetadata = (IMessageFieldMetadata) children.get(0);
        IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(childMetadata.getType());
        if (converter == null)
            throw new IllegalStateException("지원하지 않는 타입: " + childMetadata.getType());

        for (int i = 0; i < values.length; i++) {
            ctx.pushIndex(i);
            Object value = converter.read(values[i], childMetadata, ctx);
            String resolvedPath = childMetadata.getPath().replace("[*]", "[" + i + "]");
            ctx.addPathValue(resolvedPath, value);
            MessageUtils.addToListField(data, metadata.getId(), value);
            ctx.popIndex();
        }
    }

    /**
     * 다중 파일 업로드 필드(List&lt;byte[]&gt;)를 읽어 DTO에 주입합니다.
     *
     * @param request  Multipart 요청 객체
     * @param data     DTO 인스턴스
     * @param metadata 반복 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     * @param <T>      IMessageObject 구현 클래스
     * @throws Exception 파일 읽기 또는 주입 실패 시
     */
    protected <T extends IMessageObject> void readRepeatedMultipartFile(MultipartHttpServletRequest request, T data,
            IMessageFieldMetadata metadata, MessageContext ctx) throws Exception {
        List<MultipartFile> files = request.getFiles(metadata.getId());
        if (CollectionUtils.isEmpty(files))
            return;

        Class<?> genericType = metadata.getRepeatedGenericType();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file != null && !file.isEmpty()) {
                if (genericType == MultipartFile.class) {
                    MessageUtils.addToListField(data, metadata.getId(), file);
                } else if (genericType == byte[].class) {
                    byte[] content = file.getBytes();
                    MessageUtils.addToListField(data, metadata.getId(), content);
                }

                String resolvedPath = metadata.getPath().replace("[*]", "[" + i + "]");
                ctx.addPathValue(resolvedPath, file.getSize());
            }
        }
    }

    /**
     * 단일 MultipartFile 필드를 읽어 byte[]로 변환 후 DTO에 주입합니다.
     *
     * @param request  Multipart 요청 객체
     * @param data     DTO 인스턴스
     * @param metadata 필드 메타데이터
     * @param ctx      메시지 컨텍스트
     * @param <T>      IMessageObject 구현 클래스
     * @throws Exception 파일 읽기 또는 주입 실패 시
     */
    protected <T extends IMessageObject> void readMultipartFile(MultipartHttpServletRequest request, T data,
            IMessageFieldMetadata metadata, MessageContext ctx) throws Exception {
        MultipartFile file = request.getFile(metadata.getId());
        if (file == null || file.isEmpty())
            return;

        Class<?> fieldType = metadata.getFieldType();
        if (fieldType == MultipartFile.class) {
            MessageUtils.setFieldValue(data, metadata.getFieldName(), file);
        } else if (fieldType == byte[].class) {
            byte[] content = file.getBytes();
            MessageUtils.setFieldValue(data, metadata.getFieldName(), content);
        }

        ctx.addPathValue(metadata.getPath(), file.getSize());
    }
}
