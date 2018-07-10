package com.example.sharingapp;

import java.util.List;

/**
 * UserListController is responsible for all communication between views and UserList model
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class UserListController {

    private UserList userList;

    public UserListController(UserList userList){
        this.userList = userList;
    }

    public void setUsers(List<User> userList) {
        this.userList.setUsers(userList);
    }

    public List<User> getUsers() {
        return userList.getUsers();
    }

    public boolean addUser(User user) {
        AddUserCommand addUserCommand = new AddUserCommand(user);
        addUserCommand.execute();
        return addUserCommand.isExecuted();
    }

    public boolean editUser(User user, User updatedUser){
        EditUserCommand editUserCommand = new EditUserCommand(user, updatedUser);
        editUserCommand.execute();
        return editUserCommand.isExecuted();
    }

    public User getUser(int index) {
        return userList.getUser(index);
    }

    public int getSize() {
        return userList.getSize();
    }

    public User getUserByUsername( String username) {
        return userList.getUserByUsername(username);
    }

    public User getUserByUserId( String userId) {
        return userList.getUserByUserId(userId);
    }


    public boolean isUsernameAvailable(String username){
        return userList.isUsernameAvailable(username);
    }

    public String getUsernameByUserId(String userId){
        return userList.getUsernameByUserId(userId);
    }

    public String getUserIdByUsername(String username){
        return userList.getUserIdByUsername(username);
    }

    public void addObserver(Observer observer) {
        userList.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
       userList.removeObserver(observer);
    }

    public void getRemoteUsers(){
        userList.getRemoteUsers();
    }
}
