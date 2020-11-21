package com.myinstagram.repository;

import com.myinstagram.domain.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Repository
public interface UserRepository {
    User createUser(String name, String login, String email);

    User updateUser(User user, HashMap<String, String> request);

    String createUserImage(MultipartFile multipartFile, Long userImageId);

    void updateUserPassword(User user, String newPassword);

    void resetUserPassword(User user);
}
