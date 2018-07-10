package com.example.sharingapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * BidList Class
 */
@SuppressWarnings("WeakerAccess")
public class BidList extends Observable {

    private static final List<Bid> BIDS = new ArrayList<>();

    public BidList() {
    }

    public void setBids(List<Bid> bidList) {
        BIDS.clear();
        BIDS.addAll(bidList);
        notifyObservers();
    }

    public List<Bid> getBids() {
        return new ArrayList<>(BIDS);
    }

    public Bid getBid(int index) {
        return BIDS.get(index);
    }

    public boolean isEmpty() {
        return BIDS.isEmpty();
    }

    public int getIndex(Bid bid) {
        int pos = 0;
        for (Bid b : BIDS) {
            if (bid.getBidId().equals(b.getBidId())) {
                return pos;
            }
            pos = pos + 1;
        }
        return -1;
    }

    public int getSize() {
        return BIDS.size();
    }

    // Used by getHighestBid and BidListController
    public List<Bid> getItemBids(String id){
        List<Bid> itemBids = new ArrayList<>();
        for (Bid b : BIDS) {
            if (b.getItemId().equals(id)) {
                itemBids.add(b);
            }
        }
        return itemBids;
    }

    // Get highest bid_amount of all bids
    public Float getHighestBid(String id) {
        List<Bid> itemBids = getItemBids(id);

        if (itemBids.isEmpty()){
            return null;
        }

        Float highestBidAmount = itemBids.get(0).getBidAmount(); // Initialize
        for (Bid b : itemBids) {
            if (b.getBidAmount() > highestBidAmount) {
                highestBidAmount = b.getBidAmount();
            }
        }

        return highestBidAmount;
    }

    public String getHighestBidder(String id) {
        List<Bid> itemBids = getItemBids(id);

        if (itemBids.isEmpty()){
            return null;
        }

        Float highestBidAmount = itemBids.get(0).getBidAmount(); // Initialize
        String highestBidder = itemBids.get(0).getBidderUsername();
        for (Bid b : itemBids) {
            if (b.getBidAmount() > highestBidAmount) {
                highestBidAmount = b.getBidAmount();
                highestBidder = b.getBidderUsername();
            }
        }

        return highestBidder;
    }

    public void getRemoteBids() {
        ElasticSearchManager.GetBidListTask getBidListTask = new ElasticSearchManager.GetBidListTask();
        getBidListTask.execute();

        try {
            BIDS.clear();
            BIDS.addAll(getBidListTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        notifyObservers();
    }
}
