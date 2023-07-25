import axios from "axios";

const API_URL = "http://localhost:8000/users";

// Fetch all users from the server
export const fetchUsers = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// Add a new user to the server
export const addUser = async (newUser) => {
  const response = await axios.post(API_URL, newUser);
  return response.data;
};

// Update an existing user on the server
export const updateUser = async (updatedUser) => {
  const { id } = updatedUser;
  const response = await axios.put(`${API_URL}/${id}`, updatedUser);
  return response.data;
};

// Delete a user from the server
export const deleteUser = async (userId) => {
  const response = await axios.delete(`${API_URL}/${userId}`);
  return response.data;
};
