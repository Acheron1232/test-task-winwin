package com.acheron.serviceb.api;

import com.acheron.serviceb.service.TransformService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")

public class TransformApi {
    private final TransformService transformService;

    public TransformApi(TransformService transformService) {
        this.transformService = transformService;
    }


    @PostMapping("/transform")
    public String transform(@RequestBody String text, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        return transformService.transform(text, request, response);
    }


}
