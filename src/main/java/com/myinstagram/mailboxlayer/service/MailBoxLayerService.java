package com.myinstagram.mailboxlayer.service;

import com.myinstagram.mailboxlayer.client.MailBoxLayerClient;
import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import com.myinstagram.mailboxlayer.exceptions.MailBoxLayerApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailBoxLayerService {
    private final MailBoxLayerClient mailBoxLayerClient;

    public ValidateMailResponseDto getValidateMailResponseFromClient(final String email) {
        log.info("Validation response from MailBoxLayer API sent for email: " + email);
        try {
            return mailBoxLayerClient.getValidatedEmailFromUrl(email).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new MailBoxLayerApiException(email);
        }
    }
}
