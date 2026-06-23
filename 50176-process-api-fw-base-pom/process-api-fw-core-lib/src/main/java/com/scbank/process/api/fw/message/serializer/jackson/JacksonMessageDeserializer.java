package com.scbank.process.api.fw.message.serializer.jackson;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 JSON -> DTO 역직렬화 처리
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 14.
 */
@Slf4j
@RequiredArgsConstructor
public class JacksonMessageDeserializer extends AbstractMessageDeserializer implements IMessageDeserializer {

    protected final ObjectMapper objectMapper;

    @Override
    public <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
            throws Exception {
        IIntegrationMessageMetadata integrationMessageMetadata = this.findIntegrationMessageMetadata(targetType);
        return this.deserialize(source, integrationMessageMetadata, ctx);
    }

    @Override
    public <T extends IMessageObject> T deserialize(byte[] source,
            IIntegrationMessageMetadata integrationMessageMetadata, MessageContext ctx) throws Exception {
        ObjectNode root = (ObjectNode) objectMapper.readTree(source);
        return this.readFromJsonNode(root, integrationMessageMetadata, ctx);
    }

    @SuppressWarnings("unchecked")
    protected <T extends IMessageObject> T readFromJsonNode(
            ObjectNode rootNode,
            IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        Class<T> clazz = (Class<T>) metadata.getTargetClass();
        T obj = (T) clazz.getDeclaredConstructor().newInstance();
        this.readFromJsonNode(rootNode, obj, metadata.getChildren(), ctx);
        return obj;
    }

    protected <T extends IMessageObject> void readFromJsonNode(
            ObjectNode rootNode,
            T data,
            List<IMessageMetadata> metadataList,
            MessageContext ctx) throws Exception {
        if (CollectionUtils.isEmpty(metadataList)) {
            return;
        }

        for (IMessageMetadata metadata : metadataList) {
            this.readFromJsonNode(rootNode, data, (IMessageFieldMetadata) metadata, ctx);
        }
    }

    protected <T extends IMessageObject> void readFromJsonNode(
            JsonNode parentNode,
            T data,
            IMessageFieldMetadata metadata,
            MessageContext ctx) throws Exception {
        // 조건부 필드 처리
        if (!this.checkConditionalField(metadata, ctx)) {
            return;
        }

        switch (metadata.getType()) {
            case OBJECT -> this.readObjectFromJsonNode(parentNode, metadata, data, ctx);
            case REPEATED -> this.readRepeatedFieldFromJsonNode(parentNode, metadata, data, ctx);
            case BOOLEAN, STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                this.readPrimitiveTypeFromNode(parentNode, metadata, data, ctx);
            default -> log.error("Unexpected value: " + metadata.getId() + "/" + metadata.getType());
        }
    }

    protected <T extends IMessageObject> void readObjectFromJsonNode(
            JsonNode parentNode,
            IMessageFieldMetadata metadata,
            T parent,
            MessageContext ctx) throws Exception {
        if (parentNode == null) {
            return;
        }
        JsonNode valNode = parentNode.get(metadata.getId());
        T data = MessageUtils.createInstance(parent, metadata.getFieldName());

        List<IMessageMetadata> children = metadata.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            for (IMessageMetadata child : children) {
                this.readFromJsonNode(valNode, data, (IMessageFieldMetadata) child, ctx);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readRepeatedFieldFromJsonNode(
            JsonNode parentNode,
            IMessageFieldMetadata fieldMetadata,
            T parent,
            MessageContext ctx) throws Exception {

        if (parentNode == null) {
            return;
        }

        JsonNode node = parentNode.get(fieldMetadata.getId());
        if (node == null) {
            return;
        }

        if (!node.isArray()) {
            return;
        }

        List<? extends IMessageMetadata> children = fieldMetadata.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        // jackson 모듈에서는 단건의 리스트로 오는경우 ArrayNode가 아닌 ObjectNode로 인식을 함
        // 단건인경우 임의로 ArrayNode를 생성해서 반환하도록 처리
        ArrayNode arrayNode = asArrayNode(node);

        int repeatCount = this.getRepeatCount(fieldMetadata, parent, ctx);
        if (repeatCount <= 0) {
            repeatCount = arrayNode.size();
        }

        Class<?> genericType = fieldMetadata.getRepeatedGenericType();
        if (MessageUtils.isPrimitiveType(genericType)) {
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode elementNode = arrayNode.get(i);
                ctx.pushIndex(i); // 트레이스 및 Path 추적

                IMessageFieldMetadata childMetadata = (IMessageFieldMetadata) fieldMetadata.getChildren().get(0);
                IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(childMetadata.getType());

                if (converter == null) {
                    throw new IllegalStateException("지원하지 않는 타입: " + childMetadata.getType());
                }

                Object value = converter.read(elementNode, childMetadata, ctx);

                // Path 트레이싱 저장
                String resolvedPath = childMetadata.getPath().replace("[*]", "[" + i + "]");
                ctx.addPathValue(resolvedPath, value);

                ctx.popIndex();

                MessageUtils.addToListField(parent, fieldMetadata.getFieldName(), value);
            }
        } else {
            for (int i = 0; i < repeatCount; i++) {
                Object element = MessageUtils.createInstance(parent, fieldMetadata.getFieldName(), i);
                JsonNode elementNode = arrayNode.get(i);
                ctx.pushIndex(i);

                for (IMessageMetadata child : children) {
                    IMessageFieldMetadata childMeta = (IMessageFieldMetadata) child;
                    this.readPrimitiveTypeWithIndexFromNode(elementNode, childMeta, (IMessageObject) element, i,
                            ctx);
                }

                ctx.popIndex();

                MessageUtils.addToListField(parent, fieldMetadata.getFieldName(), element);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readPrimitiveTypeFromNode(
            JsonNode parentNode,
            IMessageFieldMetadata metadata,
            T parent,
            MessageContext ctx) {
        try {
            if (parentNode == null) {
                return;
            }

            // 필드 타입과 컨버터 조회
            MessageType messageType = metadata.getType();
            IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(messageType);
            if (converter == null) {
                throw new IllegalStateException("지원하지 않는 타입: " + messageType);
            }

            JsonNode valueNode = parentNode.get(metadata.getId());
            if (valueNode == null) {
                return;
            }
            // 값 변환
            Object value = converter.read(valueNode, metadata, ctx);
            // DTO 필드에 값 세팅
            MessageUtils.setFieldValue(parent, metadata.getFieldName(), value);

            ctx.addPathValue(metadata.getPath(), value);

        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.MSG_SERIALIZATION_FAILED,
                    "필드 [" + metadata.getId() + "] 읽기 실패", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void readPrimitiveTypeWithIndexFromNode(
            JsonNode parentNode,
            IMessageFieldMetadata metadata,
            T parent,
            int index,
            MessageContext ctx) throws Exception {
        MessageType messageType = metadata.getType();
        IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(messageType);
        if (converter == null) {
            throw new IllegalStateException("지원하지 않는 타입: " + messageType);
        }

        if (parentNode == null) {
            return;
        }

        JsonNode valueNode = parentNode.get(metadata.getId());
        if (valueNode == null) {
            return;
        }
        // 값 변환
        Object value = converter.read(valueNode, metadata, ctx);

        MessageUtils.setFieldValue(parent, metadata.getFieldName(), value);

        String resolvedPath = metadata.getPath().replace("[*]", "[" + index + "]");
        ctx.addPathValue(resolvedPath, value);
    }

    /**
     * 단건의 리스트인경우 ObjectNode로 인식하기때문에 임의로 ArrayNode로 만들어준다.
     * 
     * @param node
     * @return
     */
    protected ArrayNode asArrayNode(JsonNode node) {
        if (node == null || node.isNull()) {
            return new ObjectMapper().createArrayNode();
        }

        if (node.isArray()) {
            return (ArrayNode) node;
        }

        ArrayNode arrayNode = this.objectMapper.createArrayNode();
        arrayNode.add(node.deepCopy());
        return arrayNode;
    }
}
