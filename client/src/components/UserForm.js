import React, { useState } from "react";

function UserForm({ addUser }) {
  const [value, setValue] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (value) {
      addUser(value);
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
