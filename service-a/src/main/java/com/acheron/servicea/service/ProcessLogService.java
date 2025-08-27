package com.acheron.servicea.service;

import com.acheron.servicea.api.ProcessApi;
import com.acheron.servicea.entity.ProcessLog;
import com.acheron.servicea.repository.ProcessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessLogService {
    @Value("${service-b}")
    private String serviceB;

    @Value("${header_secret}")
    private String headerSecret;

    private final ProcessLogRepository processLogRepository;
    private final UserService userService;

    public ProcessLog save(ProcessLog processLog) {
        log.debug("Saving process log: {}", processLog);
        return processLogRepository.save(processLog);
    }

    public String process(ProcessApi.ProcessDto processDto) {
        log.info("Processing request with text: {}", processDto.text());

        RestClient restClient = RestClient.create();
        String body;

        try {
            body = restClient.post()
                    .uri(serviceB + "/api/transform")
                    .header("X-Internal-Token", headerSecret)
                    .body(processDto.text())
                    .retrieve()
                    .onStatus(status -> status.value() == 403, (request, response) -> {
                        log.error("Service-B returned 403 Forbidden (missing/invalid header)");
                        throw new RuntimeException("Missing/invalid header");
                    })
                    .body(String.class);

            log.debug("Response from Service-B: {}", body);
        } catch (Exception e) {
            log.error("Error while calling Service-B: {}", e.getMessage(), e);
            throw e;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication != null ? authentication.getName() : "anonymous";
        log.info("Saving processing log for user: {}", currentUserEmail);

        save(new ProcessLog(null,
                userService.findByEmail(currentUserEmail).cast(),
                processDto.text(),
                body,
                LocalDateTime.now()));

        return body;
    }
}
