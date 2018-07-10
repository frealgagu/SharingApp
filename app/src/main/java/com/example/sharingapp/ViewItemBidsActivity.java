package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ViewItemBidsActivity extends AppCompatActivity implements Observer {

    private BidList bidList = new BidList();
    private BidListController bidListController = new BidListController(bidList);

    private List<Bid> itemBidList; // Bids placed on the item

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private Context context;

    private ListView itemBids;
    private ArrayAdapter<Bid> adapter;

    private String userId;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_bids);

        Intent intent = getIntent(); // Get intent from EditItemActivity
        userId = intent.getStringExtra(Constants.USER_ID);
        itemId = intent.getStringExtra(Constants.ITEM_ID);

        context = getApplicationContext();

        bidListController.loadBids(context);
        bidListController.addObserver(this);
        itemBidList = bidListController.getItemBids(itemId);

        itemListController.addObserver(this);
        itemListController.getRemoteItems();
        userListController.getRemoteUsers();
    }

    public void acceptBid(View view) {
        int pos = itemBids.getPositionForView(view);

        Bid bid = adapter.getItem(pos);
        BidController bidController = new BidController(bid);

        Item item = itemListController.getItemById(itemId);
        ItemController itemController = new ItemController(item);

        String borrowerUsername = bidController.getBidderUsername();
        User borrower = userListController.getUserByUsername(borrowerUsername);

        String title = itemController.getTitle();
        String maker = itemController.getMaker();
        String description = itemController.getDescription();
        String ownerId = itemController.getOwnerId();
        String minimumBid = itemController.getMinBid().toString();
        Bitmap image = itemController.getImage();
        String length = itemController.getLength();
        String width = itemController.getWidth();
        String height = itemController.getHeight();
        String status = "Borrowed";

        Item updatedItem = new Item(title, maker, description, ownerId, minimumBid, image, itemId);
        ItemController updatedItemController = new ItemController(updatedItem);
        updatedItemController.setDimensions(length, width, height);
        updatedItemController.setStatus(status);
        updatedItemController.setBorrower(borrower);

        boolean success = itemListController.editItem(item, updatedItem);
        if (!success){
            return;
        }

        // Delete all bids related to that item.
        success =  bidListController.removeItemBids(itemId, context);
        if (!success){
            return;
        }

        itemListController.removeObserver(this);
        bidListController.removeObserver(this);

        // End ViewItemBidsActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Bid accepted.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
    }

    public void declineBid(View view) {
        int pos = itemBids.getPositionForView(view);

        Bid bid = adapter.getItem(pos);

        Item item = itemListController.getItemById(itemId);
        ItemController itemController = new ItemController(item);

        String title = itemController.getTitle();
        String maker = itemController.getMaker();
        String description = itemController.getDescription();
        String ownerId = itemController.getOwnerId();
        String minimumBid = itemController.getMinBid().toString();
        Bitmap image = itemController.getImage();
        String length = itemController.getLength();
        String width = itemController.getWidth();
        String height = itemController.getHeight();
        String status = itemController.getStatus();

        // Delete selected bid.
        Boolean success = bidListController.removeBid(bid, context);
        if (!success){
            return;
        }

        itemBidList.remove(bid);
        bidListController.saveBids(context); // Save the changes, call to update

        if (itemBidList.isEmpty()) {
            status = "Available";
        }

        Item updatedItem = new Item(title, maker, description, ownerId, minimumBid, image, itemId);
        ItemController updatedItemController = new ItemController(updatedItem);
        updatedItemController.setDimensions(length, width, height);
        updatedItemController.setStatus(status);

        success = itemListController.editItem(item, updatedItem);
        if (!success){
            return;
        }

        itemListController.removeObserver(this);
        bidListController.removeObserver(this);

        if (status.equals("Available")){ // No bids remain
            Toast.makeText(context, "All bids declined.", Toast.LENGTH_SHORT).show();

        } else { // Some bids remain
            Toast.makeText(context, "Bid declined.", Toast.LENGTH_SHORT).show();
        }

        // End ViewItemBidsActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 750);

    }

    public void declineAllBids(View view) {
        Item item = itemListController.getItemById(itemId);
        ItemController itemController = new ItemController(item);

        String title = itemController.getTitle();
        String maker = itemController.getMaker();
        String description = itemController.getDescription();
        String ownerId = itemController.getOwnerId();
        String minimumBid = itemController.getMinBid().toString();
        Bitmap image = itemController.getImage();
        String length = itemController.getLength();
        String width = itemController.getWidth();
        String height = itemController.getHeight();
        String status = "Available";

        Item updatedItem = new Item(title, maker, description, ownerId, minimumBid, image, itemId);
        ItemController updatedItemController = new ItemController(updatedItem);
        updatedItemController.setDimensions(length, width, height);
        updatedItemController.setStatus(status);

        boolean success = itemListController.editItem(item, updatedItem);
        if (!success){
            return;
        }

        // Delete all bids related to that item.
        success =  bidListController.removeItemBids(itemId, context);
        if (!success){
            return;
        }

        itemListController.removeObserver(this);
        bidListController.removeObserver(this);

        // End ViewItemBidsActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "All bids declined.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
    }

    /**
     * Update the view
     */
    public void update() {
        itemBids = (ListView) findViewById(R.id.item_bids);
        adapter = new BidAdapter(this, itemBidList);
        itemBids.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
