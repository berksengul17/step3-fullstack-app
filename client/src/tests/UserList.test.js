import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import UserList from "../components/UserList";
import * as userService from "../userService.js";

jest.mock("../userService", () => ({
  fetchUsers: jest.fn(() =>
    Promise.resolve([
      { id: 1, name: "berk" },
      { id: 2, name: "ayşe" },
    ])
  ),
  addUser: jest.fn((newUser) => Promise.resolve(newUser)),
  updateUser: jest.fn((updatedUser) => Promise.resolve(updatedUser)),
  deleteUser: jest.fn((id) =>
    Promise.resolve("User with the id" + id + "is deleted.")
  ),
}));

let user;

beforeEach(async () => {
  user = userEvent.setup();
  render(<UserList />);
  await waitFor(() => expect(userService.fetchUsers).toHaveBeenCalledTimes(1));
});

afterEach(() => {
  jest.clearAllMocks();
});

test("renders data from API", async () => {
  const user1 = await screen.findByText("berk");
  const user2 = await screen.findByText("ayşe");

  expect(user1).toBeInTheDocument();
  expect(user2).toBeInTheDocument();
});

test("adds a new user when the form is submitted", async () => {
  const newUser = { id: 3, name: "New User" };

  const inputElement = screen.getByPlaceholderText("Add User");
  const submitButton = screen.getByText("Add User");

  await user.type(inputElement, "New User");
  await user.click(submitButton);

  await waitFor(() => expect(userService.addUser).toHaveBeenCalledTimes(1));
  expect(userService.addUser).toHaveBeenCalledWith(newUser);
  expect(screen.getByText(newUser.name)).toBeInTheDocument();
});

test("updates a user's name when edited", async () => {
  const userToUpdate = screen.getByText("ayşe");
  const updatedUser = { id: 2, name: "Updated User" };

  const editBtn = userToUpdate.parentNode.querySelector(
    "[data-testid='edit-btn']"
  );

  await user.click(editBtn);

  const inputElement = screen.getByDisplayValue("ayşe");
  const saveButton = screen.getByText("Save");

  await userEvent.clear(inputElement);
  await userEvent.type(inputElement, updatedUser.name);
  await userEvent.click(saveButton);

  await waitFor(() => expect(userService.updateUser).toHaveBeenCalledTimes(1));
  expect(userService.updateUser).toHaveBeenCalledWith(updatedUser);
  expect(screen.getByText(updatedUser.name)).toBeInTheDocument();
});

test("deletes a user when the delete icon is clicked and confirmed", async () => {
  const id = 2;
  const userToDelete = screen.getByText("ayşe");

  const deleteBtn = userToDelete.parentNode.querySelector(
    "[data-testid='delete-btn']"
  );

  window.confirm = jest.fn(() => true);

  await user.click(deleteBtn);

  await waitFor(() => expect(userService.deleteUser).toHaveBeenCalledTimes(1));
  expect(userService.deleteUser).toHaveBeenCalledWith(id);
  expect(screen.queryByText("ayşe")).not.toBeInTheDocument();
});
