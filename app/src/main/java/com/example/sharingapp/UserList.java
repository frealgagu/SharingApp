package com.example.sharingapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * UserList class
 */
@SuppressWarnings("WeakerAccess")
public class UserList extends Observable {

    private static final List<User> USERS = new ArrayList<>();

    public UserList() {
    }

    public void setUsers(List<User> userList) {
        USERS.clear();
        USERS.addAll(userList);
        notifyObservers();
    }

    public List<User> getUsers() {
        return new ArrayList<>(USERS);
    }

    public User getUser(int index) {
        return USERS.get(index);
    }

    public int getSize() {
        return USERS.size();
    }

    public User getUserByUsername(String username){
        for (User u : USERS){
            if (u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }

    public User getUserByUserId(String userId) {
        for (User u : USERS){
            if (u.getId().equals(userId)){
                return u;
            }
        }
        return null;
    }

    public String getUsernameByUserId(String userId) {
        for (User u : USERS){
            if (u.getId().equals(userId)){
                return u.getUsername();
            }
        }
        return null;
    }

    public String getUserIdByUsername(String username) {
        for (User u : USERS){
            if (u.getUsername().equals(username)){
                return u.getId();
            }
        }
        return null;
    }

    public boolean isUsernameAvailable(String username) {
        for (User u : USERS) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void getRemoteUsers(){
        ElasticSearchManager.GetUserListTask getUserListTask = new ElasticSearchManager.GetUserListTask();
        getUserListTask.execute();

        try {
            USERS.clear();
            USERS.addAll(getUserListTask.get());
        } catch (InterruptedException | ExecutionException ex) {
            Log.e("USER_LIST", ex.getMessage(), ex);
        }
        notifyObservers();
    }
}
