import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import EditUserForm from "./EditUserForm.js";

function User({ user, onUpdateUser, onDeleteUser }) {
  const [editMode, setEditMode] = useState(false);

  const handleDelete = () => {
    if (window.confirm("Are you sure you want to delete this user?")) {
      onDeleteUser(user.id);
    }
  };

  return (
    <li>
      {editMode ? (
        <EditUserForm
          user={user}
          onUpdateUser={onUpdateUser}
          closeForm={() => setEditMode(false)}
        />
      ) : (
        <div className="user">
          <p>{user.name}</p>
          <div>
            <FontAwesomeIcon
              icon={faPenToSquare}
              onClick={() => setEditMode(true)}
              data-testid="edit-btn"
            />
            <FontAwesomeIcon
              className="thrash-icon"
              icon={faTrash}
              onClick={handleDelete}
              data-testid="delete-btn"
            />
          </div>
        </div>
      )}
    </li>
  );
}

export default User;
