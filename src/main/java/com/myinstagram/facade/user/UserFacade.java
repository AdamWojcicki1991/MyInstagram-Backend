package com.myinstagram.facade.user;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.custom.user.UserNotFoundByIdException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.mapper.UserMapper;
import com.myinstagram.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.myinstagram.domain.util.Constants.PICTURE_SAVED_TO_SERVER;
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
    private final RoleServiceDb roleServiceDb;
    private final UserFacadeUtils userFacadeUtils;
    private final VerificationTokenServiceDb verificationTokenServiceDb;

    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Get available users!");
        List<UserDto> users = userMapper.mapToUsersDto(userServiceDb.getAllUsers());
        return new ResponseEntity<>(users, OK);
    }

    public ResponseEntity<List<UserDto>> getUsersByLogin(final String login) {
        log.info("Get users contains passed login!");
        return userFacadeUtils.getUserByLoginIfExists(login);
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
        return userFacadeUtils.updateProfileIfUserIsValidated(userRequest, user);
    }

    public ResponseEntity<String> uploadUserImage(final MultipartFile image) {
        log.info("Upload user image!");
        String result = imageService.loadUserImage(image, 1L);
        return imageService.getResponseIfImageUploaded(result, PICTURE_SAVED_TO_SERVER);
    }

    public ResponseEntity<String> changePassword(final PasswordRequest passwordRequest) {
        log.info("Try to change user password!");
        User user = userServiceDb.getUserByLogin(passwordRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(passwordRequest.getLogin()));
        return userFacadeUtils.changePasswordIfUserPasswordIsValidated(passwordRequest, user);
    }

    public ResponseEntity<UserDto> resetPassword(final String email) {
        log.info("Try to reset user password!");
        User user = userServiceDb.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        UserDto userDto = userMapper.mapToUserDto(userService.resetUserPassword(user));
        return new ResponseEntity<>(userDto, OK);
    }

    public ResponseEntity<String> deleteUser(final String login) {
        log.info("Delete user by login: " + login);
        User user = userServiceDb.getUserByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        List<VerificationToken> verificationTokens = verificationTokenServiceDb.getUserValidVerificationToken(user);
        List<Role> roles = roleServiceDb.getRolesByUserLogin(user.getLogin());
        return userFacadeUtils.deleteValidatedUser(user, verificationTokens, roles);
    }
}
