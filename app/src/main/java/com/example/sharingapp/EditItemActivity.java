package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

/**
 * Editing a pre-existing item consists of deleting the old item and adding a new item with the old
 * item's id.
 */
public class EditItemActivity extends AppCompatActivity implements Observer {

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private Item item;
    private ItemController itemController;

    private Context context;

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private Bitmap image;
    private int REQUEST_CODE = 1;
    private ImageView photo;

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;
    private EditText minimumBid;

    private TextView borrowerLeftTextView;
    private TextView borrowerRightTextView;
    private TextView statusRightTextView;

    private Button saveButton;
    private Button viewBidsButton;
    private Button contactInfoButton;
    private Button deleteButton;
    private Button setAvailableButton;
    private ImageButton addImageButton;
    private ImageButton deleteImageButton;

    private boolean onCreateUpdate;
    private int pos;

    private String titleString;
    private String makerString;
    private String descriptionString;
    private String lengthString;
    private String widthString;
    private String heightString;
    private String userId;
    private String minimumBidString;
    private String statusString;
    private String borrowerUsernameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        title = (EditText) findViewById(R.id.title);
        maker = (EditText) findViewById(R.id.maker);
        description = (EditText) findViewById(R.id.description);
        length = (EditText) findViewById(R.id.length);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        statusRightTextView = (TextView) findViewById(R.id.status_right_tv);
        minimumBid = (EditText) findViewById(R.id.minimum_bid);                   // initially GONE
        borrowerLeftTextView = (TextView) findViewById(R.id.borrower_left_tv);    // initially GONE
        borrowerRightTextView = (TextView) findViewById(R.id.borrower_right_tv);  // initially GONE
        photo = (ImageView) findViewById(R.id.image_view);

        addImageButton = (ImageButton) findViewById(R.id.add_image_button);       // initially GONE
        deleteImageButton = (ImageButton) findViewById(R.id.cancel_image_button); // initially GONE
        deleteButton = (Button) findViewById(R.id.delete_item);                   // initially GONE
        saveButton = (Button) findViewById(R.id.save_button);                     // initially GONE
        viewBidsButton = (Button) findViewById(R.id.view_bids_button);            // initially GONE
        setAvailableButton = (Button) findViewById(R.id.set_available_button);    // initially GONE
        contactInfoButton = (Button) findViewById(R.id.contact_info_button);      // initially GONE

        Intent intent = getIntent(); // Get intent from ItemsFragment
        pos = intent.getIntExtra("position", 0);
        userId = intent.getStringExtra(Constants.USER_ID);

        context = getApplicationContext();

        onCreateUpdate = false; // Suppress first call to update()
        itemListController.addObserver(this);
        itemListController.getRemoteItems();

        onCreateUpdate = true;
        userListController.addObserver(this);
        userListController.getRemoteUsers(); // Call update occurs

        onCreateUpdate = false; // Suppress any further calls to update()
    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
        Toast.makeText(context, "Photo removed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
            Toast.makeText(context, "Photo added.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(Constants.USER_ID, userId);
        startActivity(mainIntent);
    }

    public void deleteItem(View view) {
        boolean success = itemListController.deleteItem(item);
        if (!success){
            return;
        }

        // End EditItemActivity
        itemListController.removeObserver(this);
        userListController.removeObserver(this);

        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of new activity to allow server more time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Item removed.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
    }

    public void saveItem(View view) {
        titleString = title.getText().toString();
        makerString = maker.getText().toString();
        descriptionString = description.getText().toString();
        lengthString = length.getText().toString();
        widthString = width.getText().toString();
        heightString = height.getText().toString();
        statusString = itemController.getStatus();
        minimumBidString = minimumBid.getText().toString();

        if(!validateInput()){
            return;
        }

        String itemId = itemController.getId(); // Reuse the item id

        Item updatedItem = new Item(titleString, makerString, descriptionString, userId, minimumBidString, image, itemId);
        ItemController updatedItemController = new ItemController(updatedItem);
        updatedItemController.setDimensions(lengthString, widthString, heightString);
        updatedItemController.setStatus(statusString);

        boolean success = itemListController.editItem(item, updatedItem);

        if (!success){
            return;
        }

        itemListController.removeObserver(this);
        userListController.removeObserver(this);

        // End EditItemActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Item saved.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
    }

    public void viewBids(View view){
        userListController.removeObserver(this);
        itemListController.removeObserver(this);

        Intent intent = new Intent(this, ViewItemBidsActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        intent.putExtra(Constants.ITEM_ID, itemController.getId());
        startActivity(intent);
    }

    public void setAvailable(View view){
        itemController.setStatus("Available"); // Update status
        saveItem(view); // Must save the item so that the change in status is saved
    }

    /**
     * Only need to update the view once from the onCreate method
     */
    public void update() {
        if (onCreateUpdate){

            // For all status options we do the following
            item = itemListController.getItem(pos);
            itemController = new ItemController(item);

            title.setText(itemController.getTitle());
            maker.setText(itemController.getMaker());
            description.setText(itemController.getDescription());
            length.setText(itemController.getLength());
            width.setText(itemController.getWidth());
            height.setText(itemController.getHeight());

            statusString = itemController.getStatus();
            statusRightTextView.setText(statusString);

            minimumBidString = itemController.getMinBid().toString();
            minimumBid.setText(minimumBidString);

            image = itemController.getImage();
            if (image != null) {
                photo.setImageBitmap(image);
            } else {
                photo.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            // AVAILABLE
            if (statusString.equals("Available")){

                title.setEnabled(true);
                maker.setEnabled(true);
                description.setEnabled(true);
                length.setEnabled(true);
                width.setEnabled(true);
                height.setEnabled(true);
                minimumBid.setEnabled(true);

                addImageButton.setVisibility(View.VISIBLE);
                deleteImageButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
            }

            // BIDDED
            if (statusString.equals("Bidded")){

                viewBidsButton.setVisibility(View.VISIBLE);
                title.setEnabled(false);
                maker.setEnabled(false);
                description.setEnabled(false);
                length.setEnabled(false);
                width.setEnabled(false);
                height.setEnabled(false);
                minimumBid.setEnabled(false);
            }

            // BORROWED
            if (statusString.equals("Borrowed")){

                title.setEnabled(false);
                maker.setEnabled(false);
                description.setEnabled(false);
                length.setEnabled(false);
                width.setEnabled(false);
                height.setEnabled(false);
                minimumBid.setEnabled(false);

                borrowerLeftTextView.setVisibility(View.VISIBLE);
                borrowerRightTextView.setVisibility(View.VISIBLE);
                contactInfoButton.setVisibility(View.VISIBLE);
                setAvailableButton.setVisibility(View.VISIBLE);

                User borrower = itemController.getBorrower();
                borrowerUsernameString = borrower.getUsername();
                borrowerRightTextView.setText(borrowerUsernameString);
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private boolean validateInput(){
        if (titleString.equals("")) {
            title.setError("Empty field!");
            return false;
        }

        if (makerString.equals("")) {
            maker.setError("Empty field!");
            return false;
        }

        if (descriptionString.equals("")) {
            description.setError("Empty field!");
            return false;
        }

        if (lengthString.equals("")) {
            length.setError("Empty field!");
            return false;
        }

        if (widthString.equals("")) {
            width.setError("Empty field!");
            return false;
        }

        if (heightString.equals("")) {
            height.setError("Empty field!");
            return false;
        }

        if (minimumBidString.equals("")) {
            minimumBid.setError("Empty field!");
            return false;
        }

        if (Float.valueOf(minimumBidString) <= 0) {
            minimumBid.setError("Starting bid must be above 0!");
            return false;
        }

        return true;
    }

    public void viewUserActivity(View view){
        userListController.removeObserver(this);
        itemListController.removeObserver(this);

        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(Constants.BORROWER_USERNAME_STR, borrowerUsernameString);
        startActivity(intent);
    }
}
