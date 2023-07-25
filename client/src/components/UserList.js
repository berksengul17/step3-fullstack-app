import React, { useEffect, useState } from "react";
import User from "./User";
import UserForm from "./UserForm";
import EditUserForm from "./EditUserForm";

function UserList() {
  const [users, setUsers] = useState([]);
  useEffect(() => {
    setUsers([
      { id: 1, name: "berk", isEditing: false },
      { id: 2, name: "ali", isEditing: false },
      { id: 3, name: "ayşe", isEditing: false },
      { id: 4, name: "fatma", isEditing: false },
    ]);
  }, []);

  const addUser = (name) => {
    setUsers([
      ...users,
      { id: users[users.length - 1].id + 1, name, isEditing: false },
    ]);
  };

  const editUser = (id, newName) => {
    setUsers(
      users.map((user) =>
        user.id === id
          ? { ...user, name: newName, isEditing: !user.isEditing }
          : user
      )
    );
  };

  const deleteUser = (id) => {
    setUsers(users.filter((user) => user.id !== id));
  };

  const setIsEditing = (id) => {
    setUsers(
      users.map((user) =>
        user.id === id ? { ...user, isEditing: !user.isEditing } : user
      )
    );
  };

  return (
    <div>
      <UserForm addUser={addUser} />
      {users.map((user) =>
        user.isEditing ? (
          <EditUserForm key={user.id} user={user} editUser={editUser} />
        ) : (
          <User
            key={user.id}
            user={user}
            setIsEditing={setIsEditing}
            deleteUser={deleteUser}
          />
        )
      )}
    </div>
  );
}

export default UserList;
