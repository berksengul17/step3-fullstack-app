import React, { useState } from "react";

function EditUserForm({ user, editUser }) {
  const [value, setValue] = useState(user.name);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (value) {
      editUser(user.id, value);
      setValue("");
    }
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
        Edit User
      </button>
    </form>
  );
}

export default EditUserForm;
