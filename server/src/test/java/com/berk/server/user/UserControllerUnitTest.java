package com.berk.server.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<User> mockUsers;

    @BeforeEach
    public void setUp() {
        mockUsers = List.of(
        new User(1L, "User 1"),
        new User(2L, "User 2"));
    }

    @Test
    public void testGetUsers() {
        when(userService.getAllUsers())
        .thenReturn(mockUsers);

        ResponseEntity<List<User>> result = userController.getUsers();

        assertEquals(mockUsers, result.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById_ExistingId() {
        long existingId = 1L;
        when(userService.getUserById(existingId))
                .thenReturn(mockUsers.get(0));

        ResponseEntity<?> result = userController.getUserById(existingId);

        assertEquals(mockUsers.get(0), result.getBody());
        verify(userService, times(1)).getUserById(existingId);
    }

    @Test
    public void testGetUserById_NonExistingId() {
        long nonExistingId = 1L;
        String exceptionMessage = "User with id " + nonExistingId + " does not exist";
        String expectedJson = "{\"message\": " + "\"" + exceptionMessage + "\"" + "}";

        when(userService.getUserById(nonExistingId))
                .thenThrow(new UserNotFoundException(exceptionMessage));

        ResponseEntity<?> result = userController.getUserById(nonExistingId);

        assertEquals(expectedJson, result.getBody());
        verify(userService, times(1)).getUserById(nonExistingId);
    }

    @Test
    public void testAddUser_NonExistingName() {
        String nonExistingName = "New User";
        User newUser = new User(nonExistingName);

        when(userService.createUser(newUser)).thenReturn(newUser);

        ResponseEntity<?> result = userController.createUser(newUser);
        assertEquals(newUser, result.getBody());
        verify(userService, times(1)).createUser(newUser);
    }

    @Test
    public void testAddUser_ExistingName() {
        String existingName = "User 1";
        User newUser = new User(existingName);

        String exceptionMessage = "Name " + newUser.getName() + " is taken.";
        String expectedJson = "{\"message\": " + "\"" + exceptionMessage + "\"" + "}";

        when(userService.createUser(newUser)).thenThrow(
                new IllegalArgumentException(exceptionMessage)
        );

        ResponseEntity<?> result = userController.createUser(newUser);
        assertEquals(expectedJson, result.getBody());
        verify(userService, times(1)).createUser(newUser);
    }

    @Test
    public void testUpdateUser_ExistingId() {
        long existingId = 1L;
        String updatedName = "Updated User";
        User updatedUser = new User(updatedName);

        when(userService.updateUser(existingId, updatedUser))
                .thenReturn(updatedUser);

        ResponseEntity<?> result = userController.updateUser(existingId, updatedUser);

        assertEquals(updatedUser, result.getBody());
        verify(userService, times(1)).updateUser(existingId, updatedUser);
    }

    @Test
    public void testUpdateUser_NonExistingId() {
        long nonExistingId = 100L;
        String updatedName = "Updated User";
        User updatedUser = new User(updatedName);

        String exceptionMessage = "User with id " + nonExistingId + " does not exist";
        String expectedJson = "{\"message\": " + "\"" + exceptionMessage + "\"" + "}";

        when(userService.updateUser(nonExistingId, updatedUser))
                .thenThrow(new UserNotFoundException(exceptionMessage));

        ResponseEntity<?> result = userController.updateUser(nonExistingId, updatedUser);

        assertEquals(expectedJson, result.getBody());
        verify(userService, times(1)).updateUser(nonExistingId, updatedUser);
    }

    @Test
    public void testDeleteUser_ExistingId() {
        long existingId = 1L;
        String expectedMessage = "User with the id " + existingId + " is deleted.";

        doNothing().when(userService).deleteUser(existingId);

        ResponseEntity<?> result = userController.deleteUser(existingId);

        assertEquals(expectedMessage, result.getBody());
        verify(userService, times(1)).deleteUser(existingId);
    }

    @Test
    public void testDeleteUser_NonExistingId() {
        long nonExistingId = 100L;

        String exceptionMessage = "User with id " + nonExistingId + " does not exist";
        String expectedJson = "{\"message\": " + "\"" + exceptionMessage + "\"" + "}";

        doThrow(new UserNotFoundException(exceptionMessage))
                .when(userService).deleteUser(nonExistingId);

        ResponseEntity<?> result = userController.deleteUser(nonExistingId);

        assertEquals(expectedJson, result.getBody());
        verify(userService, times(1)).deleteUser(nonExistingId);
    }
}