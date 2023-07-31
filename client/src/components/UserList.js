import React, { useEffect, useState } from "react";
import User from "./User.js";
import UserForm from "./UserForm.js";
import { fetchUsers, addUser, updateUser, deleteUser } from "../userService.js";

function UserList() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = () => {
    fetchUsers()
      .then((data) => {
        setUsers(data);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  };

  const handleAddUser = (newUser) => {
    const id = users.length > 0 ? users[users.length - 1].id + 1 : 1;
    addUser({ id, ...newUser })
      .then((data) => {
        console.log("User added successfully:", data);
        setUsers([...users, data]);
      })
      .catch((error) => {
        console.error("Error adding user:", error);
      });
  };

  const handleUpdateUser = (updatedUser) => {
    updateUser(updatedUser)
      .then((data) => {
        console.log("User updated successfully:", data);
        setUsers((prevUsers) =>
          prevUsers.map((user) =>
            user.id === updatedUser.id ? updatedUser : user
          )
        );
      })
      .catch((error) => {
        console.error("Error updating user:", error);
      });
  };

  const handleDeleteUser = (userId) => {
    deleteUser(userId)
      .then(() => {
        console.log("User deleted successfully");
        setUsers((prevUsers) => prevUsers.filter((user) => user.id !== userId));
      })
      .catch((error) => {
        console.error("Error deleting user:", error);
      });
  };

  return (
    <div>
      <UserForm onAddUser={handleAddUser} />
      <ul className="user-list">
        {users.map((user) => (
          <User
            key={user.id}
            user={user}
            onUpdateUser={handleUpdateUser}
            onDeleteUser={handleDeleteUser}
          />
        ))}
      </ul>
    </div>
  );
}

export default UserList;
