package com.scbank.process.api.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.auto.service.AutoService;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.component.SharedComponent;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * 프레임워크 컴포넌트 어노테이션 유효성 검사 프로세서입니다.
 * <p>
 */
@AutoService(Processor.class)
public class FrameworkComponentValidator extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(
                CommonComponent.class.getCanonicalName(),
                ServiceComponent.class.getCanonicalName(),
                SharedComponent.class.getCanonicalName(),
                DaoComponent.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_21;
    }

    /**
     * 어노테이션 프로세서 진입점. 각 라운드마다 검사 수행.
     *
     * @param annotations 어노테이션 타입
     * @param roundEnv    라운드 환경
     * @return 처리 여부
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        checkServiceComponent(roundEnv, ServiceComponent.class);
        // checkClassComponent(roundEnv, ServiceComponent.class);
        checkClassComponent(roundEnv, SharedComponent.class);
        checkClassComponent(roundEnv, CommonComponent.class);
        checkInterfaceComponent(roundEnv, DaoComponent.class);
        return true;
    }

    /**
     * 클래스에 적용 가능한 컴포넌트 어노테이션의 유효성을 검사합니다.
     *
     * @param roundEnv        라운드 환경
     * @param annotationClass 검사 대상 어노테이션 클래스
     */
    private void checkServiceComponent(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            if (element.getKind() != ElementKind.CLASS) {
                this.error(element, "@" + annotationClass.getSimpleName() + " 는 클래스에만 사용할 수 있습니다.");
                continue;
            }

            TypeElement classType = (TypeElement) element;
            for (Element enclosed : classType.getEnclosedElements()) {

                // 필드에 다른 @ServiceComponent 선언 제한
                if (enclosed.getKind() == ElementKind.FIELD) {
                    TypeMirror fieldType = enclosed.asType();
                    Element fieldTypeElement = processingEnv.getTypeUtils().asElement(fieldType);

                    if (fieldTypeElement != null &&
                            fieldTypeElement.getAnnotation(ServiceComponent.class) != null) {
                        this.error(enclosed, "다른 @ServiceComponent 타입을 필드로 선언할 수 없습니다. 필드명: '%s'",
                                enclosed.getSimpleName());
                    }
                } else {
                    if (!(enclosed instanceof ExecutableElement)) {
                        continue;
                    }

                    ExecutableElement method = (ExecutableElement) enclosed;

                    if (method.getKind() == ElementKind.METHOD && this.isSpringOverrideMethod(classType, method)) {
                        continue;
                    }

                    if (method.getKind() == ElementKind.METHOD && this.isLifecycleMethod(method)) {
                        continue;
                    }

                    // public 메서드는 반드시 @ComponentOperation 필요
                    if (enclosed.getKind() == ElementKind.METHOD &&
                            enclosed.getModifiers().contains(Modifier.PUBLIC) &&
                            (enclosed.getAnnotation(ServiceEndpoint.class) == null)) {

                        this.error(enclosed, "public 메서드 '%s' 에는 @ServiceEndpoint 어노테이션이 필요합니다.",
                                enclosed.getSimpleName());
                    }
                }
            }
        }
    }

    /**
     * 클래스에 적용 가능한 컴포넌트 어노테이션의 유효성을 검사합니다.
     *
     * @param roundEnv        라운드 환경
     * @param annotationClass 검사 대상 어노테이션 클래스
     */
    private void checkClassComponent(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            if (element.getKind() != ElementKind.CLASS) {
                this.error(element, "@" + annotationClass.getSimpleName() + " 는 클래스에만 사용할 수 있습니다.");
                continue;
            }

            TypeElement classType = (TypeElement) element;
            for (Element enclosed : classType.getEnclosedElements()) {

                // 필드에 다른 @ServiceComponent 선언 제한
                if (enclosed.getKind() == ElementKind.FIELD) {
                    TypeMirror fieldType = enclosed.asType();
                    Element fieldTypeElement = processingEnv.getTypeUtils().asElement(fieldType);

                    if (fieldTypeElement != null &&
                            fieldTypeElement.getAnnotation(ServiceComponent.class) != null) {
                        this.error(enclosed, "다른 @ServiceComponent 타입을 필드로 선언할 수 없습니다. 필드명: '%s'",
                                enclosed.getSimpleName());
                    }
                } else {
                    if (!(enclosed instanceof ExecutableElement)) {
                        continue;
                    }

                    ExecutableElement method = (ExecutableElement) enclosed;

                    if (method.getKind() == ElementKind.METHOD && this.isSpringOverrideMethod(classType, method)) {
                        continue;
                    }

                    if (method.getKind() == ElementKind.METHOD && this.isLifecycleMethod(method)) {
                        continue;
                    }

                    // public 메서드는 반드시 @ComponentOperation 필요
                    if (method.getKind() == ElementKind.METHOD &&
                            method.getModifiers().contains(Modifier.PUBLIC) &&
                            (method.getAnnotation(ComponentOperation.class) == null)) {

                        this.error(method, "public 메서드 '%s' 에는 @ComponentOperation 어노테이션이 필요합니다.",
                                method.getSimpleName());
                    }
                }
            }
        }
    }

    /**
     * 인터페이스에 적용 가능한 컴포넌트 어노테이션의 유효성을 검사합니다.
     *
     * @param roundEnv        라운드 환경
     * @param annotationClass 검사 대상 어노테이션 클래스
     */
    private void checkInterfaceComponent(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            if (element.getKind() != ElementKind.INTERFACE) {
                this.error(element, "@" + annotationClass.getSimpleName() + " 는 인터페이스에만 사용할 수 있습니다.");
                continue;
            }

            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.METHOD &&
                        enclosed.getModifiers().contains(Modifier.PUBLIC) &&
                        enclosed.getAnnotation(ComponentOperation.class) == null) {

                    this.error(enclosed, "인터페이스의 public 메서드 '%s' 에는 @ComponentOperation 어노테이션이 필요합니다.",
                            enclosed.getSimpleName());
                }
            }
        }
    }

    /**
     * 메시지를 출력하는 헬퍼 함수
     *
     * @param element 대상 엘리먼트
     * @param msg     출력 메시지 포맷
     * @param args    메시지 포맷 인자
     */
    private void error(Element element, String msg, Object... args) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                element);
    }

    /**
     * 스프링 패키지 인터페이스 override 메소트 여부를 확인한다.
     * 
     * @param ownerType
     * @param method
     * @return
     */
    private boolean isSpringOverrideMethod(TypeElement ownerType, ExecutableElement method) {
        Elements elements = processingEnv.getElementUtils();
        Types types = processingEnv.getTypeUtils();

        Deque<TypeMirror> queue = new ArrayDeque<>();
        TypeMirror superClass = ownerType.getSuperclass();
        if (superClass != null && !superClass.toString().equals("java.lang.Object")) {
            queue.add(superClass);
        }
        queue.addAll(ownerType.getInterfaces());

        while (!queue.isEmpty()) {
            TypeMirror t = queue.poll();
            if (!(types.asElement(t) instanceof TypeElement superType)) {
                continue;
            }

            for (Element e : superType.getEnclosedElements()) {
                if (e.getKind() != ElementKind.METHOD) {
                    continue;
                }

                ExecutableElement superMethod = (ExecutableElement) e;

                if (elements.overrides(method, superMethod, ownerType)) {
                    PackageElement pkg = elements.getPackageOf(superType);
                    String packageName = pkg.getQualifiedName().toString();
                    if (packageName.startsWith("org.springframework.")) {
                        return true;
                    }
                }
            }

            TypeMirror sc = superType.getSuperclass();
            if (sc != null && !sc.toString().equals("java.lang.Object")) {
                queue.add(sc);
            }
            queue.addAll(superType.getInterfaces());
        }

        return false;
    }

    /**
     * 라이프사이클 메소드여부
     * @param method
     */
    private boolean isLifecycleMethod(ExecutableElement method) {
        return method.getAnnotation(PostConstruct.class) != null ||
                method.getAnnotation(PreDestroy.class) != null ||
                method.getAnnotation(EventListener.class) != null ||
                method.getAnnotation(Scheduled.class) != null;
    }
}
