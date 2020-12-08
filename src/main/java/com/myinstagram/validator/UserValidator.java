package com.myinstagram.validator;

import com.myinstagram.domain.dto.RoleRequest;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.mailboxlayer.validator.EmailValidator;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserValidator {
    private final RoleServiceDb roleServiceDb;
    private final UserServiceDb userServiceDb;
    private final EmailValidator emailValidator;
    private final VerificationTokenServiceDb verificationTokenServiceDb;

    public boolean isUserValidated(final User user) {
        return validate("Validate that user is authorized!",
                        user.isEnabled(), user.getUserStatus().equals(ACTIVE));
    }

    public boolean isUserValidatedToLikePost(final User user, final Post post) {
        return validate("Validate that user is authorized and valid to like a post!",
                        isUserValidated(user), !user.getLikedPosts().contains(post));
    }

    public boolean isUserValidatedToUnlikePost(final User user, final Post post) {
        return validate("Validate that user is authorized and valid to unlike a post!",
                        isUserValidated(user), user.getLikedPosts().contains(post));
    }

    public boolean isUserValidateToAssignRole(final User user, final RoleRequest roleRequest) {
        return isUserValidated(user) && validateUserRole(roleRequest, user);
    }

    public boolean isUserValidateToAssignEmail(final User user, final UserRequest userRequest) {
        return isUserValidated(user) && validateUserEmail(userRequest);
    }

    public boolean hasUserVerificationToken(final User user) {
        log.info("Validate that user has assigned verification token!");
        return verificationTokenServiceDb.getUserValidVerificationToken(user).size() == 1;
    }

    public boolean hasUserRoles(final User user) {
        log.info("Validate that user has assigned roles!");
        return roleServiceDb.getRolesByUserLogin(user.getLogin()).size() > 0;
    }

    private boolean validate(final String info, final boolean userValidated, final boolean contains) {
        log.info(info);
        return userValidated && contains;
    }

    private boolean validateUserRole(final RoleRequest roleRequest, final User userFromDb) {
        log.info("Validate that user doesn't have assigned role!");
        return roleServiceDb.getRolesByRoleType(roleRequest.getRoleType()).stream()
                .flatMap(role -> role.getUsers().stream())
                .noneMatch(user -> userFromDb.getId().equals(user.getId()));
    }

    private boolean isValidateUserEmail(final UserRequest userRequest) {
        log.info("Validate that email address is correct!");
        return emailValidator.validateUserEmail(userRequest.getEmail());
    }

    private boolean isNoneEmailMatch(final UserRequest userRequest) {
        log.info("Validate that email address is not already occupied!");
        return userServiceDb.getAllUsersByEmailNoneMatch(userRequest.getEmail());
    }

    private boolean validateUserEmail(final UserRequest userRequest) {
        log.info("Email validation process started!");
        return (isNoneEmailMatch(userRequest) && isValidateUserEmail(userRequest));
    }
}
