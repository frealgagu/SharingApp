package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Add a new item
 */
public class AddItemActivity extends AppCompatActivity {

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;
    private EditText minBid;

    private String userId;
    private ImageView photo;
    private Bitmap image;
    private int REQUEST_CODE = 1;

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private Context context;

    private String titleString;
    private String makerString;
    private String descriptionString;
    private String lengthString;
    private String widthString;
    private String heightString;
    private String minBidString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);

        Intent intent = getIntent(); // Get intent from MainActivity
        userId = intent.getStringExtra(Constants.USER_ID);

        title = (EditText) findViewById(R.id.title);
        maker = (EditText) findViewById(R.id.maker);
        description = (EditText) findViewById(R.id.description);
        length = (EditText) findViewById(R.id.length);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        photo = (ImageView) findViewById(R.id.image_view);
        minBid = (EditText) findViewById(R.id.minimum_bid);

        photo.setImageResource(android.R.drawable.ic_menu_gallery);

        context = getApplicationContext();
        itemListController.getRemoteItems();
    }

    public void saveItem (View view) {

        titleString = title.getText().toString();
        makerString = maker.getText().toString();
        descriptionString = description.getText().toString();
        lengthString = length.getText().toString();
        widthString = width.getText().toString();
        heightString = height.getText().toString();
        minBidString = minBid.getText().toString();

        if(!validateInput()){
            return;
        }

        Item item = new Item(titleString, makerString, descriptionString, userId, minBidString, image, null);
        ItemController itemController = new ItemController(item);
        itemController.setDimensions(lengthString, widthString, heightString);

        boolean success = itemListController.addItem(item);
        if (!success){
            return;
        }

        // End AddItemActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of new activity to allow server more time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Item created.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
        }
        Toast.makeText(context, "Photo added.", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("Duplicates")
    private boolean validateInput() {
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

        if (minBidString.equals("")) {
            minBid.setError("Empty field!");
            return false;
        }

        if (Float.valueOf(minBidString) <= 0) {
            minBid.setError("Starting bid must be above 0!");
            return false;
        }
        return true;
    }
}
