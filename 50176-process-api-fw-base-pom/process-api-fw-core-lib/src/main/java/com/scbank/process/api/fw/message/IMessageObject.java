package com.scbank.process.api.fw.message;

import java.io.Serializable;

/**
 * 프레임워크 메시지 객체의 기본 인터페이스입니다.
 * 
 * <p>
 * 모든 요청(Request) 및 응답(Response) DTO는 이 인터페이스를 반드시 구현해야 하며,
 * 메시지 직렬화/역직렬화, 필드 마스킹, 변환기 처리 등 프레임워크의 다양한 컴포넌트에서
 * 메시지 객체의 기준 타입으로 활용됩니다.
 * </p>
 *
 * <p>
 * {@link java.io.Serializable}을 확장하므로, 네트워크 전송 또는 파일 저장 등에도 사용할 수 있습니다.
 * </p>
 *
 * @author sungdon.choi
 */
public interface IMessageObject extends Serializable {

}
