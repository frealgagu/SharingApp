package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewItemActivity extends AppCompatActivity implements Observer {

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private Item item;
    private ItemController itemController;

    private BidList bidList = new BidList();
    private BidListController bidListController = new BidListController(bidList);

    private Context context;

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private Bitmap image;
    private ImageView photo;

    private TextView titleTextView;
    private TextView makerTextView;
    private TextView descriptionTextView;
    private TextView lengthTextView;
    private TextView widthTextView;
    private TextView heightTextView;
    private TextView currentBidRightTextView;
    private TextView currentBidLeftTextView;
    private EditText bidAmount;
    private Button saveBidButton;
    private Button returnItemButton;

    private boolean onCreateUpdate;

    private String titleString;
    private String makerString;
    private String descriptionString;
    private String lengthString;
    private String widthString;
    private String heightString;
    private String userId;
    private String currentBidAmountString;
    private String newBidAmountString;
    private String itemId;

    private Float newBidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        titleTextView = (TextView) findViewById(R.id.title_right_tv);
        makerTextView = (TextView) findViewById(R.id.maker_right_tv);
        descriptionTextView = (TextView) findViewById(R.id.description_right_tv);
        lengthTextView = (TextView) findViewById(R.id.length_tv);
        widthTextView = (TextView) findViewById(R.id.width_tv);
        heightTextView = (TextView) findViewById(R.id.height_tv);
        photo = (ImageView) findViewById(R.id.image_view);
        currentBidRightTextView = (TextView) findViewById(R.id.current_bid_right_tv);
        currentBidLeftTextView = (TextView) findViewById(R.id.current_bid_left_tv);

        bidAmount = (EditText) findViewById(R.id.bid_amount);

        saveBidButton = (Button) findViewById(R.id.save_bid_button);
        returnItemButton = (Button) findViewById(R.id.return_item_button);

        Intent intent = getIntent(); // Get intent from BorrowedItemsActivity/SearchActivity
        itemId = intent.getStringExtra(Constants.ITEM_ID);
        userId = intent.getStringExtra(Constants.USER_ID);

        context = getApplicationContext();

        onCreateUpdate = false; // Suppress first call to update()
        userListController.getRemoteUsers();

        onCreateUpdate = true; // First call to update occurs now
        bidListController.getRemoteBids();
        bidListController.addObserver(this);
        itemListController.addObserver(this);
        itemListController.getRemoteItems();

        onCreateUpdate = false; // Suppress any further calls to update()
    }

    @Override
    public void onBackPressed() {
        Intent borrowIntent = new Intent(this, BorrowedItemsActivity.class);
        borrowIntent.putExtra(Constants.USER_ID, userId);
        startActivity(borrowIntent);
    }

    public void saveBid(View view) {
        titleString = titleTextView.getText().toString();
        makerString = makerTextView.getText().toString();
        descriptionString = descriptionTextView.getText().toString();
        currentBidAmountString = currentBidRightTextView.getText().toString();
        newBidAmountString = bidAmount.getText().toString();
        lengthString = lengthTextView.getText().toString();
        widthString = widthTextView.getText().toString();
        heightString = heightTextView.getText().toString();

        if(!validateInput()){ // Current bid amount must be higher than the previous bid
            return;
        }

        String ownerIdString = itemController.getOwnerId();
        String statusString = "Bidded";
        String minimumBidAmountString = itemController.getMinBid().toString();
        String username = userListController.getUsernameByUserId(userId);

        Bid bid = new Bid(itemId, newBidAmount, username);

        boolean success = bidListController.addBid(bid, context);
        if (!success){
            return;
        }

        // Reuse the item id
        Item updatedItem = new Item(titleString, makerString, descriptionString,ownerIdString, minimumBidAmountString, image, itemId);
        ItemController updatedItemController = new ItemController(updatedItem);

        updatedItemController.setStatus(statusString);
        updatedItemController.setDimensions(lengthString, widthString, heightString);

        success = itemListController.editItem(item, updatedItem);
        if (!success){
            return;
        }

        // End ViewItemActivity
        itemListController.removeObserver(this);
        bidListController.removeObserver(this);

        final Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of SearchActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Bid placed.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);

    }

    public void update() {
        if (onCreateUpdate){
            // For all status options we do the following
            item = itemListController.getItemById(itemId);
            itemController = new ItemController(item);

            titleTextView.setText(itemController.getTitle());
            makerTextView.setText(itemController.getMaker());
            descriptionTextView.setText(itemController.getDescription());

            lengthTextView.setText(itemController.getLength());
            widthTextView.setText(itemController.getWidth());
            heightTextView.setText(itemController.getHeight());

            image = itemController.getImage();
            if (image != null) {
                photo.setImageBitmap(image);
            } else {
                photo.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            String status = itemController.getStatus();

            if (status.equals("Available") || status.equals("Bidded")) {
                Float highestBid = bidListController.getHighestBid(itemId);

                if (highestBid == null) {
                    currentBidRightTextView.setText(String.valueOf(itemController.getMinBid()));
                } else {
                    currentBidRightTextView.setText(String.valueOf(highestBid));
                }
            } else { // Borrowed
                currentBidRightTextView.setVisibility(View.GONE);
                currentBidLeftTextView.setVisibility(View.GONE);
                bidAmount.setVisibility(View.GONE);
                saveBidButton.setVisibility(View.GONE);
                returnItemButton.setVisibility(View.VISIBLE);

            }
        }
    }

    public void returnItem(View view) {

        titleString = titleTextView.getText().toString();
        makerString = makerTextView.getText().toString();
        descriptionString = descriptionTextView.getText().toString();
        lengthString = lengthTextView.getText().toString();
        widthString = widthTextView.getText().toString();
        heightString = heightTextView.getText().toString();
        String status = "Available";
        String ownerId = itemController.getOwnerId();
        String minimumBid = String.valueOf(itemController.getMinBid());

        Item updatedItem = new Item(titleString, makerString, descriptionString, ownerId, minimumBid, image, itemId);
        ItemController updatedItemController = new ItemController(updatedItem);
        updatedItemController.setDimensions(lengthString, widthString, heightString);
        updatedItemController.setStatus(status);

        boolean success = itemListController.editItem(item, updatedItem);
        if (!success){
            return;
        }

        // End ViewItemActivity
        itemListController.removeObserver(this);
        bidListController.removeObserver(this);

        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Item returned.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
    }

    private boolean validateInput() {
        if (newBidAmountString.equals("")) {
            bidAmount.setError("Enter Bid!");
            return false;
        }

        Float currentBidAmount = Float.valueOf(currentBidAmountString);
        newBidAmount = Float.valueOf(newBidAmountString);

        if (newBidAmount <= currentBidAmount){
            bidAmount.setError("New bid amount must be higher than current bid amount!");
            return false;
        }

        return true;
    }
}
