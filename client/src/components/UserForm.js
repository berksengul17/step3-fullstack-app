import React, { useState } from "react";

function UserForm({ onAddUser }) {
  const [value, setValue] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();
    if (value.length > 0) {
      onAddUser({ name: value });
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
        placeholder="Add User"
      />
      <button className="form-button" type="submit">
        Add User
      </button>
    </form>
  );
}

export default UserForm;
