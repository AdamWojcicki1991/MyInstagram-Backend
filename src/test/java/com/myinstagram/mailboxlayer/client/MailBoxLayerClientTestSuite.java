package com.myinstagram.mailboxlayer.client;

import com.myinstagram.mailboxlayer.config.MailBoxLayerConfig;
import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.myinstagram.util.DtoDataFixture.createValidateResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MailBoxLayerClientTestSuite {
    @InjectMocks
    private MailBoxLayerClient mailBoxLayerClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private MailBoxLayerConfig mailBoxLayerConfig;

    @BeforeEach
    public void setUp() {
        given(mailBoxLayerConfig.getMailBoxLayerApiEndpoint()).willReturn("http://test.net/check");
        given(mailBoxLayerConfig.getMailBoxLayerApiKey()).willReturn("test");
    }

    @Test
    public void shouldGetValidatedEmailFromUrl() throws URISyntaxException, ExecutionException, InterruptedException {
        //GIVEN
        ValidateMailResponseDto validateResponseDto = createValidateResponseDto(true, true, true);
        URI uri = new URI("http://test.net/check" + "?access_key=test&email=test@gmail.com");
        given(restTemplate.getForObject(uri, ValidateMailResponseDto.class)).willReturn(validateResponseDto);
        //WHEN
        CompletableFuture<ValidateMailResponseDto> validatedEmailFromUrl = mailBoxLayerClient.getValidatedEmailFromUrl("test@gmail.com");
        //THEN
        assertEquals("test@gmail.com", validatedEmailFromUrl.get().getValidatedEmail());
        assertTrue(validatedEmailFromUrl.get().isEmailFormatValid());
        assertTrue(validatedEmailFromUrl.get().isMxRecordsFound());
        assertTrue(validatedEmailFromUrl.get().isSmtpValid());
    }
}
