package com.example.sharingapp;

/**
 * BidController is responsible for all communication between views and Bid model
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BidController {

    private Bid bid;

    public BidController (Bid bid) {
        this.bid = bid;
    }

    public String getBidId() {
        return bid.getBidId();
    }

    public void setBidId() {
        bid.setBidId();
    }

    public String getItemId() {
        return bid.getItemId();
    }

    public void setItemId(String itemId) {
        bid.setItemId(itemId);
    }

    public void setbidAmount(float bidAmount) {
        bid.setBidAmount(bidAmount);
    }

    public Float getBidAmount() {
        return bid.getBidAmount();
    }

    public void setBidderUsername(String bidderUsername) {
        bid.setBidderUsername(bidderUsername);
    }

    public String getBidderUsername() {
        return bid.getBidderUsername();
    }

    public void addObserver(Observer observer) {
        bid.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        bid.removeObserver(observer);
    }
}
