package com.scbank.process.api.fw.channel.service.argument;

import java.util.List;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;

import lombok.extern.slf4j.Slf4j;

/**
 * 서비스 메서드의 파라미터 바인딩을 처리하는 Resolver Composite 클래스입니다.
 * 
 * 등록된 {@link IServiceMethodArgumentResolver} 중 supports 조건에 맞는 Resolver를 찾아
 * 인자를 해석하며, 내부적으로 캐싱을 통해 반복적인 바인딩 성능을 최적화합니다.
 *
 * <p>
 * Spring의 HandlerMethodArgumentResolverComposite 유사 구조
 * </p>
 */
@Slf4j
public class ServiceMethodArgumentResolverComposite {

    private final List<IServiceMethodArgumentResolver> resolvers;

    public ServiceMethodArgumentResolverComposite(List<IServiceMethodArgumentResolver> resolvers) {
        this.resolvers = resolvers;
    }

    /**
     * 파라미터 정보를 기반으로 메서드 인자 배열을 구성합니다.
     *
     * @param parameters 메서드 파라미터 메타데이터 목록
     * @param context    서비스 컨텍스트
     * @param input      입력 객체 (nullable)
     * @return Object[] 메서드에 전달할 인자 목록
     * @throws Exception 바인딩 실패 시
     */
    public Object[] resolveArguments(List<ParameterMetadata> parameters, IServiceContext context, Object input)
            throws Exception {
        Object[] args = new Object[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            ParameterMetadata param = parameters.get(i);

            for (IServiceMethodArgumentResolver resolver : resolvers) {
                if (resolver.supports(param)) {
                    args[i] = resolver.resolveArgument(param, context, input);
                    // resolverCache.put(cacheKey, resolver); // 캐시 등록
                    log.debug("Resolved [{}] using: {}", param.getName(), resolver.getClass().getSimpleName());
                    break;
                }
            }

            if (args[i] == null) {
                // throw new IllegalArgumentException("해석 가능한 ArgumentResolver가 없습니다: " +
                // param.getName());
            }
        }

        return args;
    }
}
