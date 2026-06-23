package com.scbank.process.api.fw.message.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import lombok.Getter;
import lombok.Setter;

/**
 * MessageContext 생성 팩토리
 *
 * <p>
 * MessageFormat, 인코딩, 직렬화/역직렬화 옵션을 받아서
 * {@link MessageContext}를 생성합니다.
 * </p>
 * 
 * (확장 가능: 추후 crypto, field converter 등 설정 추가 가능)
 * 
 * @author sungdon.choi
 * 
 */
@Component
public class MessageContextFactory {

  /**
   * 전문처리 디버그 로그 사용여부
   */
  @Getter
  @Setter
  @Value("${csl.message.use-debug-log:false}")
  private boolean useDebugLog;

  /**
   * MessageFormat과 인코딩만 받아 기본 MessageContext를 생성합니다.
   *
   * @param format   메시지 포맷
   * @param encoding 문자 인코딩
   * @return MessageContext
   */
  public MessageContext create(MessageFormat format, String encoding) {
    return new MessageContext.Builder()
        .format(format)
        .defaultEncoding(encoding)
        .useDebugLog(useDebugLog)
        .deserializationOptions(DeserializationOptions.getDefaultOptions(format).get())
        .serializationOptions(SerializationOptions.getDefaultOptions(format).get())
        .build();
  }

  /**
   * MessageFormat + 인코딩 + 옵션(Serialization, Deserialization)까지 받아 MessageContext를
   * 생성합니다.
   *
   * @param format                 메시지 포맷
   * @param encoding               문자 인코딩
   * @param serializationOptions   직렬화 옵션
   * @param deserializationOptions 역직렬화 옵션
   * @return MessageContext
   */
  public MessageContext create(MessageFormat format, String encoding,
      SerializationOptions serializationOptions,
      DeserializationOptions deserializationOptions) {
    return new MessageContext.Builder()
        .format(format)
        .defaultEncoding(encoding)
        .useDebugLog(useDebugLog)
        .serializationOptions(serializationOptions)
        .deserializationOptions(deserializationOptions)
        .build();
  }

  public MessageContext merge(MessageContext original, SerializationOptions serializationOptions,
      DeserializationOptions deserializationOptions) {
    return new MessageContext.Builder()
        .format(original.getFormat())
        .defaultEncoding(original.getDefaultEncoding())
        .useDebugLog(original.isUseDebugLog())
        .encryptProcessorRegistrar(original.getEncryptProcessorRegistrar())
        .messageFieldConverterRegistry(original.getMessageFieldConverterRegistry())
        .serializationOptions(
            serializationOptions != null ? serializationOptions : original.getSerializationOptions())
        .deserializationOptions(
            deserializationOptions != null ? deserializationOptions : original.getDeserializationOptions())
        .build();
  }
}
