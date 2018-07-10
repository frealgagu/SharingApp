package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command to add user
 */
@SuppressWarnings("WeakerAccess")
public class AddUserCommand extends Command {

    private User user;

    public AddUserCommand (User user) {
        this.user = user;
    }

    // Save the user remotely to server
    @SuppressWarnings("Duplicates")
    public void execute() {
        ElasticSearchManager.AddUserTask addUserTask = new ElasticSearchManager.AddUserTask();
        addUserTask.execute(user);

        // use get() to access the return of AddUserTask. i.e. AddUserTask returns a Boolean to
        // indicate if the user was successfully saved to the remote server
        try {
            if(addUserTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            super.setIsExecuted(false);
        }
    }
}
