package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command used to edit pre-existing item
 */
@SuppressWarnings("WeakerAccess")
public class EditItemCommand extends Command {

    private Item oldItem;
    private Item newItem;

    public EditItemCommand(Item oldItem, Item newItem) {
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    // Delete the old item remotely, save the new item remotely to server
    @SuppressWarnings("Duplicates")
    public void execute() {
        ElasticSearchManager.RemoveItemTask removeItemTask = new ElasticSearchManager.RemoveItemTask();
        removeItemTask.execute(oldItem);

        ElasticSearchManager.AddItemTask addItemTask = new ElasticSearchManager.AddItemTask();
        addItemTask.execute(newItem);

        // use get() to access the return of AddItemTask/RemoveItemTask.
        // i.e. AddItemTask/RemoveItemTask returns a Boolean to indicate if the item was successfully
        // deleted/saved to the remote server
        try {
            if(addItemTask.get() && removeItemTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            super.setIsExecuted(false);
        }
    }
}
