package com.myinstagram.service;

import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.mailboxlayer.exceptions.MailBoxLayerApiException;
import com.myinstagram.mailboxlayer.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.myinstagram.domain.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserServiceDb userServiceDb;
    private final EmailValidator emailValidator;
    private final MailSenderService mailSenderService;
    private final MailCreationService mailCreationService;
    private final PasswordProcessorService passwordProcessorService;

    public User updateUserProfile(final User user, final UserRequest userRequest) {
        User updatedUser = userServiceDb.saveUser(user.toBuilder()
                                                          .userName(userRequest.getUserName())
                                                          .email(userRequest.getEmail())
                                                          .description(userRequest.getDescription())
                                                          .updateDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                                                          .build());
        mailSenderService.sendPersonalizedEmail(user.getEmail(), UPDATE_USER_EMAIL,
                                                mailCreationService.createUpdateUserProfileEmail(updatedUser));
        return updatedUser;
    }

    public User updateUserPassword(final User user, final String newPassword) {
        if (emailValidator.validateUserEmail(user.getEmail())) {
            String encryptedPassword = passwordProcessorService.encryptPassword(newPassword);
            User userWithUpdatePassword = userServiceDb.saveUser(user.toBuilder().password(encryptedPassword).build());
            mailSenderService.sendPersonalizedEmail(user.getEmail(), UPDATE_USER_PASSWORD_EMAIL,
                                                    mailCreationService.createResetPasswordEmail(user, newPassword));
            return userWithUpdatePassword;
        } else {
            throw new MailBoxLayerApiException(user.getEmail());
        }
    }

    public User resetUserPassword(final User user) {
        if (emailValidator.validateUserEmail(user.getEmail())) {
            String password = passwordProcessorService.generateUuid();
            String encryptedPassword = passwordProcessorService.encryptPassword(password);
            User userWithResetPassword = userServiceDb.saveUser(user.toBuilder().password(encryptedPassword).build());
            mailSenderService.sendPersonalizedEmail(user.getEmail(), RESET_USER_PASSWORD_EMAIL,
                                                    mailCreationService.createResetPasswordEmail(user, password));
            return userWithResetPassword;
        } else {
            throw new MailBoxLayerApiException(user.getEmail());
        }
    }
}
