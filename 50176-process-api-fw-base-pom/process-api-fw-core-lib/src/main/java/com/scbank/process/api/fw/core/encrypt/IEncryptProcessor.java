package com.scbank.process.api.fw.core.encrypt;

/**
 * 암복호화 처리기 인터페이스
 * <p>
 * 제네릭 타입 {@code S} → {@code T}로 암호화하고, {@code T} → {@code S}로 복호화하는 양방향 변환 처리용
 * 인터페이스입니다.
 * </p>
 *
 * <ul>
 * <li>{@code S} – 평문(Plaintext) 타입</li>
 * <li>{@code T} – 암호문(Ciphertext) 타입</li>
 * </ul>
 *
 * <p>
 * 이 인터페이스를 통해 다양한 암호화 전략 (예: String → String, String → byte[], 객체 전체 → 암호화 DTO
 * 등)을 정의할 수 있습니다.
 * </p>
 *
 * @param <S> 평문 타입
 * @param <T> 암호문 타입
 *
 * @see EncryptException
 * @see DecryptException
 * @author sungdon.choi
 */
public interface IEncryptProcessor<S, T> {

    /**
     * S 타입의 평문 데이터를 T 타입으로 암호화
     *
     * @param source S 타입의 평문 데이터
     * @return T 타입의 암호화 데이터
     * @throws EncryptException 암호화 실패 시 예외
     */
    T encrypt(S source) throws EncryptException;

    /**
     * T 타입의 암호문 데이터를 S 타입으로 복호화
     *
     * @param target T 타입의 암호화 데이터
     * @return S 타입의 복호화 평문 데이터
     * @throws DecryptException 복호화 실패 시 예외
     */
    S decrypt(T target) throws DecryptException;
}
