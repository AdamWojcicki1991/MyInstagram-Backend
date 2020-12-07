package com.myinstagram.validator;

import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.mailboxlayer.validator.EmailValidator;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createUserRequest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserEmailValidatorTestSuite {
    @InjectMocks
    private UserValidator userValidator;
    @Mock
    private RoleServiceDb roleServiceDb;
    @Mock
    private UserServiceDb userServiceDb;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private VerificationTokenServiceDb verificationTokenServiceDb;

    @Test
    public void isUserValidateToAssignEmail() {
        //GIVEN
        User user = createUser("login", "test@gmail.com");
        UserRequest userRequest = createUserRequest(user);
        given(userServiceDb.getAllUsersByEmailNoneMatch(anyString())).willReturn(true);
        given(emailValidator.validateUserEmail(anyString())).willReturn(true);
        //WHEN
        boolean userValidateToAssignEmail = userValidator.isUserValidateToAssignEmail(user, userRequest);
        //THEN
        assertTrue(userValidateToAssignEmail);
    }

    @Test
    public void isUserNotValidateToAssignEmail() {
        //GIVEN
        User user = createUser("login", "test@gmail.com");
        UserRequest userRequest = createUserRequest(user);
        given(userServiceDb.getAllUsersByEmailNoneMatch(anyString())).willReturn(true);
        given(emailValidator.validateUserEmail(anyString())).willReturn(false);
        //WHEN
        boolean userNotValidateToAssignEmail = userValidator.isUserValidateToAssignEmail(user, userRequest);
        //THEN
        assertFalse(userNotValidateToAssignEmail);
    }
}
