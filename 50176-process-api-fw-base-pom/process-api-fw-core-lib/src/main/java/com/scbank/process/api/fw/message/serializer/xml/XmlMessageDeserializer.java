package com.scbank.process.api.fw.message.serializer.xml;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.serializer.jackson.JacksonMessageDeserializer;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * 프레임워크 XML -> DTO 역직렬화 처리
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 14.
 */
public class XmlMessageDeserializer extends JacksonMessageDeserializer {

    public XmlMessageDeserializer(XmlMapper objectMapper) {
        super(objectMapper);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected <T extends IMessageObject> void readRepeatedFieldFromJsonNode(JsonNode parentNode,
            IMessageFieldMetadata fieldMetadata, T parent, MessageContext ctx) throws Exception {

        String wrapperName = resolveWrapperName(fieldMetadata);
        String elementName = resolveElementName(fieldMetadata);

        JsonNode wrapperNode = parentNode.get(wrapperName);
        if (wrapperNode == null) {
            return;
        }

        List<? extends IMessageMetadata> children = fieldMetadata.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        // jackson 모듈에서는 단건의 리스트로 오는경우 ArrayNode가 아닌 ObjectNode로 인식을 함
        // 단건인경우 임의로 ArrayNode를 생성해서 반환하도록 처리
        ArrayNode arrayNode = wrapperName.equals(elementName) ? asArrayNode(wrapperNode)
                : (ArrayNode) asArrayNode(wrapperNode.get(elementName));

        int repeatCount = this.getRepeatCount(fieldMetadata, parent, ctx);
        if (repeatCount <= 0) {
            repeatCount = arrayNode.size();
        }

        Class<?> genericType = fieldMetadata.getRepeatedGenericType();
        for (int i = 0; i < repeatCount; i++) {
            ctx.pushIndex(i);
            JsonNode itemNode = arrayNode.get(i);
            if (MessageUtils.isPrimitiveType(genericType)) {
                for (IMessageMetadata child : children) {
                    IMessageFieldConverter converter = ctx.getMessageFieldConverterRegistry()
                            .get(child.getType());
                    Object value = converter.read(itemNode, (IMessageFieldMetadata) child, ctx);
                    MessageUtils.addToListField(parent, fieldMetadata.getId(), value);
                }
            } else {
                Object element = MessageUtils.createInstance(parent, fieldMetadata.getId(), i);
                for (IMessageMetadata child : children) {
                    this.readFromJsonNode(itemNode, (IMessageObject) element, (IMessageFieldMetadata) child, ctx);
                }
                MessageUtils.addToListField(parent, fieldMetadata.getId(), element);
            }
            ctx.popIndex();
        }
    }

    private String resolveWrapperName(IMessageFieldMetadata meta) {
        return meta.getWrapperName() != null && !meta.getWrapperName().isBlank() ? meta.getWrapperName() : meta.getId();
    }

    private String resolveElementName(IMessageFieldMetadata meta) {
        return meta.getElementName() != null && !meta.getElementName().isBlank() ? meta.getElementName() : meta.getId();
    }
}
