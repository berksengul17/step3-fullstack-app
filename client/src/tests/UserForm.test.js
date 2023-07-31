import { render } from "@testing-library/react";
import UserForm from "../components/UserForm";
import userEvent from "@testing-library/user-event";

let onAddUser;
let renderedComp;
let user;

let input;
let submitBtn;

beforeEach(() => {
  onAddUser = jest.fn();
  renderedComp = render(<UserForm onAddUser={onAddUser} />);
  user = userEvent.setup();

  input = renderedComp.getByPlaceholderText("Add User");
  submitBtn = renderedComp.getByText("Add User");
});

test("submitting the form with a value should call onAddUser", async () => {
  await user.type(input, "berk");
  await user.click(submitBtn);

  expect(onAddUser).toHaveBeenCalledTimes(1);
  expect(onAddUser).toHaveBeenCalledWith({ name: "berk" });
});

test("after submitting the form with a value the form should be empty", async () => {
  await user.type(input, "berk");
  await user.click(submitBtn);

  expect(input.value).toBe("");
});

test("if form is submitted empty onAddUser should not be called", async () => {
  await user.click(submitBtn);

  expect(input.value).toBe("");
  expect(onAddUser).toHaveBeenCalledTimes(0);
});
