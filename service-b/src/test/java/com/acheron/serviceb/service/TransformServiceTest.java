package com.acheron.serviceb.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransformServiceTest {

    @InjectMocks
    private TransformService transformService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        try(AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this)){
            ReflectionTestUtils.setField(transformService, "headerSecret", "my-secret-token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testTransformWithValidHeader() throws IOException {
        when(request.getHeader("X-Internal-Token")).thenReturn("my-secret-token");
        String result = transformService.transform("hello", request, response);
        assertEquals("HeLlO", result);
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    void testTransformWithInvalidHeader() throws IOException {
        when(request.getHeader("X-Internal-Token")).thenReturn("wrong-token");
        String result = transformService.transform("hello", request, response);
        assertEquals("", result);
        verify(response).sendError(403, "Invalid/missing header");
    }
}