package com.scbank.process.api.fw.message.metadata.registry.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.MessageProperties;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.metadata.DefaultIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.factory.impl.ConditionalFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.factory.impl.DefaultFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.factory.impl.ExtE2EIgnoreSegmentFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.factory.impl.RepeatedFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.factory.impl.SegmentFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.factory.impl.VariableLengthFieldMetadataFactory;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link IIntegrationMessageMetadataRegistrar}의 기본 구현체로,
 * {@link IntegrationMessage} 어노테이션이 붙은 클래스를 스캔하고 필드 메타데이터를 분석하여 캐시에 등록합니다.
 * <p>
 * 메시지 구조는 재귀적으로 분석되며, 각 필드의 타입에 따라 {@link IMessageFieldMetadataFactory} 구현을 통해
 * 처리됩니다.
 * </p>
 * 
 * @author Min-jun
 */
@Slf4j
@RequiredArgsConstructor
public class IntegrationMessageMetadataRegistrar
        implements IIntegrationMessageMetadataRegistrar, InitializingBean, DisposableBean {

    /** 구성 설정 정보 */
    private final MessageProperties properties;

    /** 클래스별 메타데이터 캐시 */
    private final Map<Class<?>, IIntegrationMessageMetadata> cache = new ConcurrentHashMap<>();

    /** IntegrationMessage 어노테이션 클래스 */
    private Class<? extends IntegrationMessage> annotationClass;

    /** 필드 처리 전략 팩토리 목록 */
    private final List<IMessageFieldMetadataFactory> factories = List.of(
            new RepeatedFieldMetadataFactory(this::extractFieldsRecursive),
            new VariableLengthFieldMetadataFactory(),
            new SegmentFieldMetadataFactory(),
            new ExtE2EIgnoreSegmentFieldMetadataFactory(),
            new ConditionalFieldMetadataFactory(),
            new DefaultFieldMetadataFactory());

    /**
     * Spring Bean 초기화 시 메타데이터 스캔을 수행합니다.
     * 
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        List<String> basePackages = this.properties.getBasePackages();
        this.annotationClass = this.properties.getAnnotationClass() != null
                ? this.properties.getAnnotationClass()
                : IntegrationMessage.class;

        log.info("# 프레임워크 전문 메타데이터 스캔 시작, 패키지 목록: {}", basePackages);

        this.scan(basePackages.toArray(String[]::new));

        log.info("# 프레임워크 전문 메타데이터 등록완료 개수: {}", cache.size());
    }

    /**
     * Spring Bean 종료 시 캐시를 정리합니다.
     * 
     * @see DisposableBean#destroy()
     */
    @Override
    public void destroy() {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.clear();
        }
    }

    /**
     * 지정된 basePackages 내에서 IntegrationMessage 어노테이션이 붙은 클래스를 스캔하고 메타데이터를 등록합니다.
     *
     * @param basePackages 분석 대상 패키지 목록
     */
    @Override
    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<Class<?>> classes = scanAnnotatedClasses(basePackage, this.annotationClass);
            for (Class<?> clazz : classes) {
                if (!IMessageObject.class.isAssignableFrom(clazz))
                    continue;

                IntegrationMessage annotation = clazz.getAnnotation(annotationClass);
                List<IMessageMetadata> fields = extractFieldsRecursive(clazz, annotation.id());

                IIntegrationMessageMetadata meta = DefaultIntegrationMessageMetadata.builder()
                        .id(annotation.id())
                        .type(annotation.type())
                        .targetClass(clazz)
                        .xmlRootWrap(annotation.xmlRootNameWrap())
                        .xmlRootName(annotation.xmlRootName())
                        .children(fields.stream().sorted().toList())
                        .build();

                if (log.isTraceEnabled()) {
                    log.trace("scan 완료 clazz={}", clazz);
                }
                cache.put(clazz, meta);
            }
        }
    }

    /**
     * 지정된 패키지 내 특정 어노테이션이 부착된 클래스를 스캔합니다.
     *
     * @param basePackage    스캔 대상 패키지 경로
     * @param annotationType 스캔할 어노테이션 타입
     * @return 어노테이션이 부착된 클래스 목록
     */
    private Set<Class<?>> scanAnnotatedClasses(String basePackage, Class<? extends Annotation> annotationType) {
        Set<Class<?>> result = new HashSet<>();

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        ResourceLoader resolver = new PathMatchingResourcePatternResolver(classLoader);
        CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        // scanner.setEnvironment(null);
        scanner.setResourceLoader(resolver);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            try {
                MetadataReader reader = readerFactory
                        .getMetadataReader(Objects.requireNonNull(bd.getBeanClassName()));
                Class<?> clazz = Class.forName(reader.getClassMetadata().getClassName());
                result.add(clazz);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 클래스 내 필드들을 재귀적으로 분석하여 메타데이터를 추출합니다.
     *
     * @param clazz      대상 클래스
     * @param parentPath 상위 필드 경로
     * @return 필드 메타데이터 목록
     */
    private List<IMessageMetadata> extractFieldsRecursive(Class<?> clazz, String parentPath) {
        List<IMessageMetadata> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        IntStream.range(0, fields.length).forEach(idx -> {
            Field field = fields[idx];
            field.setAccessible(true);

            if (!field.isAnnotationPresent(MessageField.class))
                return;

            String path = buildPath(parentPath, field.getName());

            for (IMessageFieldMetadataFactory factory : factories) {
                if (factory.supports(field)) {
                    IMessageMetadata metadata = factory.create(field, idx, path);
                    // 객체 타입일 경우 재귀적으로 하위 필드 분석
                    if (metadata instanceof DefaultMessageFieldMetadata m &&
                            m.getType() == MessageType.OBJECT &&
                            IMessageObject.class.isAssignableFrom(m.getFieldType())) {
                        List<IMessageMetadata> children = extractFieldsRecursive(m.getFieldType(), path);
                        if (!CollectionUtils.isEmpty(children)) {
                            m.setChildren(children.stream().sorted().toList());
                        }
                    }
                    result.add(metadata);
                    return;
                }
            }
        });
        return result;
    }

    /**
     * 경로 문자열을 생성합니다.
     *
     * @param parent  상위 경로
     * @param current 현재 필드명
     * @return 조합된 경로 문자열
     */
    private String buildPath(String parent, String current) {
        return parent.isEmpty() ? current : parent + "/" + current;
    }

    /**
     * 현재 등록된 모든 통합 메시지 메타데이터를 반환합니다.
     *
     * @return 전체 메타데이터 리스트
     */
    @Override
    public List<IIntegrationMessageMetadata> getAllMetadata() {
        return List.copyOf(cache.values());
    }

    /**
     * 특정 클래스에 대한 통합 메시지 메타데이터를 반환합니다.
     *
     * @param target 조회 대상 클래스
     * @return 해당 클래스에 매핑된 메타데이터, 없을 경우 null
     */
    @Override
    public IIntegrationMessageMetadata getMetadata(Class<?> target) {
        return cache.get(target);
    }
}
