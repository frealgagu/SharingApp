package com.example.sharingapp;

import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Command to delete an item
 */
@SuppressWarnings("WeakerAccess")
public class DeleteItemCommand extends Command {

    private Item item;

    public DeleteItemCommand(Item item) {
        this.item = item;
    }

    // Delete the item remotely from server
    @SuppressWarnings("Duplicates")
    public void execute() {
        ElasticSearchManager.RemoveItemTask removeItemTask = new ElasticSearchManager.RemoveItemTask();
        removeItemTask.execute(item);

        // use get() to access the return of RemoveItemTask. i.e. RemoveItemTask returns a Boolean to
        // indicate if the item was successfully deleted from the remote server
        try {
            if(removeItemTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException ex) {
            Log.e("DELETE_ITEM_COMMAND", ex.getMessage(), ex);
            super.setIsExecuted(false);
        }
    }
}
