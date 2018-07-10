package com.example.sharingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * BidAdapter is responsible for what information is displayed in bid ListView entries.
 */
@SuppressWarnings("WeakerAccess")
public class BidAdapter extends ArrayAdapter<Bid> {

    private LayoutInflater inflater;

    public BidAdapter(Context context, List<Bid> bids) {
        super(context, 0, bids);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bid bid = getItem(position);
        BidController bidController = new BidController(bid);

        String bidder = "Bidder: " + bidController.getBidderUsername();
        String bidAmount = "Bid: " + bidController.getBidAmount();

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bidlist_bid, parent, false);
        }

        TextView bidderTextView = convertView.findViewById(R.id.bidder_tv);
        TextView bidAmountTextView = convertView.findViewById(R.id.bid_amount_tv);

        bidderTextView.setText(bidder);
        bidAmountTextView.setText(bidAmount);

        return convertView;
    }
}
