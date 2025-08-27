package com.acheron.servicea.api;

import com.acheron.servicea.service.ProcessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcessApi {
    private final ProcessLogService processLogService;


    @PostMapping("/process")
    public String process(@RequestBody ProcessDto processDto) {
        return processLogService.process(processDto);
    }

    public record ProcessDto(String text) {
    }
}
