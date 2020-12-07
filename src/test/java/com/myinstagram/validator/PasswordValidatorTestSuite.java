package com.myinstagram.validator;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.service.PasswordProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createPasswordRequest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordValidatorTestSuite {
    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private PasswordProcessorService passwordProcessorService;

    @Test
    public void shouldValidateNewPasswordWithConfirmed() {
        //GIVEN
        PasswordRequest passwordRequest = createPasswordRequest("123", "321", "321");
        //WHEN
        boolean result = passwordValidator.validateNewPasswordWithConfirmed(passwordRequest);
        //THEN
        assertFalse(result);
    }

    @Test
    public void shouldNotValidateNewPasswordWithConfirmed() {
        //GIVEN
        PasswordRequest passwordRequest = createPasswordRequest("321", "123", "321");
        //WHEN
        boolean result = passwordValidator.validateNewPasswordWithConfirmed(passwordRequest);
        //THEN
        assertTrue(result);
    }

    @Test
    public void shouldValidatePasswords() {
        //GIVEN
        String encryptPassword = passwordProcessorService.encryptPassword("Password");
        User user = createUser("login_1", "email1@gmail.com").toBuilder().password(encryptPassword).build();
        PasswordRequest passwordRequest = createPasswordRequest("Password", null, "123");
        //WHEN
        boolean result = passwordValidator.validatePasswords(user, passwordRequest);
        //THEN
        assertTrue(result);
    }

    @Test
    public void shouldNotValidatePasswords() {
        //GIVEN
        String encryptPassword = passwordProcessorService.encryptPassword("Password");
        User user = createUser("login_1", "email1@gmail.com").toBuilder().password(encryptPassword).build();
        PasswordRequest firstPasswordRequest = createPasswordRequest("Password", "123", "");
        PasswordRequest secondPasswordRequest = createPasswordRequest("Pass", "123", "123");
        //WHEN
        boolean firstResult = passwordValidator.validatePasswords(user, firstPasswordRequest);
        boolean secondResult = passwordValidator.validatePasswords(user, secondPasswordRequest);
        //THEN
        assertFalse(firstResult);
        assertFalse(secondResult);
    }
}
