package com.example.sharingapp;

import android.content.Context;

/**
 * Command to add a bid
 */
@SuppressWarnings("WeakerAccess")
public class AddBidCommand extends Command {

    private BidList bidList;
    private Bid bid;
    private Context context;

    public AddBidCommand(BidList bidList, Bid bid, Context context) {
        this.bidList = bidList;
        this.bid = bid;
        this.context = context;
    }

    // Save bid locally
    public void execute() {
        bidList.addBid(bid);
        super.setIsExecuted(bidList.saveBids(context));
    }
}
