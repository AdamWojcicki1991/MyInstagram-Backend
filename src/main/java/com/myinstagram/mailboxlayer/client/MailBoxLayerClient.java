package com.myinstagram.mailboxlayer.client;

import com.myinstagram.mailboxlayer.config.MailBoxLayerConfig;
import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailBoxLayerClient {
    private final RestTemplate restTemplate;
    private final MailBoxLayerConfig mailBoxLayerConfig;

    @Async
    public CompletableFuture<ValidateMailResponseDto> getValidatedEmailFromUrl(final String email) {
        return CompletableFuture.completedFuture(
                restTemplate.getForObject(getUri(email), ValidateMailResponseDto.class));
    }

    private URI getUri(final String email) {
        return UriComponentsBuilder.fromHttpUrl(mailBoxLayerConfig.getMailBoxLayerApiEndpoint())
                .queryParam("access_key", mailBoxLayerConfig.getMailBoxLayerApiKey())
                .queryParam("email", email)
                .build().encode().toUri();
    }
}
