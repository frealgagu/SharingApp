package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command to delete a bid
 */
@SuppressWarnings("WeakerAccess")
public class DeleteBidCommand extends Command {

    private Bid bid;

    public DeleteBidCommand(Bid bid) {
        this.bid = bid;
    }

    // Delete the bid remotely from server
    @SuppressWarnings("Duplicates")
    public void execute(){
        ElasticSearchManager.RemoveBidTask removeBidTask = new ElasticSearchManager.RemoveBidTask();
        removeBidTask.execute(bid);

        // use get() to access the return of RemoveBidTask. i.e. RemoveBidTask returns a Boolean to
        // indicate if the bid was successfully deleted from the remote server
        try {
            if(removeBidTask.get()) {
                super.setIsExecuted(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            super.setIsExecuted(false);
        }
    }
}