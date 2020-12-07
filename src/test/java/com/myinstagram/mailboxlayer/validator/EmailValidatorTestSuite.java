package com.myinstagram.mailboxlayer.validator;

import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import com.myinstagram.mailboxlayer.service.MailBoxLayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.myinstagram.util.DtoDataFixture.createValidateResponseDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTestSuite {
    @InjectMocks
    private EmailValidator emailValidator;
    @Mock
    private MailBoxLayerService mailBoxLayerService;

    @Test
    public void shouldValidateUserEmail() {
        //GIVEN
        String email = "test@gmail.com";
        ValidateMailResponseDto validateResponseDto = createValidateResponseDto(true, true, true);
        given(mailBoxLayerService.getValidateMailResponseFromClient(email)).willReturn(validateResponseDto);
        //WHEN
        boolean validateUserEmail = emailValidator.validateUserEmail("test@gmail.com");
        //THEN
        assertTrue(validateUserEmail);
    }

    @Test
    public void shouldNotValidateUserEmail() {
        //GIVEN
        String email = "test@gmail.com";
        ValidateMailResponseDto validateResponseDto = createValidateResponseDto(false, true, true);
        given(mailBoxLayerService.getValidateMailResponseFromClient(email)).willReturn(validateResponseDto);
        //WHEN
        boolean validateUserEmail = emailValidator.validateUserEmail("test@gmail.com");
        //THEN
        assertFalse(validateUserEmail);
    }
}
