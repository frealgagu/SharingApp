package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command to add a bid
 */
@SuppressWarnings("WeakerAccess")
public class AddBidCommand extends Command {

    private Bid bid;

    public AddBidCommand(Bid bid) {
        this.bid = bid;
    }

    // Save the bid remotely to server
    @SuppressWarnings("Duplicates")
    public void execute() {
        ElasticSearchManager.AddBidTask addBidTask = new ElasticSearchManager.AddBidTask();
        addBidTask.execute(bid);

        // use get() to access the return of AddBidTask. i.e. AddBidTask returns a Boolean to
        // indicate if the bid was successfully saved to the remote server
        try {
            if(addBidTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            super.setIsExecuted(false);
        }
    }
}
