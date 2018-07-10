package com.example.sharingapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * BidList Class
 */
@SuppressWarnings("WeakerAccess")
public class BidList extends Observable {

    private static final String FILENAME = "bids.sav";

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

    public void addBid(Bid bid){
        BIDS.add(bid);
        notifyObservers();
    }

    public void removeBid(Bid bid){
        BIDS.remove(bid);
        notifyObservers();
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

    public void loadBids(Context context) {
        BIDS.clear();
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Bid>>() {}.getType();
            List<Bid> bidList = gson.fromJson(isr, listType);// temporary
            BIDS.addAll(bidList);
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        notifyObservers();
    }

    public boolean saveBids(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(BIDS, osw);
            osw.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
