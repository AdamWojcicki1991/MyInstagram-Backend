package com.myinstagram.facade.user;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.custom.security.ChangePasswordException;
import com.myinstagram.exceptions.custom.security.PasswordNotMatchedException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.exceptions.custom.user.UserValidationException;
import com.myinstagram.mapper.UserMapper;
import com.myinstagram.service.UserService;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import com.myinstagram.validator.PasswordValidator;
import com.myinstagram.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

import static com.myinstagram.domain.enums.ValidationStatus.AUTHORIZED_CONTAINS_EMAIL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
class UserFacadeUtils {
    private final UserMapper userMapper;
    private final UserService userService;
    private final UserServiceDb userServiceDb;
    private final UserValidator userValidator;
    private final PasswordValidator passwordValidator;
    private final VerificationTokenServiceDb verificationTokenServiceDb;

    ResponseEntity<List<UserDto>> getUserByLoginIfExists(final String login) {
        try {
            List<UserDto> users = userMapper.mapToUsersDto(userServiceDb.getAllUsersByLoginContaining(login));
            log.info("Users returned successfully!");
            return new ResponseEntity<>(users, OK);
        } catch (Exception e) {
            throw new UserNotFoundException(login);
        }
    }

    ResponseEntity<UserDto> updateProfileIfUserIsValidated(final UserRequest userRequest, final User user) {
        if (userValidator.isUserValidateToAssignEmail(user, userRequest)) {
            UserDto userDto = userMapper.mapToUserDto(userService.updateUserProfile(user, userRequest));
            log.info("User profile updated successfully!");
            return new ResponseEntity<>(userDto, OK);
        } else {
            throw new UserValidationException(user.getLogin(), AUTHORIZED_CONTAINS_EMAIL);
        }
    }

    ResponseEntity<String> deleteUserWithValidatedToken(final User user, final List<VerificationToken> verificationTokens) {
        if (userValidator.hasUserVerificationToken(user)) {
            verificationTokenServiceDb.deleteVerificationToken(verificationTokens.get(0));
        }
        userServiceDb.deleteUser(user);
        log.info("User deleted successfully!");
        return new ResponseEntity<>("User Deleted Successfully!", OK);
    }

    ResponseEntity<String> changePasswordIfUserPasswordIsValidated(final PasswordRequest passwordRequest, final User user) {
        if (passwordValidator.validateNewPasswordWithConfirmed(passwordRequest))
            throw new PasswordNotMatchedException();
        try {
            if (passwordValidator.validatePasswords(user, passwordRequest)) {
                userService.updateUserPassword(user, passwordRequest.getNewPassword());
            } else {
                log.info("Users password changed failed!");
                return new ResponseEntity<>("Current Password is Incorrect", BAD_REQUEST);
            }
            log.info("Users password changed successfully!");
            return new ResponseEntity<>("Password Changed Successfully!", OK);
        } catch (Exception e) {
            throw new ChangePasswordException();
        }
    }
}
