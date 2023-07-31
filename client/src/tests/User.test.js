import { render } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import User from "../components/User";

let user;
let onUpdateUser;
let onDeleteUser;

let renderedComp;
let userEvents;

let editBtn;
let deleteBtn;

beforeEach(() => {
  user = { id: 1, name: "berk" };
  onUpdateUser = jest.fn();
  onDeleteUser = jest.fn();

  renderedComp = render(
    <User user={user} onUpdateUser={onUpdateUser} onDeleteUser={onDeleteUser} />
  );

  userEvents = userEvent.setup();

  editBtn = renderedComp.getByTestId("edit-btn");
  deleteBtn = renderedComp.getByTestId("delete-btn");
});

test("clicking on edit icon should switch to edit mode", async () => {
  await userEvents.click(editBtn);
  const input = renderedComp.getByDisplayValue("berk");
  expect(input).toBeInTheDocument();
});

test("clicking on the delete icon should prompt confirmation and delete user", async () => {
  window.confirm = jest.fn(() => true);

  await userEvents.click(deleteBtn);

  expect(window.confirm).toHaveBeenCalledWith(
    "Are you sure you want to delete this user?"
  );

  expect(onDeleteUser).toHaveBeenCalledTimes(1);
  expect(onDeleteUser).toHaveBeenCalledWith(user.id);
});
