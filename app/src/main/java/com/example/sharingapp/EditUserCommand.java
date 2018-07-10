package com.example.sharingapp;

import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Command used to edit pre-existing user
 */
@SuppressWarnings("WeakerAccess")
public class EditUserCommand extends Command {

    private User oldUser;
    private User newUser;

    public EditUserCommand (User oldUser, User newUser){
        this.oldUser = oldUser;
        this.newUser = newUser;
    }

    // Delete the old user remotely, save the new user remotely to server
    @SuppressWarnings("Duplicates")
    public void execute() {
        ElasticSearchManager.RemoveUserTask removeUserTask = new ElasticSearchManager.RemoveUserTask();
        removeUserTask.execute(oldUser);

        ElasticSearchManager.AddUserTask addUserTask = new ElasticSearchManager.AddUserTask();
        addUserTask.execute(newUser);

        // use get() to access the return of AddUserTask/RemoveUserTask.
        // i.e. AddUserTask/RemoveUserTask returns a Boolean to indicate if the user was successfully
        // deleted/saved to the remote server
        try {
            if(addUserTask.get() && removeUserTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException ex) {
            Log.e("EDIT_USER_COMMAND", ex.getMessage(), ex);
            super.setIsExecuted(false);
        }
    }
}
