package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.repository.UserDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceDb {
    private final UserDbRepository userDbRepository;

    public List<User> getAllUsers() {
        return userDbRepository.findAll();
    }

    public List<User> getAllUsersByLoginContaining(final String login) {
        return userDbRepository.findAllByLoginContaining(login);
    }

    public Optional<User> getUserById(final Long id) {
        return userDbRepository.findById(id);
    }

    public Optional<User> getUserByEmail(final String email) {
        return userDbRepository.findByEmail(email);
    }

    public Optional<User> getUserByLogin(final String login) {
        return userDbRepository.findByLogin(login);
    }

    public User saveUser(final User user) {
        return userDbRepository.save(user);
    }

    public void deleteUserById(final Long id) {
        userDbRepository.deleteById(id);
    }

    public void deleteUser(final User user) {
        userDbRepository.delete(user);
    }
}
