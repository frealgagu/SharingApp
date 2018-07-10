package com.example.sharingapp;

import java.util.UUID;

/**
 * Bid Class
 */
@SuppressWarnings("WeakerAccess")
public class Bid extends Observable {

    private String itemId;
    private String bidId;
    private Float bidAmount;
    private String bidderUsername;

    public Bid(String itemId, Float bidAmount, String bidderUsername) {
        this.itemId = itemId;
        this.bidAmount = bidAmount;
        this.bidderUsername = bidderUsername;
        setBidId();
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String item_id) {
        this.itemId = item_id;
    }

    public String getBidId(){
        return this.bidId;
    }

    public void setBidId() {
        this.bidId = UUID.randomUUID().toString();
        notifyObservers();
    }

    public void setBidAmount(Float bid_amount) {
        this.bidAmount = bid_amount;
        notifyObservers();
    }

    public Float getBidAmount() {
        return bidAmount;
    }

    public void setBidderUsername(String bidder_username) {
        this.bidderUsername = bidder_username;
        notifyObservers();
    }

    public String getBidderUsername() {
        return bidderUsername;
    }
}
