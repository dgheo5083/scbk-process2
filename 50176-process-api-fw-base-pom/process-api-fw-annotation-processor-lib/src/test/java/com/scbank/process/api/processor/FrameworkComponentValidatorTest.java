package com.scbank.process.api.processor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.component.SharedComponent;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * FrameworkComponentValidator Test Class
 *
 * @author test
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FrameworkComponentValidatorTest {

    private FrameworkComponentValidator validator;

    @Mock
    private ProcessingEnvironment processingEnv;

    @Mock
    private Messager messager;

    @Mock
    private Types types;

    @Mock
    private Elements elements;

    @Mock
    private RoundEnvironment roundEnv;

    @BeforeEach
    void setUp() {
        validator = new FrameworkComponentValidator();

        when(processingEnv.getMessager()).thenReturn(messager);
        when(processingEnv.getTypeUtils()).thenReturn(types);
        when(processingEnv.getElementUtils()).thenReturn(elements);

        // Initialize the processor with the mock processing environment
        validator.init(processingEnv);
    }

    private void setupEmptyRoundEnv() {
        doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
        doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
        doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
        doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);
    }

    @Nested
    @DisplayName("getSupportedAnnotationTypes() tests")
    class GetSupportedAnnotationTypesTests {

        @Test
        @DisplayName("Should return all supported annotation types")
        void getSupportedAnnotationTypes_ShouldReturnAllTypes() {
            // when
            Set<String> supportedTypes = validator.getSupportedAnnotationTypes();

            // then
            assertNotNull(supportedTypes);
            assertEquals(4, supportedTypes.size());
            assertTrue(supportedTypes.contains(CommonComponent.class.getCanonicalName()));
            assertTrue(supportedTypes.contains(ServiceComponent.class.getCanonicalName()));
            assertTrue(supportedTypes.contains(SharedComponent.class.getCanonicalName()));
            assertTrue(supportedTypes.contains(DaoComponent.class.getCanonicalName()));
        }

        @Test
        @DisplayName("Should return immutable set")
        void getSupportedAnnotationTypes_ShouldReturnImmutableSet() {
            // when
            Set<String> supportedTypes = validator.getSupportedAnnotationTypes();

            // then
            assertThrows(UnsupportedOperationException.class, () -> {
                supportedTypes.add("some.annotation");
            });
        }
    }

    @Nested
    @DisplayName("getSupportedSourceVersion() tests")
    class GetSupportedSourceVersionTests {

        @Test
        @DisplayName("Should return RELEASE_21")
        void getSupportedSourceVersion_ShouldReturnRelease21() {
            // when
            SourceVersion version = validator.getSupportedSourceVersion();

            // then
            assertEquals(SourceVersion.RELEASE_21, version);
        }
    }

    @Nested
    @DisplayName("process() tests")
    class ProcessTests {

        @Test
        @DisplayName("Should return true after processing")
        void process_ShouldReturnTrue() {
            // given
            setupEmptyRoundEnv();

            // when
            boolean result = validator.process(Collections.emptySet(), roundEnv);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should process all annotation types")
        void process_ShouldProcessAllAnnotationTypes() {
            // given
            setupEmptyRoundEnv();

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            verify(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            verify(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            verify(roundEnv).getElementsAnnotatedWith(DaoComponent.class);
        }
    }

    @Nested
    @DisplayName("checkServiceComponent() tests")
    class CheckServiceComponentTests {

        @Test
        @DisplayName("Should error when ServiceComponent is not on class")
        void checkServiceComponent_ShouldErrorWhenNotOnClass() {
            // given
            Element interfaceElement = mock(Element.class);
            when(interfaceElement.getKind()).thenReturn(ElementKind.INTERFACE);

            doReturn(Set.of(interfaceElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("ServiceComponent"),
                    eq(interfaceElement));
        }

        @Test
        @DisplayName("Should error when public method without ServiceEndpoint annotation")
        void checkServiceComponent_ShouldErrorWhenPublicMethodWithoutServiceEndpoint() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            Name methodName = mock(Name.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getSimpleName()).thenReturn(methodName);
            when(methodElement.getAnnotation(ServiceEndpoint.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(methodName.toString()).thenReturn("testMethod");

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("@ServiceEndpoint"),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should not error when public method has ServiceEndpoint annotation")
        void checkServiceComponent_ShouldNotErrorWhenPublicMethodHasServiceEndpoint() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            ServiceEndpoint serviceEndpoint = mock(ServiceEndpoint.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ServiceEndpoint.class)).thenReturn(serviceEndpoint);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should error when field has ServiceComponent type")
        void checkServiceComponent_ShouldErrorWhenFieldHasServiceComponentType() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            Element fieldElement = mock(Element.class);
            TypeMirror fieldType = mock(TypeMirror.class);
            TypeElement fieldTypeElement = mock(TypeElement.class);
            ServiceComponent serviceComponentAnnotation = mock(ServiceComponent.class);
            Name fieldName = mock(Name.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(fieldElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(fieldElement.getKind()).thenReturn(ElementKind.FIELD);
            when(fieldElement.asType()).thenReturn(fieldType);
            when(fieldElement.getSimpleName()).thenReturn(fieldName);
            when(fieldName.toString()).thenReturn("testField");

            when(types.asElement(fieldType)).thenReturn(fieldTypeElement);
            when(fieldTypeElement.getAnnotation(ServiceComponent.class)).thenReturn(serviceComponentAnnotation);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("@ServiceComponent"),
                    eq(fieldElement));
        }

        @Test
        @DisplayName("Should skip lifecycle methods with PostConstruct")
        void checkServiceComponent_ShouldSkipPostConstructMethods() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            PostConstruct postConstruct = mock(PostConstruct.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ServiceEndpoint.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(postConstruct);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should skip lifecycle methods with PreDestroy")
        void checkServiceComponent_ShouldSkipPreDestroyMethods() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            PreDestroy preDestroy = mock(PreDestroy.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ServiceEndpoint.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(preDestroy);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should skip lifecycle methods with EventListener")
        void checkServiceComponent_ShouldSkipEventListenerMethods() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            EventListener eventListener = mock(EventListener.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ServiceEndpoint.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(eventListener);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should skip lifecycle methods with Scheduled")
        void checkServiceComponent_ShouldSkipScheduledMethods() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            Scheduled scheduled = mock(Scheduled.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ServiceEndpoint.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(scheduled);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }
    }

    @Nested
    @DisplayName("checkClassComponent() tests")
    class CheckClassComponentTests {

        @Test
        @DisplayName("Should error when SharedComponent is not on class")
        void checkClassComponent_ShouldErrorWhenSharedComponentNotOnClass() {
            // given
            Element interfaceElement = mock(Element.class);
            when(interfaceElement.getKind()).thenReturn(ElementKind.INTERFACE);

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(interfaceElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("SharedComponent"),
                    eq(interfaceElement));
        }

        @Test
        @DisplayName("Should error when CommonComponent is not on class")
        void checkClassComponent_ShouldErrorWhenCommonComponentNotOnClass() {
            // given
            Element enumElement = mock(Element.class);
            when(enumElement.getKind()).thenReturn(ElementKind.ENUM);

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Set.of(enumElement)).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("CommonComponent"),
                    eq(enumElement));
        }

        @Test
        @DisplayName("Should error when public method without ComponentOperation annotation")
        void checkClassComponent_ShouldErrorWhenPublicMethodWithoutComponentOperation() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            Name methodName = mock(Name.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getSimpleName()).thenReturn(methodName);
            when(methodElement.getAnnotation(ComponentOperation.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(methodName.toString()).thenReturn("testMethod");

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("@ComponentOperation"),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should not error when public method has ComponentOperation annotation")
        void checkClassComponent_ShouldNotErrorWhenPublicMethodHasComponentOperation() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            ComponentOperation componentOperation = mock(ComponentOperation.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ComponentOperation.class)).thenReturn(componentOperation);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should not error for private methods")
        void checkClassComponent_ShouldNotErrorForPrivateMethods() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PRIVATE));
            when(methodElement.getAnnotation(ComponentOperation.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }
    }

    @Nested
    @DisplayName("checkInterfaceComponent() tests")
    class CheckInterfaceComponentTests {

        @Test
        @DisplayName("Should error when DaoComponent is not on interface")
        void checkInterfaceComponent_ShouldErrorWhenNotOnInterface() {
            // given
            Element classElement = mock(Element.class);
            when(classElement.getKind()).thenReturn(ElementKind.CLASS);

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("DaoComponent"),
                    eq(classElement));
        }

        @Test
        @DisplayName("Should error when interface public method without ComponentOperation")
        void checkInterfaceComponent_ShouldErrorWhenPublicMethodWithoutComponentOperation() {
            // given
            Element interfaceElement = mock(Element.class);
            Element methodElement = mock(Element.class);
            Name methodName = mock(Name.class);

            when(interfaceElement.getKind()).thenReturn(ElementKind.INTERFACE);
            doReturn(List.of(methodElement)).when(interfaceElement).getEnclosedElements();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getSimpleName()).thenReturn(methodName);
            when(methodElement.getAnnotation(ComponentOperation.class)).thenReturn(null);

            when(methodName.toString()).thenReturn("testMethod");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Set.of(interfaceElement)).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    contains("@ComponentOperation"),
                    eq(methodElement));
        }

        @Test
        @DisplayName("Should not error when interface public method has ComponentOperation")
        void checkInterfaceComponent_ShouldNotErrorWhenPublicMethodHasComponentOperation() {
            // given
            Element interfaceElement = mock(Element.class);
            Element methodElement = mock(Element.class);
            ComponentOperation componentOperation = mock(ComponentOperation.class);

            when(interfaceElement.getKind()).thenReturn(ElementKind.INTERFACE);
            doReturn(List.of(methodElement)).when(interfaceElement).getEnclosedElements();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ComponentOperation.class)).thenReturn(componentOperation);

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Set.of(interfaceElement)).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }
    }

    @Nested
    @DisplayName("isSpringOverrideMethod() tests")
    class IsSpringOverrideMethodTests {

        @Test
        @DisplayName("Should skip Spring override methods")
        void shouldSkipSpringOverrideMethods() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement methodElement = mock(ExecutableElement.class);
            TypeMirror superClass = mock(TypeMirror.class);
            TypeElement superTypeElement = mock(TypeElement.class);
            ExecutableElement superMethod = mock(ExecutableElement.class);
            PackageElement packageElement = mock(PackageElement.class);
            Name packageName = mock(Name.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(methodElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
            when(methodElement.getModifiers()).thenReturn(Set.of(Modifier.PUBLIC));
            when(methodElement.getAnnotation(ComponentOperation.class)).thenReturn(null);
            when(methodElement.getAnnotation(PostConstruct.class)).thenReturn(null);
            when(methodElement.getAnnotation(PreDestroy.class)).thenReturn(null);
            when(methodElement.getAnnotation(EventListener.class)).thenReturn(null);
            when(methodElement.getAnnotation(Scheduled.class)).thenReturn(null);

            when(superClass.toString()).thenReturn("org.springframework.web.servlet.HandlerInterceptor");

            when(types.asElement(superClass)).thenReturn(superTypeElement);
            when(superTypeElement.getSuperclass()).thenReturn(null);
            doReturn(Collections.emptyList()).when(superTypeElement).getInterfaces();
            doReturn(List.of(superMethod)).when(superTypeElement).getEnclosedElements();

            when(superMethod.getKind()).thenReturn(ElementKind.METHOD);

            when(elements.overrides(methodElement, superMethod, classElement)).thenReturn(true);
            when(elements.getPackageOf(superTypeElement)).thenReturn(packageElement);
            when(packageElement.getQualifiedName()).thenReturn(packageName);
            when(packageName.toString()).thenReturn("org.springframework.web.servlet");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(methodElement));
        }
    }

    @Nested
    @DisplayName("Edge cases tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle constructor elements")
        void shouldHandleConstructorElements() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            ExecutableElement constructorElement = mock(ExecutableElement.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(constructorElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(constructorElement.getKind()).thenReturn(ElementKind.CONSTRUCTOR);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then - no error for constructor
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(constructorElement));
        }

        @Test
        @DisplayName("Should handle null field type element")
        void shouldHandleNullFieldTypeElement() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            Element fieldElement = mock(Element.class);
            TypeMirror fieldType = mock(TypeMirror.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(List.of(fieldElement)).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(fieldElement.getKind()).thenReturn(ElementKind.FIELD);
            when(fieldElement.asType()).thenReturn(fieldType);

            when(types.asElement(fieldType)).thenReturn(null);

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then - no error for null field type element
            verify(messager, never()).printMessage(
                    eq(Diagnostic.Kind.ERROR),
                    anyString(),
                    eq(fieldElement));
        }

        @Test
        @DisplayName("Should handle empty enclosed elements")
        void shouldHandleEmptyEnclosedElements() {
            // given
            TypeElement classElement = mock(TypeElement.class);
            TypeMirror superClass = mock(TypeMirror.class);

            when(classElement.getKind()).thenReturn(ElementKind.CLASS);
            doReturn(Collections.emptyList()).when(classElement).getEnclosedElements();
            when(classElement.getSuperclass()).thenReturn(superClass);
            doReturn(Collections.emptyList()).when(classElement).getInterfaces();

            when(superClass.toString()).thenReturn("java.lang.Object");

            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(ServiceComponent.class);
            doReturn(Set.of(classElement)).when(roundEnv).getElementsAnnotatedWith(SharedComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(CommonComponent.class);
            doReturn(Collections.emptySet()).when(roundEnv).getElementsAnnotatedWith(DaoComponent.class);

            // when
            validator.process(Collections.emptySet(), roundEnv);

            // then - no error for empty class
            verify(messager, never()).printMessage(any(), anyString(), any());
        }
    }
}
