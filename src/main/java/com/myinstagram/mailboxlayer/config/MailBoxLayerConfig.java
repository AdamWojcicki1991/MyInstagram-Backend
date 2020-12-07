package com.myinstagram.mailboxlayer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MailBoxLayerConfig {
    @Value("${mailboxlayer.api.endpoint.prod}")
    private String mailBoxLayerApiEndpoint;
    @Value("${mailboxlayer.api.key}")
    private String mailBoxLayerApiKey;
}
