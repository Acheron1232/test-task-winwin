package com.acheron.serviceb.api;

import com.acheron.serviceb.service.TransformService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransformApi.class)
public class TransformApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransformService transformService;

    @Test
    void testTransformEndpointWithValidRequest() throws Exception {
        when(transformService.transform(any(), any(), any())).thenReturn("HeLlO");

        mockMvc.perform(post("/api/transform")
                .header("X-Internal-Token", "my-secret-token")
                .content("hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("HeLlO"));
    }
}