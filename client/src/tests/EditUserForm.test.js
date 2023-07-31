import { render } from "@testing-library/react";
import EditUserForm from "../components/EditUserForm";
import userEvent from "@testing-library/user-event";

let updateUser;
let onUpdateUser;
let closeForm;

let renderedComp;
let user;

let input;
let submitBtn;

beforeEach(() => {
  updateUser = { id: 1, name: "berk" };

  onUpdateUser = jest.fn();
  closeForm = jest.fn();

  renderedComp = render(
    <EditUserForm
      user={updateUser}
      onUpdateUser={onUpdateUser}
      closeForm={closeForm}
    />
  );

  user = userEvent.setup();

  input = renderedComp.getByPlaceholderText("Edit User");
  submitBtn = renderedComp.getByText("Save");
});

test("submitting the form with a value should call onUpdateUser", async () => {
  await user.clear(input);
  await user.type(input, "updatedName");
  await user.click(submitBtn);

  expect(onUpdateUser).toHaveBeenCalledTimes(1);
  expect(onUpdateUser).toHaveBeenCalledWith({
    ...updateUser,
    name: "updatedName",
  });
  expect(closeForm).toHaveBeenCalledTimes(1);
});

test("if form is submitted empty onUpdateUser should not be called", async () => {
  await user.clear(input);
  await user.click(submitBtn);

  expect(input.value).toBe("");
  expect(onUpdateUser).toHaveBeenCalledTimes(0);
  expect(closeForm).toHaveBeenCalledTimes(1);
});
