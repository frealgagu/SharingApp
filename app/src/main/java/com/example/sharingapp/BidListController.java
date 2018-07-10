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

    public void setBids(List<Bid> bidList) {
        this.bidList.setBids(bidList);
    }

    public List<Bid> getBids() {
        return bidList.getBids();
    }

    public boolean addBid(Bid bid, Context context){
        AddBidCommand addBidCommand = new AddBidCommand(bidList, bid, context);
        addBidCommand.execute();
        return addBidCommand.isExecuted();
    }

    public boolean removeBid(Bid bid, Context context) {
        DeleteBidCommand deleteBidCommand = new DeleteBidCommand(bidList, bid, context);
        deleteBidCommand.execute();
        return deleteBidCommand.isExecuted();
    }

    public boolean removeItemBids(String id, Context context) {
        DeleteBidCommand deleteBidCommand;
        List<Bid> oldBids = bidList.getItemBids(id);

        for (Bid b : oldBids) {
            deleteBidCommand = new DeleteBidCommand(bidList, b, context);
            deleteBidCommand.execute();
            if (!deleteBidCommand.isExecuted()){
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
