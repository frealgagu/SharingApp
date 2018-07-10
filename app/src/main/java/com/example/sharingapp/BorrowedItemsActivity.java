package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BorrowedItemsActivity extends AppCompatActivity implements Observer {

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private ListView borrowedItems;
    private ArrayAdapter<Item> adapter;
    private Context context;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_borrowed_items);

        Intent intent = getIntent(); // Get intent from MainActivity
        userId = intent.getStringExtra(Constants.USER_ID);

        context = getApplicationContext();

        userListController.getRemoteUsers();
        String username = userListController.getUsernameByUserId(userId);

        itemListController.addObserver(this);
        itemListController.getRemoteItems();
        itemListController.setItems(itemListController.getBorrowedItemsByUsername(username));

        // When an item is long clicked, this starts ViewItemActivity
        borrowedItems.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Item item = adapter.getItem(pos);
                String itemId = item.getId();

                itemListController.removeObserver(BorrowedItemsActivity.this);

                Intent intent = new Intent(context, ViewItemActivity.class);
                intent.putExtra(Constants.USER_ID, userId);
                intent.putExtra(Constants.ITEM_ID, itemId);
                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(Constants.USER_ID, userId);
        startActivity(mainIntent);
    }

    /**
     * Update the view
     */
    public void update(){
        borrowedItems = (ListView) findViewById(R.id.borrowed_items);
        adapter = new ItemActivityAdapter(this, itemListController.getItems());
        borrowedItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
