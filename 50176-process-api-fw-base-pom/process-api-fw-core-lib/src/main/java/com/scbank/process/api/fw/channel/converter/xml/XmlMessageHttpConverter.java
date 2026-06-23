package com.scbank.process.api.fw.channel.converter.xml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.channel.converter.AbstractMessageHttpConverter;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.mapper.xml.XmlMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * <pre>
 * 프레임워크 전문 메타데이터 기반 XML HTTP 메시지 변환기입니다.
 *
 * 주요 기능:
 * - HTTP 요청 본문(XML)을 {@link IMessageObject}로 변환 (deserialize)
 * - {@link IMessageObject}를 HTTP 응답 본문(XML)으로 변환 (serialize)
 * - 루트 노드명 지정 가능
 * - 전문 메타데이터를 기반으로 구조화된 XML 메시지를 생성 및 파싱
 * </pre>
 *
 * @param <T> 처리 대상 메시지 타입 (일반적으로 {@link IMessageObject})
 * 
 * @author sungdon.choi
 */
public class XmlMessageHttpConverter<T extends IMessageObject> extends AbstractMessageHttpConverter<T> {

    /** 기본 루트 노드 이름 */
    private static final String defaultRootNodeName = "message";

    /** XML 메시지 매퍼 */
    private final XmlMessageMapper xmlMessageMapper;

    /** XML 루트 노드 이름 */
    private final String rootNodeName;

    /**
     * 커스텀 루트 노드명을 지정하는 생성자
     *
     * @param rootNodeName     루트 노드 이름
     * @param xmlMessageMapper XML 메시지 매퍼
     */
    public XmlMessageHttpConverter(
            String rootNodeName,
            XmlMessageMapper xmlMessageMapper) {
        super(MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_XML, MediaType.TEXT_XML);
        this.rootNodeName = rootNodeName;
        this.xmlMessageMapper = xmlMessageMapper;
    }

    /**
     * 기본 루트 노드("message")를 사용하는 생성자
     *
     * @param xmlMessageMapper XML 메시지 매퍼
     */
    public XmlMessageHttpConverter(XmlMessageMapper xmlMessageMapper) {
        this(defaultRootNodeName, xmlMessageMapper);
    }

    /**
     * 지원 대상 클래스 여부 검사
     *
     * @param clazz 변환할 클래스 타입
     * @return 지원 여부
     */
    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return IMessageObject.class.isAssignableFrom(clazz);
    }

    /**
     * 입력 HTTP 메시지를 읽어 {@link IMessageObject}로 변환합니다.
     *
     * @param clazz        변환할 클래스 타입
     * @param inputMessage 입력 HTTP 메시지
     * @return 변환된 객체
     * @throws IOException                     변환 실패 시 발생
     * @throws HttpMessageNotReadableException 읽기 실패 시 발생
     */
    @Override
    @NonNull
    protected T readInternal(@NonNull Class<? extends T> clazz, @NonNull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        long length = inputMessage.getHeaders().getContentLength();
        byte[] requestBytes = inputMessage.getBody().readNBytes((int) length);

        Charset defaultCharset = this.resolveCharset(inputMessage,
                Charset.forName(RuntimeContext.getDefaultEncoding()));

        MessageContext messageContgext = this.createMessageContext(MessageFormat.XML, defaultCharset.name());
        MessageContextHolder.set(messageContgext);

        try {
            return this.xmlMessageMapper.deserialize(requestBytes, clazz, messageContgext);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("XML 데이터 역직렬화 오류", e, inputMessage);
        } finally {
            MessageContextHolder.clear();
        }
    }

    /**
     * {@link IMessageObject}를 HTTP 응답 본문(XML)으로 변환하여 작성합니다.
     *
     * @param t             변환할 객체
     * @param outputMessage 출력 HTTP 메시지
     * @throws HttpMessageNotWritableException 쓰기 실패 시 발생
     */
    @Override
    protected void writeInternal(@NonNull T t, @NonNull HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {

        Charset defaultCharset = this.resolveCharset(outputMessage,
                Charset.forName(RuntimeContext.getDefaultEncoding()));

        MessageContext messageContgext = this.createMessageContext(MessageFormat.XML, defaultCharset.name());
        MessageContextHolder.set(messageContgext);

        try {
            XmlMapper xmlMapper = this.xmlMessageMapper.getXmlMapper();
            IIntegrationMessageMetadataRegistrar metadataRegistrar = RuntimeContext
                    .getBean(IIntegrationMessageMetadataRegistrar.class);
            ObjectWriter writer = xmlMapper.writer().withRootName(rootNodeName);
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

            List<Field> fields = MessageUtils.getAllFields(t.getClass());
            for (Field field : fields) {
                Object child = MessageUtils.getFieldValue(t, field.getName());
                if (!(child instanceof IMessageObject msg)) {
                    continue;
                }

                IIntegrationMessageMetadata metadata = metadataRegistrar.getMetadata(child.getClass());
                if (metadata == null) {
                    continue;
                }

                byte[] bytes = this.xmlMessageMapper.serialize(msg, metadata, messageContgext);
                rootNode.set(field.getName(), xmlMapper.readTree(bytes));
            }

            byte[] responseBytes = writer.writeValueAsBytes(rootNode);
            outputMessage.getHeaders().setContentLength(responseBytes.length);
            outputMessage.getBody().write(responseBytes);
        } catch (Exception e) {
            throw new HttpMessageNotWritableException("XML 데이터 역직렬화 오류", e);
        } finally {
            MessageContextHolder.clear();
        }
    }
}
