package com.example.sharingapp;

import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Command to add an item
 */
@SuppressWarnings("WeakerAccess")
public class AddItemCommand extends Command {

    private Item item;

    public AddItemCommand(Item item) {
        this.item = item;
    }

    // Save the item remotely to server
    @SuppressWarnings("Duplicates")
    public void execute() {
        ElasticSearchManager.AddItemTask addItemTask = new ElasticSearchManager.AddItemTask();
        addItemTask.execute(item);

        // use get() to access the return of AddItemTask. i.e. AddItemTask returns a Boolean to
        // indicate if the item was successfully saved to the remote server
        try {
            if(addItemTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException ex) {
            Log.e("ADD_ITEM_COMMAND", ex.getMessage(), ex);
            super.setIsExecuted(false);
        }
    }
}
