package com.example.sharingapp;

import android.content.Context;

import java.util.List;

/**
 * BidListController is responsible for all communication between views and BidList model
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BidListController {

    private BidList bidList;

    public BidListController(BidList bidList) {
        this.bidList = bidList;
    }

    public void setBids(List<Bid> bid_list) {
        this.bidList.setBids(bid_list);
    }

    public List<Bid> getBids() {
        return bidList.getBids();
    }

    public boolean addBid(Bid bid, Context context){
        AddBidCommand add_bid_command = new AddBidCommand(bidList, bid, context);
        add_bid_command.execute();
        return add_bid_command.isExecuted();
    }

    public boolean removeBid(Bid bid, Context context) {
        DeleteBidCommand delete_bid_command = new DeleteBidCommand(bidList, bid, context);
        delete_bid_command.execute();
        return delete_bid_command.isExecuted();
    }

    public boolean removeItemBids(String id, Context context) {
        DeleteBidCommand delete_bid_command;
        List<Bid> old_bids = bidList.getItemBids(id);

        for (Bid b : old_bids) {
            delete_bid_command = new DeleteBidCommand(bidList, b, context);
            delete_bid_command.execute();
            if (!delete_bid_command.isExecuted()){
                return false;
            }
        }
        return true;
    }

    public Bid getBid(int index) {
        return bidList.getBid(index);
    }

    public boolean isEmpty() {
        return bidList.isEmpty();
    }

    public int getIndex(Bid bid) {
        return bidList.getIndex(bid);
    }

    public int getSize() {
        return bidList.getSize();
    }

    public List<Bid> getItemBids(String id) {
        return bidList.getItemBids(id);
    }

    public Float getHighestBid(String id) {
        return bidList.getHighestBid(id);
    }

    public String getHighestBidder(String id) {
        return bidList.getHighestBidder(id);
    }

    public void loadBids(Context context) {
        bidList.loadBids(context);
    }

    public boolean saveBids(Context context) {
        return bidList.saveBids(context);
    }

    public void addObserver(Observer observer) {
        bidList.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        bidList.removeObserver(observer);
    }
}
