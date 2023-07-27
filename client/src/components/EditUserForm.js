import React, { useState } from "react";

function EditUserForm({ user, onUpdateUser, closeForm }) {
  const [value, setValue] = useState(user.name);

  const handleSubmit = (event) => {
    event.preventDefault();
    onUpdateUser({ id: user.id, name: value });
    closeForm();
  };

  return (
    <form className="user-form" onSubmit={handleSubmit}>
      <input
        className="form-input"
        type="text"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="Edit User"
      />
      <button className="form-button" type="submit">
        Save
      </button>
    </form>
  );
}

export default EditUserForm;
