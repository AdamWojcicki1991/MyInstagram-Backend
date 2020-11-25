package com.myinstagram.service;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.myinstagram.domain.enums.RoleType.USER;
import static com.myinstagram.domain.enums.UserStatus.ACTIVE;
import static com.myinstagram.domain.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final ImageService imageService;
    private final UserServiceDb userServiceDb;
    private final RoleServiceDb roleServiceDb;
    private final MailSenderService mailSenderService;
    private final MailCreationService mailCreationService;
    private final PasswordProcessorService passwordProcessorService;

    public User registerUser(final String name, final String login, final String email) {
        String password = passwordProcessorService.generateRandomPassword();
        String encryptedPassword = passwordProcessorService.encryptPassword(password);
        User userWithRole = createUserWithRole(name, login, email, encryptedPassword);
        imageService.loadDefaultUserImage(userWithRole);
        mailSenderService.sendPersonalizedEmail(
                email,
                NEW_USER_EMAIL,
                mailCreationService.createNewUserEmail(userWithRole, password));
        return userWithRole;
    }

    public User updateUserProfile(final User user, final HashMap<String, String> request) {
        User updatedUser = userServiceDb.saveUser(user.toBuilder()
                .userName(request.get("userName"))
                .email(request.get("email"))
                .description(request.get("description"))
                .build());
        mailSenderService.sendPersonalizedEmail(
                user.getEmail(),
                UPDATE_USER_EMAIL,
                mailCreationService.createUpdateUserProfileEmail(updatedUser));
        return updatedUser;
    }

    public User updateUserPassword(final User user, final String newPassword) {
        String encryptedPassword = passwordProcessorService.encryptPassword(newPassword);
        User userWithUpdatePassword = userServiceDb.saveUser(user.toBuilder().password(encryptedPassword).build());
        mailSenderService.sendPersonalizedEmail(
                user.getEmail(),
                UPDATE_USER_PASSWORD_EMAIL,
                mailCreationService.createResetPasswordEmail(user, newPassword));
        return userWithUpdatePassword;
    }

    public User resetUserPassword(final User user) {
        String password = passwordProcessorService.generateRandomPassword();
        String encryptedPassword = passwordProcessorService.encryptPassword(password);
        User userWithResetPassword = userServiceDb.saveUser(user.toBuilder().password(encryptedPassword).build());
        mailSenderService.sendPersonalizedEmail(
                user.getEmail(),
                RESET_USER_PASSWORD_EMAIL,
                mailCreationService.createResetPasswordEmail(user, password));
        return userWithResetPassword;
    }

    private User createUserWithRole(final String name, final String login,
                                    final String email, final String encryptedPassword) {
        User user = userServiceDb.saveUser(User.builder()
                .userName(name)
                .login(login)
                .password(encryptedPassword)
                .email(email)
                .description("")
                .createDate(LocalDate.now())
                .userStatus(ACTIVE)
                .enabled(true)
                .posts(new ArrayList<>())
                .roles(new HashSet<>())
                .build());
        roleServiceDb.saveRole(Role.builder()
                .roleType(USER)
                .users(new HashSet<>(Set.of(user)))
                .build());
        return user;
    }
}
