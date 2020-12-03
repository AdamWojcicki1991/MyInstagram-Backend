package com.myinstagram.mailboxlayer.facade;

import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import com.myinstagram.mailboxlayer.service.MailBoxLayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailBoxLayerFacade {
    private final MailBoxLayerService mailBoxLayerService;

    public ResponseEntity<ValidateMailResponseDto> getValidateMailResponse(final String email) {
        log.info("Get validated response for email: " + email);
        ValidateMailResponseDto validateMailResponseDto = mailBoxLayerService.getValidateMailResponseFromClient(email);
        return new ResponseEntity<>(validateMailResponseDto, OK);
    }
}
