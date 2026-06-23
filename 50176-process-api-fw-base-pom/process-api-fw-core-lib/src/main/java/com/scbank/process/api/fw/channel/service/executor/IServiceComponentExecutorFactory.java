package com.scbank.process.api.fw.channel.service.executor;

import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;

/**
 * 서비스 실행기 생성 팩토리 인터페이스입니다.
 * 
 * 주어진 {@link ServiceMethodMetadata}를 기반으로 실제 실행 가능한
 * {@link IServiceComponentExecutor} 인스턴스를 생성합니다.
 * 
 * 이 구조를 통해 리플렉션 기반 실행기뿐 아니라 프록시 기반 실행기, 트레이스 확장 등 다양한 구현체로의 확장이 가능합니다.
 * 
 * @author sungdon.choi
 */
@FunctionalInterface
public interface IServiceComponentExecutorFactory {

    /**
     * 서비스 메서드 메타데이터를 기반으로 실행기를 생성합니다.
     *
     * @param serviceMethodMetadata 실행 대상 서비스 메서드 메타데이터
     * @return 생성된 실행기 인스턴스
     * @throws Exception 실행기 생성 실패 시 예외
     */
    IServiceComponentExecutor create(ServiceMethodMetadata serviceMethodMetadata) throws Exception;
}
