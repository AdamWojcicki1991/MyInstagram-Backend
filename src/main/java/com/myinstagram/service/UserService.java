package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class UserService implements UserRepository {

    @Override
    public User createUser(String name, String login, String email) {
        return null;
    }

    @Override
    public User updateUser(User user, HashMap<String, String> request) {
        return null;
    }

    @Override
    public String createUserImage(MultipartFile multipartFile, Long userImageId) {
        return null;
    }

    @Override
    public void updateUserPassword(User user, String newPassword) {

    }

    @Override
    public void resetUserPassword(User user) {

    }
}
