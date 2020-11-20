package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class UserServiceDb {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByLoginContaining(final String login) {
        return userRepository.findAllByLoginContaining(login);
    }

    public Optional<User> getUserById(final Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByLogin(final String login) {
        return userRepository.findByLogin(login);
    }

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(final Long id) {
        userRepository.deleteById(id);
    }
}
