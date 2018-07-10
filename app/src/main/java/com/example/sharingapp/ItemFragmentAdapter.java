package com.example.sharingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ItemFragmentAdapter is responsible for what information is displayed in ListView entries.
 */
@SuppressWarnings("WeakerAccess")
public class ItemFragmentAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;
    private Fragment fragment;

    public ItemFragmentAdapter(Context context, List<Item> items, Fragment fragment) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        ItemController itemController = new ItemController(item);

        String title = "Title: " + itemController.getTitle();
        String description = "Description: " + itemController.getDescription();
        Bitmap thumbnail = itemController.getImage();
        String status = "Status: " + itemController.getStatus();

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemlist_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.title_tv);
        TextView statusTextView = convertView.findViewById(R.id.status_tv);
        TextView descriptionTextView = convertView.findViewById(R.id.description_tv);
        ImageView photo = convertView.findViewById(R.id.image_view);
        TextView bidderTextView = convertView.findViewById(R.id.bidder_tv);
        TextView highestBidTextView = convertView.findViewById(R.id.highest_bid_tv);

        if (thumbnail != null) {
            photo.setImageBitmap(thumbnail);
        } else {
            photo.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // AllItemFragments: itemlist_item shows title, description and status
        if (fragment instanceof AllItemsFragment ) {
            statusTextView.setText(status);
        }

        // BorrowedItemsFragment/AvailableItemsFragment: itemlist_item shows title and description only
        if (fragment instanceof BorrowedItemsFragment || fragment instanceof AvailableItemsFragment
                || fragment instanceof BiddedItemsFragment) {
            statusTextView.setVisibility(View.GONE);
        }

        //  BiddedItemsFragment: itemlist_item shows bidder and highest bid
        if (fragment instanceof BiddedItemsFragment) {
            BidList bidList = new BidList();
            BidListController bidListController = new BidListController(bidList);
            bidListController.getRemoteBids();

            String bidder = "Bidder: " + bidListController.getHighestBidder(itemController.getId());
            String highestBid = "Highest Bid: " + bidListController.getHighestBid(itemController.getId());

            bidderTextView.setVisibility(View.VISIBLE);
            highestBidTextView.setVisibility(View.VISIBLE);
            bidderTextView.setText(bidder);
            highestBidTextView.setText(highestBid);
        }

        return convertView;
    }
}
