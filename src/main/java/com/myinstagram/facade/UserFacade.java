package com.myinstagram.facade;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.*;
import com.myinstagram.mapper.UserMapper;
import com.myinstagram.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;
import static com.myinstagram.domain.enums.ValidationStatus.AUTHORIZED_CONTAINS_EMAIL;
import static com.myinstagram.domain.util.Constants.PICTURE_SAVED_TO_SERVER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserFacade {
    private final UserMapper userMapper;
    private final UserService userService;
    private final ImageService imageService;
    private final UserServiceDb userServiceDb;
    private final PasswordProcessorService passwordProcessorService;
    private final VerificationTokenServiceDb verificationTokenServiceDb;

    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Get available users!");
        List<UserDto> users = userMapper.mapToUsersDto(userServiceDb.getAllUsers());
        return new ResponseEntity<>(users, OK);
    }

    public ResponseEntity<List<UserDto>> getUsersByLogin(final String login) {
        log.info("Get users contains passed login!");
        try {
            List<UserDto> users = userMapper.mapToUsersDto(userServiceDb.getAllUsersByLoginContaining(login));
            return new ResponseEntity<>(users, OK);
        } catch (Exception e) {
            throw new UserNotFoundException(login);
        }
    }

    public ResponseEntity<UserDto> getUserById(final Long id) {
        log.info("Get user by id: " + id);
        UserDto userDto = userMapper.mapToUserDto(userServiceDb.getUserById(id)
                                                          .orElseThrow(() -> new UserNotFoundByIdException(id)));
        return new ResponseEntity<>(userDto, OK);
    }

    public ResponseEntity<UserDto> getUserByLogin(final String login) {
        log.info("Get user by login: " + login);
        UserDto userDto = userMapper.mapToUserDto(userServiceDb.getUserByLogin(login)
                                                          .orElseThrow(() -> new UserNotFoundException(login)));
        return new ResponseEntity<>(userDto, OK);
    }

    public ResponseEntity<UserDto> getUserByMail(final String mail) {
        log.info("Get user by mail: " + mail);
        UserDto userDto = userMapper.mapToUserDto(userServiceDb.getUserByEmail(mail)
                                                          .orElseThrow(() -> new UserNotFoundException(mail)));
        return new ResponseEntity<>(userDto, OK);
    }

    public ResponseEntity<UserDto> updateProfile(final UserRequest userRequest) {
        log.info("Try to update user profile!");
        User user = userServiceDb.getUserById(userRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundByIdException(userRequest.getUserId()));
        List<User> users = userServiceDb.getAllUsers().stream()
                .filter(userInDb -> userInDb.getEmail().contains(userRequest.getEmail()))
                .collect(Collectors.toList());
        if (user.isEnabled() && user.getUserStatus().equals(ACTIVE) && users.isEmpty()) {
            UserDto userDto = userMapper.mapToUserDto(userService.updateUserProfile(user, userRequest));
            return new ResponseEntity<>(userDto, OK);
        } else {
            throw new UserValidationException(user.getLogin(), AUTHORIZED_CONTAINS_EMAIL);
        }
    }

    public ResponseEntity<String> deleteUser(String login) {
        log.info("Delete user by login: " + login);
        User user = userServiceDb.getUserByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        List<VerificationToken> verificationTokens = verificationTokenServiceDb.getAllVerificationTokens().stream()
                .filter(verificationToken -> verificationToken.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
        if (verificationTokens.size() == 1) verificationTokenServiceDb.deleteVerificationToken(verificationTokens.get(0));
        userServiceDb.deleteUser(user);
        return new ResponseEntity<>("User Deleted Successfully!", OK);
    }

    public ResponseEntity<String> uploadUserImage(final MultipartFile image) {
        log.info("Upload user image!");
        String result = imageService.loadUserImage(image, 1L);
        return (result.equals(PICTURE_SAVED_TO_SERVER)) ?
                new ResponseEntity<>(result, OK) :
                new ResponseEntity<>(result, BAD_REQUEST);
    }

    public ResponseEntity<String> changePassword(final PasswordRequest passwordRequest) {
        log.info("Try to change user password!");
        User user = userServiceDb.getUserByLogin(passwordRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(passwordRequest.getLogin()));
        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) throw new PasswordNotMatchedException();
        try {
            if (!passwordRequest.getNewPassword().isBlank() && passwordProcessorService.isEncryptedPasswordMatching(
                    passwordRequest.getCurrentPassword(), user.getPassword())) {
                userService.updateUserPassword(user, passwordRequest.getNewPassword());
            } else {
                return new ResponseEntity<>("Current Password is Incorrect", BAD_REQUEST);
            }
            return new ResponseEntity<>("Password Changed Successfully!", OK);
        } catch (Exception e) {
            throw new ChangePasswordException();
        }
    }

    public ResponseEntity<UserDto> resetPassword(final String email) {
        log.info("Try to reset user password!");
        User user = userServiceDb.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        UserDto userDto = userMapper.mapToUserDto(userService.resetUserPassword(user));
        return new ResponseEntity<>(userDto, OK);
    }
}
