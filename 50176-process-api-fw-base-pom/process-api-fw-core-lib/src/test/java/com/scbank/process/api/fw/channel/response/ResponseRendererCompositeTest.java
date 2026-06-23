package com.scbank.process.api.fw.channel.response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.message.IResponseMessage;

/**
 * ResponseRendererComposite Test Class
 */
@ExtendWith(MockitoExtension.class)
class ResponseRendererCompositeTest {

    private ResponseRendererComposite composite;

    @Mock
    private IResponseRenderer<IResponseMessage<?, ?>, Object> renderer1;

    @Mock
    private IResponseRenderer<IResponseMessage<?, ?>, Object> renderer2;

    @Mock
    private IResponseMessage<Object, Object> responseMessage;

    @Mock
    private IServiceContext serviceContext;

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create composite with renderers list")
        void shouldCreateCompositeWithRenderersList() {
            List<IResponseRenderer<?, ?>> renderers = new ArrayList<>();
            renderers.add(renderer1);

            composite = new ResponseRendererComposite(renderers);

            assertNotNull(composite);
        }

        @Test
        @DisplayName("Should create composite with empty renderers list")
        void shouldCreateCompositeWithEmptyRenderersList() {
            composite = new ResponseRendererComposite(Collections.emptyList());

            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("render tests")
    class RenderTests {

        @Test
        @DisplayName("Should render with first supporting renderer")
        void shouldRenderWithFirstSupportingRenderer() {
            ResponseEntity<?> expectedResponse = ResponseEntity.ok("success");

            when(renderer1.supports(responseMessage, serviceContext)).thenReturn(true);
            doReturn(expectedResponse).when(renderer1).render(responseMessage, serviceContext);

            List<IResponseRenderer<?, ?>> renderers = new ArrayList<>();
            renderers.add(renderer1);
            renderers.add(renderer2);

            composite = new ResponseRendererComposite(renderers);

            ResponseEntity<?> result = composite.render(responseMessage, serviceContext);

            assertSame(expectedResponse, result);
            verify(renderer1).supports(responseMessage, serviceContext);
            verify(renderer1).render(responseMessage, serviceContext);
            verify(renderer2, never()).supports(any(), any());
        }

        @Test
        @DisplayName("Should skip non-supporting renderer and use next")
        void shouldSkipNonSupportingRendererAndUseNext() {
            ResponseEntity<?> expectedResponse = ResponseEntity.ok("success from renderer2");

            when(renderer1.supports(responseMessage, serviceContext)).thenReturn(false);
            when(renderer2.supports(responseMessage, serviceContext)).thenReturn(true);
            doReturn(expectedResponse).when(renderer2).render(responseMessage, serviceContext);

            List<IResponseRenderer<?, ?>> renderers = new ArrayList<>();
            renderers.add(renderer1);
            renderers.add(renderer2);

            composite = new ResponseRendererComposite(renderers);

            ResponseEntity<?> result = composite.render(responseMessage, serviceContext);

            assertSame(expectedResponse, result);
            verify(renderer1).supports(responseMessage, serviceContext);
            verify(renderer1, never()).render(any(), any());
            verify(renderer2).supports(responseMessage, serviceContext);
            verify(renderer2).render(responseMessage, serviceContext);
        }

        @Test
        @DisplayName("Should throw IllegalStateException when no renderer supports")
        void shouldThrowIllegalStateExceptionWhenNoRendererSupports() {
            when(renderer1.supports(responseMessage, serviceContext)).thenReturn(false);
            when(renderer2.supports(responseMessage, serviceContext)).thenReturn(false);

            List<IResponseRenderer<?, ?>> renderers = new ArrayList<>();
            renderers.add(renderer1);
            renderers.add(renderer2);

            composite = new ResponseRendererComposite(renderers);

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                composite.render(responseMessage, serviceContext);
            });

            assertTrue(exception.getMessage().contains("지원하는 ResponseRenderer가 존재하지 않습니다"));
        }

        @Test
        @DisplayName("Should throw IllegalStateException when renderers list is empty")
        void shouldThrowIllegalStateExceptionWhenRenderersListIsEmpty() {
            composite = new ResponseRendererComposite(Collections.emptyList());

            assertThrows(IllegalStateException.class, () -> {
                composite.render(responseMessage, serviceContext);
            });
        }
    }

    @Nested
    @DisplayName("Renderer order tests")
    class RendererOrderTests {

        @Test
        @DisplayName("Should check renderers in order")
        void shouldCheckRenderersInOrder() {
            ResponseEntity<?> expectedResponse = ResponseEntity.ok("success");

            when(renderer1.supports(responseMessage, serviceContext)).thenReturn(false);
            when(renderer2.supports(responseMessage, serviceContext)).thenReturn(true);
            doReturn(expectedResponse).when(renderer2).render(responseMessage, serviceContext);

            List<IResponseRenderer<?, ?>> renderers = new ArrayList<>();
            renderers.add(renderer1);
            renderers.add(renderer2);

            composite = new ResponseRendererComposite(renderers);
            composite.render(responseMessage, serviceContext);

            // Verify order of calls
            var inOrder = inOrder(renderer1, renderer2);
            inOrder.verify(renderer1).supports(responseMessage, serviceContext);
            inOrder.verify(renderer2).supports(responseMessage, serviceContext);
        }
    }

    @Nested
    @DisplayName("Single renderer tests")
    class SingleRendererTests {

        @Test
        @DisplayName("Should work with single renderer")
        void shouldWorkWithSingleRenderer() {
            ResponseEntity<?> expectedResponse = ResponseEntity.ok("single renderer success");

            when(renderer1.supports(responseMessage, serviceContext)).thenReturn(true);
            doReturn(expectedResponse).when(renderer1).render(responseMessage, serviceContext);

            composite = new ResponseRendererComposite(Collections.singletonList(renderer1));

            ResponseEntity<?> result = composite.render(responseMessage, serviceContext);

            assertSame(expectedResponse, result);
        }
    }
}
