package com.berk.server.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerIntegrationTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    // It provides a powerful and convenient way to
    // write unit tests for your controllers without needing
    // to deploy your application to a web server.
    private MockMvc mockMvc;
    private List<User> mockUsers;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockUsers = List.of(
                new User(1L, "User 1"),
                new User(2L, "User 2"));
    }

    @Test
    public void testGetUsers() throws Exception{
        when(userService.getAllUsers())
                .thenReturn(mockUsers);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById_ExistingId() throws Exception {
        long existingId = 1L;
        when(userService.getUserById(existingId)).thenReturn(mockUsers.get(0));

        mockMvc.perform(get("/api/users/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("User 1"));

        verify(userService, times(1)).getUserById(existingId);
    }

    @Test
    public void testGetUserById_NonExistingId() throws Exception {
        long nonExistingId = 100L;
        String expectedMessage = "User with id " + nonExistingId + " does not exist";

        when(userService.getUserById(nonExistingId))
                .thenThrow(new UserNotFoundException(expectedMessage));

        ResultActions result = mockMvc.perform(get("/api/users/{id}", nonExistingId));

        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = result.andReturn();
        String responseJson = mvcResult.getResponse().getContentAsString();
        UserNotFoundException error =
                new ObjectMapper().readValue(responseJson, UserNotFoundException.class);

        assertEquals(expectedMessage, error.getMessage());
        verify(userService, times(1)).getUserById(nonExistingId);
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("User 3");

        when(userService.createUser(any()))
                .thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User 3"));

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void testUpdateUser_ExistingId() throws Exception {
        long existingId = 1L;
        String updatedName = "UpdatedUser";
        User updatedUser = new User(existingId, updatedName);

        when(userService.updateUser(existingId, updatedUser)).
                thenReturn(updatedUser);

        ObjectMapper mapper = new ObjectMapper();
        String updateUserJson = mapper.writeValueAsString(updatedUser);

        mockMvc.perform(put("/api/users/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedName));

        verify(userService, times(1)).updateUser(existingId, updatedUser);
    }

    @Test
    public void testUpdateUser_NonExistingId() throws Exception {
        long nonExistingId = 100L;
        String updatedName = "UpdatedUser";
        User updatedUser = new User(updatedName);

        String expectedMessage = "User with id " + nonExistingId + " does not exist";

        when(userService.updateUser(nonExistingId, updatedUser)).
                thenThrow(new UserNotFoundException(expectedMessage));

        ObjectMapper mapper = new ObjectMapper();
        String updateUserJson = mapper.writeValueAsString(updatedUser);

        ResultActions result = mockMvc.perform(put("/api/users/{id}", nonExistingId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(updateUserJson));

        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = result.andReturn();
        String responseJson = mvcResult.getResponse().getContentAsString();
        UserNotFoundException error =
                new ObjectMapper().readValue(responseJson, UserNotFoundException.class);

        assertEquals(expectedMessage, error.getMessage());
        verify(userService, times(1)).updateUser(nonExistingId, updatedUser);
    }

    @Test
    public void testDeleteUser_ExistingId() throws Exception {
        long existingId = 1L;

        doNothing().when(userService).deleteUser(existingId);

        mockMvc.perform(delete("/api/users/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User with the id " + existingId + " is deleted."));

        verify(userService, times(1)).deleteUser(existingId);
    }

    @Test
    public void testDeleteUser_NonExistingId() throws Exception {
        long nonExistingId = 1L;
        String expectedMessage = "User with id " + nonExistingId + " does not exist";

        doThrow(new UserNotFoundException(expectedMessage))
                .when(userService).deleteUser(nonExistingId);

        mockMvc.perform(delete("/api/users/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).deleteUser(nonExistingId);
    }
}