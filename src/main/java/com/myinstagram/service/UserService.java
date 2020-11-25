package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.myinstagram.domain.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserServiceDb userServiceDb;
    private final RoleServiceDb roleServiceDb;

    public User createUser(final String name, final String login, final String email) {
        return null;
    }

    public User updateUser(final User user, final HashMap<String, String> request) {
        return userServiceDb.saveUser(User.builder()
                                              .id(user.getId())
                                              .userName(request.get("userName"))
                                              .login(user.getLogin())
                                              .password(user.getPassword())
                                              .email(request.get("email"))
                                              .description(request.get("description"))
                                              .createDate(user.getCreateDate())
                                              .userStatus(user.getUserStatus())
                                              .enabled(user.isEnabled())
                                              .posts(user.getPosts())
                                              .roles(user.getRoles())
                                              .build());
    }

    public void updateUserPassword(final User user, final String newPassword) {

    }

    public void resetUserPassword(final User user) {

    }

    public String createUserImage(final MultipartFile multipartFile,final Long userImageId) {
        try {
            Files.deleteIfExists(Paths.get(USER_FOLDER + "/" + userImageId + ".png"));
            Path path = Paths.get(USER_FOLDER + userImageId + ".png");
            Files.write(path, multipartFile.getBytes());
        } catch (IOException e) {
            log.error(PICTURE_SAVED);
            return PICTURE_SAVED;
        }
        log.info(PICTURE_SAVED_TO_SERVER);
        return PICTURE_SAVED_TO_SERVER;
    }
}
