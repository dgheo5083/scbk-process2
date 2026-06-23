package com.scbank.process.api.fw.message.serializer.xml;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.serializer.jackson.JacksonMessageSerializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 DTO -> XML 직렬화 처리 구현 클래스
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 14.
 */
@Slf4j
public class XmlMessageSerializer extends JacksonMessageSerializer {

    public XmlMessageSerializer(XmlMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata integrationMessageMetadata,
            MessageContext ctx) throws Exception {
        boolean isXmlRootWrap = integrationMessageMetadata.isXmlRootWrap();
        String xmlRootName = StringUtils.defaultIfEmpty(integrationMessageMetadata.getXmlRootName(), StringUtils.EMPTY);
        ObjectNode root = this.writeField(source, integrationMessageMetadata, ctx);

        ObjectWriter writer = objectMapper.writer();
        if (isXmlRootWrap) {
            writer = writer.withRootName(xmlRootName.isEmpty() ? integrationMessageMetadata.getId() : xmlRootName);
        } else {
            writer = writer.withoutRootName();
        }
        return writer.writeValueAsBytes(root);
    }

    @Override
    public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
        IIntegrationMessageMetadataRegistrar integrationMessageMetadataRegistrar = RuntimeContext.getBean(
                IIntegrationMessageMetadataRegistrar.class);
        IIntegrationMessageMetadata integrationMessageMetadata = integrationMessageMetadataRegistrar.getMetadata(
                source.getClass());
        if (integrationMessageMetadata == null) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                List<Field> fields = MessageUtils.getAllFields(source.getClass());

                for (Field field : fields) {
                    Object child = MessageUtils.getFieldValue(source, field.getName());
                    if (!(child instanceof IMessageObject msg)) {
                        continue;
                    }

                    IIntegrationMessageMetadata metadata = integrationMessageMetadataRegistrar.getMetadata(
                            child.getClass());
                    if (metadata == null) {
                        continue;
                    }

                    byte[] bytes = this.serialize(msg, metadata, ctx);
                    out.write(bytes);
                }

                return out.toByteArray();
            }
        } else {
            return this.serialize(source, integrationMessageMetadata, ctx);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected <T extends IMessageObject> void writeRepeatedField(T source, ObjectNode parentNode,
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

        ObjectNode wrapperNode = objectMapper.createObjectNode();

        String elementName = this.resolveElementName(meta);
        wrapperNode.set(elementName, arrayNode);

        String wrapperName = resolveWrapperName(meta);
        parentNode.set(wrapperName, wrapperNode);
    }

    private String resolveWrapperName(IMessageFieldMetadata meta) {
        return meta.getWrapperName() != null && !meta.getWrapperName().isBlank() ? meta.getWrapperName() : meta.getId();
    }

    private String resolveElementName(IMessageFieldMetadata meta) {
        return meta.getElementName() != null && !meta.getElementName().isBlank() ? meta.getElementName() : meta.getId();
    }
}
