package com.berk.server.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private List<User> mockUsers;

    @BeforeEach
    public void setUp() {
        mockUsers = Arrays.asList(new User(1L, "User1"), new User(2L, "User2"));
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(mockUsers.size(), users.size());
        assertEquals(mockUsers.get(0).getName(), users.get(0).getName());
        assertEquals(mockUsers.get(1).getName(), users.get(1).getName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById_ExistingId() {
        User mockUser = mockUsers.get(0);
        Long mockUserId = mockUser.getId();
        when(userRepository.findById(mockUserId)).
                thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(mockUserId);

        assertEquals(mockUser.getId(), user.getId());
        assertEquals(mockUser.getName(), user.getName());

        verify(userRepository, times(1)).findById(mockUserId);
    }

    @Test
    public void testGetUserById_NonExistingId() {
        long nonExistingId = 100L;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(nonExistingId);
        });
    }

    @Test
    public void testCreateUser() {
        User newUser = new User("berk");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User createdUser = userService.createUser(newUser);

        assertEquals(newUser.getName(), createdUser.getName());

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testUpdateUser_ExistingId() {
        long id = 1L;
        String updatedName = "UpdatedUser";
        User updatedUser = new User(id, updatedName);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUsers.get(0)));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(id, updatedUser);

        assertEquals(updatedName, result.getName());

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testUpdateUser_NonExistingId() {
        long nonExistingId = 100L;
        String updatedName = "UpdatedUser";
        User updatedUser = new User(updatedName);

        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(nonExistingId, updatedUser);
        });
    }

    @Test
    public void testUpdateUser_NullName() {
        long id = 1L;
        String nullName = null;
        User updatedUser = new User(nullName);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUsers.get(0)));

        User result = userService.updateUser(id, updatedUser);

        assertNull(result);
        verify(userRepository, never()).save(any());

    }

    @Test
    public void testDeleteUser_ExistingId() {
        User mockUser = mockUsers.get(0);
        long mockId = mockUser.getId();

        when(userRepository.findById(mockId)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(mockId);

        verify(userRepository, times(1)).deleteById(mockId);
    }

    @Test
    public void testDeleteUser_NonExistingId() {
        long nonExistingId = 100L;
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(nonExistingId);
        });
    }
}