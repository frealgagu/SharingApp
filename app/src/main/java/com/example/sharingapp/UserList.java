package com.example.sharingapp;

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

    public void setUsers(List<User> user_list) {
        USERS.clear();
        USERS.addAll(user_list);
        notifyObservers();
    }

    public List<User> getUsers() {
        return new ArrayList<>(USERS);
    }

    @SuppressWarnings("unused")
    public void addUser(User user) {
        USERS.add(user);
        notifyObservers();
    }

    @SuppressWarnings("unused")
    public void deleteUser(User user) {
        USERS.remove(user);
        notifyObservers();
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

    public User getUserByUserId(String user_id){
        for (User u : USERS){
            if (u.getId().equals(user_id)){
                return u;
            }
        }
        return null;
    }

    public String getUsernameByUserId(String user_id){
        for (User u : USERS){
            if (u.getId().equals(user_id)){
                return u.getUsername();
            }
        }
        return null;
    }

    public String getUserIdByUsername(String username){
        for (User u : USERS){
            if (u.getUsername().equals(username)){
                return u.getId();
            }
        }
        return null;
    }

    public boolean isUsernameAvailable(String username){
        for (User u : USERS) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void getRemoteUsers(){
        ElasticSearchManager.GetUserListTask get_user_list_task = new ElasticSearchManager.GetUserListTask();
        get_user_list_task.execute();

        try {
            USERS.clear();
            USERS.addAll(get_user_list_task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        notifyObservers();
    }
}
