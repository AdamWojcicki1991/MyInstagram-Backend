package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;

import static com.myinstagram.domain.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserServiceDb userServiceDb;
    private final MailSenderService mailSenderService;
    private final MailCreationService mailCreationService;
    private final PasswordProcessorService passwordProcessorService;

    public User updateUserProfile(final User user, final HashMap<String, String> request) {
        User updatedUser = userServiceDb.saveUser(user.toBuilder()
                                                          .userName(request.get("userName"))
                                                          .email(request.get("email"))
                                                          .description(request.get("description"))
                                                          .build());
        mailSenderService.sendPersonalizedEmail(user.getEmail(), UPDATE_USER_EMAIL,
                                                mailCreationService.createUpdateUserProfileEmail(updatedUser));
        return updatedUser;
    }

    public User updateUserPassword(final User user, final String newPassword) {
        String encryptedPassword = passwordProcessorService.encryptPassword(newPassword);
        User userWithUpdatePassword = userServiceDb.saveUser(user.toBuilder().password(encryptedPassword).build());
        mailSenderService.sendPersonalizedEmail(user.getEmail(), UPDATE_USER_PASSWORD_EMAIL,
                                                mailCreationService.createResetPasswordEmail(user, newPassword));
        return userWithUpdatePassword;
    }

    public User resetUserPassword(final User user) {
        String password = passwordProcessorService.generateUuid();
        String encryptedPassword = passwordProcessorService.encryptPassword(password);
        User userWithResetPassword = userServiceDb.saveUser(user.toBuilder().password(encryptedPassword).build());
        mailSenderService.sendPersonalizedEmail(user.getEmail(), RESET_USER_PASSWORD_EMAIL,
                                                mailCreationService.createResetPasswordEmail(user, password));
        return userWithResetPassword;
    }
}
