package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Search other user's "Available" and "Bidded" items
 */
public class SearchActivity extends AppCompatActivity implements Observer {

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private ListView allItems;
    private ArrayAdapter<Item> adapter;
    private Context context;
    private String userId;
    private EditText searchEntry;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent(); // Get intent from MainActivity
        userId = intent.getStringExtra(Constants.USER_ID);

        searchEntry = (EditText) findViewById(R.id.search_entry);

        context = getApplicationContext();

        itemListController.addObserver(this);
        userListController.getRemoteUsers();
        itemListController.getRemoteItems();
        itemListController.setItems(itemListController.getSearchItems(userId));

        // When an item is long clicked, this starts ViewItemActivity
        allItems.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Item item = adapter.getItem(pos);
                String itemId = item.getId();

                Intent intent = new Intent(context, ViewItemActivity.class);
                intent.putExtra(Constants.USER_ID, userId);
                intent.putExtra(Constants.ITEM_ID, itemId);
                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchEntry.setText("");
        itemListController.getRemoteItems();
        itemListController.setItems(itemListController.getSearchItems(userId));
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(Constants.USER_ID, userId);
        startActivity(mainIntent);
    }

    protected void onDestroy() {
        super.onDestroy();
        itemListController.removeObserver(this);
    }

    public void keywordSearch(View view) {
        String entry = searchEntry.getText().toString();
        itemListController.getRemoteItems();
        if (entry.equals("")) {
            itemListController.setItems(itemListController.getSearchItems(userId));
            return;
        }

        ArrayList<String> keywords = new ArrayList<>();
        keywords.addAll(splitWords(entry));

        ArrayList<Item> matchingItems = new ArrayList<>();
        for (Item i : itemListController.getSearchItems(userId)) {
            ArrayList<String> itemWords = new ArrayList<>();
            itemWords.addAll(splitWords(i.getTitle()));
            itemWords.addAll(splitWords(i.getMaker()));
            itemWords.addAll(splitWords(i.getDescription()));

            for( String word : itemWords ) {
                for (String key : keywords ) {

                    if (word.equals(key) && !matchingItems.contains(i)) {
                        matchingItems.add(i);
                    }
                }
            }
        }

        itemListController.setItems(matchingItems);

        if (matchingItems.isEmpty()) {
            Toast.makeText(context, "No match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Keyword found.", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> splitWords(String itemString) {
        ArrayList<String> itemWords = new ArrayList<>();
        itemWords.addAll(Arrays.asList(itemString.split("[ ;,.?!@#$%^&*+-_=<>/]")));

        return itemWords;
    }

    /**
     * Update the view
     */
    public void update(){
        allItems = (ListView) findViewById(R.id.all_items);
        adapter = new ItemActivityAdapter(this, itemListController.getItems());
        allItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
