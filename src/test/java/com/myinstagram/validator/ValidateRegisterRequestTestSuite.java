package com.myinstagram.validator;

import com.myinstagram.domain.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.myinstagram.util.DataFixture.createRegisterRequest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ValidateRegisterRequestTestSuite {
    @Autowired
    private ValidateRegisterRequest validateRegisterRequest;

    @Test
    public void isRegisterRequestValid() {
        //GIVEN
        RegisterRequest registerRequest = createRegisterRequest("name", "login",
                                                                "password", "test@gmail.com", "Poznan");
        //WHEN
        boolean result = validateRegisterRequest.isRegisterRequestValid(registerRequest);
        //THEN
        assertTrue(result);
    }

    @Test
    public void isRegisterRequestNotValid() {
        //GIVEN
        RegisterRequest blankRegisterRequest = createRegisterRequest("", "login",
                                                                     "password", "test@gmail.com", "Poznan");
        RegisterRequest nullRegisterRequest = createRegisterRequest(null, "login",
                                                                    "password", "test@gmail.com", "Poznan");
        //WHEN
        boolean resultForBlankField = validateRegisterRequest.isRegisterRequestValid(blankRegisterRequest);
        boolean resultForNullField = validateRegisterRequest.isRegisterRequestValid(nullRegisterRequest);
        //THEN
        assertFalse(resultForBlankField);
        assertFalse(resultForNullField);
    }
}
