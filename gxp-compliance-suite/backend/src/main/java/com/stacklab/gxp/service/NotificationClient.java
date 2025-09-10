package com.stacklab.gxp.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class NotificationClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://mock-notify.example.com") // replace with real
            .build();

    @CircuitBreaker(name = "notificationClient", fallbackMethod = "fallback")
    @Retry(name = "notificationClient")
    public Mono<String> send(String to, String message) {
        return webClient.post()
                .uri("/send")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("to", to, "message", message)))
                .retrieve()
                .bodyToMono(String.class);
    }

    private Mono<String> fallback(String to, String message, Throwable ex) {
        return Mono.just("queued-local:" + to);
    }
}
