package com.scbank.process.api.fw.message.context;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

import com.scbank.process.api.fw.core.encrypt.IEncryptProcessorRegistrar;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.evaluate.ConditionEvaluatorComposite;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import lombok.Getter;
import lombok.Setter;

/**
 * 전문 변환 컨텍스트 객체.
 *
 * <p>
 * 이 객체는 전문 변환 작업(직렬화/역직렬화) 과정에서 필요한 다양한 설정과, 필드별 변환 결과값, 인덱스 관리 등을 담당한다.
 * </p>
 *
 * 주요 책임:
 * <ul>
 * <li>필드 경로별 변환 결과 값 관리 (path → value)</li>
 * <li>로그 출력용 필드 값 관리</li>
 * <li>리스트/반복 필드의 인덱스 관리</li>
 * <li>직렬화/역직렬화 옵션, 인코딩, 메시지 포맷 정보 보관</li>
 * <li>암호화 프로세서 및 변환기 레지스트리 보관</li>
 * </ul>
 *
 * @see SerializationOptions
 * @see DeserializationOptions
 * @see IMessageFieldConverterRegistry
 * @see IEncryptProcessorRegistrar
 * @see MessageFormat
 *
 * @author sungdon.choi
 */
@Getter
@Setter
public class MessageContext {

    /** 필드 경로(path)별 변환된 값 저장 */
    private final Map<String, Object> pathValues = new LinkedHashMap<>();

    /** 로그 출력용 필드 경로(path)별 값 저장 */
    private final Map<String, Object> logValues = new LinkedHashMap<>();

    /** 반복 필드 인덱스 스택 (e.g., 리스트 구조의 현재 위치 관리) */
    private final Deque<Integer> indexStack = new ArrayDeque<>();

    /** 직렬화 옵션 */
    private SerializationOptions serializationOptions = new SerializationOptions();

    /** 역직렬화 옵션 */
    private DeserializationOptions deserializationOptions = new DeserializationOptions();

    /** 기본 문자 인코딩 (ex: UTF-8) */
    private String defaultEncoding;

    /** 메시지 포맷 (FIXEDLENGTH, JSON, XML 등) */
    private MessageFormat format;

    /** 암호화 프로세서 레지스트리 */
    private IEncryptProcessorRegistrar encryptProcessorRegistrar;

    /** 필드 변환기 레지스트리 */
    private IMessageFieldConverterRegistry messageFieldConverterRegistry;

    /**
     * 조건부 필드 처리 ConditionEvaluator Composite
     */
    private ConditionEvaluatorComposite conditionEvaluator = new ConditionEvaluatorComposite();

    /**
     * 전문처리 디버그 로그 사용여부
     */
    private boolean useDebugLog;
    
    private int remainLength;
    
    private byte[] remainBytes;

    /**
     * 변환된 필드 값을 경로(path) 기준으로 저장한다.
     *
     * @param fieldPath 필드 경로
     * @param value     저장할 값
     */
    public void addPathValue(String fieldPath, Object value) {
        this.pathValues.put(fieldPath, value);
    }

    /**
     * 저장된 필드 값을 경로(path) 기준으로 조회한다.
     *
     * @param fieldPath 필드 경로
     * @return 저장된 값 (없으면 null)
     */
    public Object getPathValue(String fieldPath) {
        return this.pathValues.get(fieldPath);
    }

    /**
     * 로그 출력용 필드 값을 경로(path) 기준으로 저장한다.
     *
     * @param path  필드 경로
     * @param value 로그 출력용 값
     */
    public void addLogValue(String path, String value) {
        this.logValues.put(path, value);
    }

    /**
     * 현재 반복 필드 인덱스를 스택에 추가한다.
     *
     * @param i 인덱스 값
     */
    public void pushIndex(int i) {
        this.indexStack.push(i);
    }

    /**
     * 현재 반복 필드 인덱스를 스택에서 제거한다.
     */
    public void popIndex() {
        this.indexStack.pop();
    }

    /**
     * 주어진 경로 템플릿에 현재 인덱스를 적용하여 최종 경로를 생성한다.
     *
     * <p>
     * 예: "items[*].name" → "items[0].name"
     * </p>
     *
     * @param pathTemplate 경로 템플릿
     * @return 인덱스가 치환된 최종 경로
     */
    public String resolvePath(String pathTemplate) {
        String resolved = pathTemplate;
        for (int i : indexStack) {
            resolved = resolved.replaceFirst("\\[\\*\\]", "[" + i + "]");
        }
        return resolved;
    }

    /**
     * {@link MessageContext} 객체를 생성하는 빌더 클래스.
     */
    public static class Builder {

        private final MessageContext ctx = new MessageContext();

        /**
         * 직렬화 옵션을 설정한다.
         */
        public Builder serializationOptions(SerializationOptions options) {
            ctx.setSerializationOptions(options);
            return this;
        }

        /**
         * 역직렬화 옵션을 설정한다.
         */
        public Builder deserializationOptions(DeserializationOptions options) {
            ctx.setDeserializationOptions(options);
            return this;
        }

        /**
         * 기본 인코딩을 설정한다.
         */
        public Builder defaultEncoding(String encoding) {
            ctx.setDefaultEncoding(encoding);
            return this;
        }

        /**
         * 메시지 포맷을 설정한다.
         */
        public Builder format(MessageFormat format) {
            ctx.setFormat(format);
            return this;
        }

        /**
         * 암호화 프로세서 레지스트리를 설정한다.
         */
        public Builder encryptProcessorRegistrar(IEncryptProcessorRegistrar registry) {
            ctx.setEncryptProcessorRegistrar(registry);
            return this;
        }

        /**
         * 메시지 필드 변환기 레지스트리를 설정한다.
         */
        public Builder messageFieldConverterRegistry(IMessageFieldConverterRegistry registry) {
            ctx.setMessageFieldConverterRegistry(registry);
            return this;
        }

        public Builder useDebugLog(boolean useDebugLog) {
            ctx.setUseDebugLog(useDebugLog);
            return this;
        }

        /**
         * 최종 {@link MessageContext} 인스턴스를 반환한다.
         *
         * @return 구성된 MessageContext 객체
         */
        public MessageContext build() {
            return ctx;
        }
    }
}
