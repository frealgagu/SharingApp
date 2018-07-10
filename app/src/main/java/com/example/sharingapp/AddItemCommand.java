package com.example.sharingapp;

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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            super.setIsExecuted(false);
        }
    }
}
