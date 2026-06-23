package com.scbank.process.api.fw.core.encrypt;

/**
 * 암복호화 처리기 등록소 (EncryptProcessor Registry)
 * <p>
 * 암호화 유형별로 {@link IEncryptProcessor} 구현체를 등록 및 조회하는 레지스트리 인터페이스입니다.
 * </p>
 *
 * <ul>
 * <li>암호화 타입(`type`) 문자열을 키로 등록</li>
 * <li>유형에 따라 다양한 암복호화 전략을 선택적으로 적용 가능</li>
 * <li>등록되지 않은 타입에 대한 조회 시 예외 또는 null 반환 가능 (구현에 따라)</li>
 * </ul>
 *
 * <p>
 * 예: "aes", "rsa", "base64", "custom:account" 등으로 분기 처리 가능
 * </p>
 *
 * @see IEncryptProcessor
 * @author sungdon.choi
 */
public interface IEncryptProcessorRegistrar {

    /**
     * 암복호화 처리기를 등록
     *
     * @param type             등록할 암호화 유형 키
     * @param encryptProcessor 해당 유형에 대한 암복호화 처리기
     * @param <S>              평문 타입
     * @param <T>              암호문 타입
     */
    <S, T> void register(String type, IEncryptProcessor<S, T> encryptProcessor);

    /**
     * 암복호화 처리기를 조회
     *
     * @param type 암호화 유형 키
     * @param <S>  평문 타입
     * @param <T>  암호문 타입
     * @return 해당 유형의 암복호화 처리기
     */
    <S, T> IEncryptProcessor<S, T> get(String type);

    /**
     * 특정 암호화 유형이 등록되어 있는지 확인
     *
     * @param type 암호화 유형 키
     * @return 등록 여부
     */
    boolean contains(String type);
}
