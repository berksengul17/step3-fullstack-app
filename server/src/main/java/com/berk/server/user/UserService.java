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
                        new UserNotFoundException("User with id " + id + " does not exist"));
    }

    public User createUser(User user) {
        boolean existsName = userRepository
                .existsByName(user.getName());

        if(existsName)
            throw new IllegalArgumentException(
                    "Name " + user.getName() + " is taken."
            );

        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);

        String newName = updatedUser.getName();

        if (newName != null) {
            user.setName(newName);
            return userRepository.save(user);
        }

        return null;
    }

    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }
}
