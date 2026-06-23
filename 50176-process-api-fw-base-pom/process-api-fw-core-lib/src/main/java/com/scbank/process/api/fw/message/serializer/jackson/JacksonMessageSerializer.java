package com.scbank.process.api.fw.message.serializer.jackson;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.serializer.AbstractMessageSerializer;
import com.scbank.process.api.fw.message.serializer.IMessageSerializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 DTO -> JSON 직렬화 처리
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 14.
 */
@Slf4j
@RequiredArgsConstructor
public class JacksonMessageSerializer extends AbstractMessageSerializer implements IMessageSerializer {

    protected final ObjectMapper objectMapper;

    @Override
    public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
        IIntegrationMessageMetadata metadata = this.findIntegrationMessageMetadata(source.getClass());
        return this.serialize(source, metadata, ctx);
    }

    @Override
    public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata integrationMessageMetadata,
            MessageContext ctx) throws Exception {
        ObjectNode root = this.writeField(source, integrationMessageMetadata, ctx);

        ObjectWriter writer = this
                .getObjectWriter(ctx.getSerializationOptions().enabled(MessageFormatOption.PRETTY_FORMAT));
        return writer.writeValueAsBytes(root);
    }

    protected <O extends ObjectNode, T extends IMessageObject> O writeField(
            T source,
            IIntegrationMessageMetadata metadata,
            MessageContext ctx) throws Exception {
        return this.writeField(source, metadata.getChildren(), ctx);
    }

    @SuppressWarnings("unchecked")
    protected <O extends ObjectNode, T extends IMessageObject> O writeField(
            T source,
            List<IMessageMetadata> children,
            MessageContext ctx) throws Exception {
        ObjectNode root = objectMapper.createObjectNode();

        if (CollectionUtils.isEmpty(children)) {
            return (O) root;
        }

        for (IMessageMetadata c : children) {
            this.writeField(source, root, (IMessageFieldMetadata) c, ctx);
        }
        return (O) root;
    }

    protected <T extends IMessageObject> void writeField(
            T source,
            ObjectNode parent,
            IMessageFieldMetadata fieldMetadata,
            MessageContext ctx) throws Exception {
        MessageType messageType = fieldMetadata.getType();
        switch (messageType) {
            case OBJECT -> writeObjectField(source, parent, fieldMetadata, ctx);
            case REPEATED -> this.writeRepeatedField(source, parent, fieldMetadata, ctx);
            case BOOLEAN, STRING, BYTE, CHAR, SHORT, INTEGER, LONG, DOUBLE, FLOAT, BIGDECIMAL ->
                this.writePrimitiveField(source, parent, fieldMetadata, ctx);
            default -> log.error("Unexpected value: " + fieldMetadata.getId() + "/" + fieldMetadata.getType());
        }
    }

	@SuppressWarnings("unchecked")
    private <T extends IMessageObject> void writeObjectField(T source, ObjectNode parent, IMessageFieldMetadata meta,
            MessageContext ctx) throws Exception {
        T child = (T) MessageUtils.getFieldValue(source, meta.getFieldName());
        if (child != null) {
            ObjectNode childNode = writeField(child, meta.getChildren(), ctx);
            parent.set(resolveOutputKey(meta), childNode);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void writeRepeatedField(
            T source,
            ObjectNode parentNode,
            IMessageFieldMetadata meta, MessageContext ctx) throws Exception {
        List<?> list = (List<?>) MessageUtils.getFieldValue(source, meta.getFieldName());
        if (CollectionUtils.isEmpty(list)) {
            list = List.of();
        }

        ArrayNode arrayNode = objectMapper.createArrayNode();
        List<IMessageMetadata> children = meta.getChildren();
        Class<?> genericType = meta.getRepeatedGenericType();

        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            ctx.pushIndex(i);

            if (!MessageUtils.isPrimitiveType(genericType)) {
                ObjectNode itemNode = objectMapper.createObjectNode();
                for (IMessageMetadata child : children) {
                    writeField((IMessageObject) item, itemNode, (IMessageFieldMetadata) child, ctx);
                }
                arrayNode.add(itemNode);
            } else {
                // 원시타입인 경우
                for (IMessageMetadata child : children) {
                    IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(child.getType());
                    if (converter == null) {
                        throw new IllegalStateException("지원하지 않는 타입: " + meta.getType());
                    }

                    Object value = item;
                    JsonNode valueNode = (JsonNode) converter.write(value, meta, ctx);
                    arrayNode.add(valueNode);
                }
            }
            ctx.popIndex();
        }

        parentNode.set(resolveOutputKey(meta), arrayNode);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T extends IMessageObject> void writePrimitiveField(T parent, ObjectNode parentNode,
            IMessageFieldMetadata fieldMetadata,
            MessageContext ctx) throws Exception {
        IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry().get(fieldMetadata.getType());
        if (converter == null) {
            throw new IllegalStateException("지원하지 않는 타입: " + fieldMetadata.getType());
        }

        Object value = MessageUtils.getFieldValue(parent, fieldMetadata.getFieldName());
        // if (value == null) {
        // return;
        // }
        JsonNode valueNode = (JsonNode) converter.write(value, fieldMetadata, ctx);
        parentNode.set(fieldMetadata.getId(), valueNode);
    }
    
    protected String resolveOutputKey(IMessageFieldMetadata meta) {
        return meta.getId() != null && !meta.getId().isBlank() ? meta.getId() : meta.getName();
    }

    private ObjectWriter getObjectWriter(boolean prettyFormat) {
        if (prettyFormat) {
            return this.objectMapper.writer().withDefaultPrettyPrinter();
        }
        return this.objectMapper.writer();
    }
}
