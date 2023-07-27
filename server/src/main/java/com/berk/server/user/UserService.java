package com.berk.server.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() ->
                        new IllegalArgumentException("User with id " + id + " does not exist"));
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Long id, User updatedUser) {
        User user = getUserById(id);

        String newName = updatedUser.getName();

        if (newName != null) {
            user.setName(newName);
            userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
