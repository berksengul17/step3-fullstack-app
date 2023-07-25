import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { faTrash } from "@fortawesome/free-solid-svg-icons";

function User({ user, setIsEditing, deleteUser }) {
  return (
    <div className="user">
      <p>{user.name}</p>
      <div>
        <FontAwesomeIcon
          icon={faPenToSquare}
          onClick={() => setIsEditing(user.id)}
        />
        <FontAwesomeIcon
          className="thrash-icon"
          icon={faTrash}
          onClick={() => deleteUser(user.id)}
        />
      </div>
    </div>
  );
}

export default User;
