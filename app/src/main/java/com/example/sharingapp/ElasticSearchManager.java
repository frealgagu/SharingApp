package com.example.sharingapp;

import android.os.AsyncTask;
import android.util.Log;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;
import io.searchbox.core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * For remote machine: SERVER = "http://34.202.206.222:8080"
 * -------------------------------------------------------------------------------------------------
 * curl -XDELETE 'http://34.202.206.222:8080/INDEX' - can be used to delete ALL objects on the server
 * (items, users, and bids) at that index
 * view an item at: http://34.202.206.222:8080/INDEX/items/item_id
 * view a user at: http://34.202.206.222:8080/INDEX/users/user_id
 * view a bid at: http://34.202.206.222:8080/INDEX/bids/bid_id
 * Where INDEX is replaced with the random number string you generate as per the assignment
 * instructions. Note: item_ids and user_ids are printed to the log (See the Android Monitor)
 * as each user/item is added.
 *
 * curl -XDELETE 'http://34.202.206.222:8080/586533040'
 * http://34.202.206.222:8080/586533040/items/item_id
 * http://34.202.206.222:8080/586533040/users/user_id
 * http://34.202.206.222:8080/586533040/bids/bid_id
 *
 */
@SuppressWarnings("WeakerAccess")
public class ElasticSearchManager {

    private static final String SERVER = "http://34.202.206.222:8080";
    private static final String INDEX = "586533040";
    private static final String ITEM_TYPE = "items";
    private static final String USER_TYPE = "users";
    private static final String BID_TYPE = "bids";
    private static JestDroidClient client;

    /**
     * Returns all remote items from server
     */
    public static class GetItemListTask extends AsyncTask<Void,Void,List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... params) {

            verifyConfig();
            List<Item> items = new ArrayList<>();
            String searchString = "{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(searchString).addIndex(INDEX).addType(ITEM_TYPE).build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    //noinspection deprecation
                    items = execute.getSourceAsObjectList(Item.class);
                    Log.i("ELASTICSEARCH","Item search was successful");
                } else {
                    Log.i("ELASTICSEARCH", "No items found");
                }
            } catch (IOException e) {
                Log.i("ELASTICSEARCH", "Item search failed");
                e.printStackTrace();
            }

            return items;
        }
    }

    /**
     * Add item to remote server
     */
    public static class AddItemTask extends AsyncTask<Item,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Item... params) {

            verifyConfig();
            Boolean success = false;
            Item item = params[0];

            String id = item.getId(); // Explicitly set the id to match the locally generated id
            Index index = new Index.Builder(item).index(INDEX).type(ITEM_TYPE).id(id).build();
            try {
                DocumentResult execute = client.execute(index);
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add item was successful");
                    Log.i("ADDED ITEM", id);
                    success = true;
                } else {
                    Log.e("ELASTICSEARCH", "Add item failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    /**
     * Delete item from remote server using item_id
     */
    public static class RemoveItemTask extends AsyncTask<Item,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Item... params) {

            verifyConfig();
            Boolean success = false;
            Item itemToDelete = params[0];
            try {
                DocumentResult execute = client.execute(new Delete.Builder(itemToDelete.getId()).index(INDEX).type(ITEM_TYPE).build());
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Delete item was successful");
                    success = true;
                } else {
                    Log.e("ELASTICSEARCH", "Delete item failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    /**
     * Returns all remote users from server
     */
    public static class GetUserListTask extends AsyncTask<Void,Void,List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            verifyConfig();

            List<User> users = new ArrayList<>();
            String searchString = "{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(searchString).addIndex(INDEX).addType(USER_TYPE).build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    //noinspection deprecation
                    List<User> remoteUsers = execute.getSourceAsObjectList(User.class);
                    users.addAll(remoteUsers);
                    Log.i("ELASTICSEARCH","User search was successful");
                } else {
                    Log.i("ELASTICSEARCH", "No users found");
                }
            } catch (IOException e) {
                Log.i("ELASTICSEARCH", "User search failed");
                e.printStackTrace();
            }

            return users;
        }
    }

    /**
     * Add user to remote server
     */
    public static class AddUserTask extends AsyncTask<User,Void,Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {

            verifyConfig();
            Boolean success = false;
            User user = params[0];

            String id = user.getId(); // Explicitly set the id to match the locally generated id
            Index index = new Index.Builder(user).index(INDEX).type(USER_TYPE).id(id).build();
            try {
                DocumentResult execute = client.execute(index);
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "User was successfully added");
                    Log.i("ADDED USER", id);
                    success = true;
                } else {
                    Log.e("ELASTICSEARCH", "User failed to be added");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    /**
     * Delete user from remote server using userId
     */
    public static class RemoveUserTask extends AsyncTask<User,Void,Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {

            verifyConfig();
            Boolean success = false;
            User user = params[0];
            try {
                DocumentResult execute = client.execute(new Delete.Builder(user.getId()).index(INDEX).type(USER_TYPE).build());
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "User was successfully deleted");
                    success = true;
                } else {
                    Log.e("ELASTICSEARCH", "User delete failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    /**
     * Returns all remote bids from server
     */
    public static class GetBidListTask extends AsyncTask<Void,Void,List<Bid>> {

        @Override
        protected List<Bid> doInBackground(Void... params) {

            verifyConfig();
            List<Bid> bids = new ArrayList<>();
            String search_string = "{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(search_string).addIndex(INDEX).addType(BID_TYPE).build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    //noinspection deprecation
                    bids = execute.getSourceAsObjectList(Bid.class);
                    Log.i("ELASTICSEARCH","Bid search was successful");
                } else {
                    Log.i("ELASTICSEARCH", "No bids found");
                }
            } catch (IOException e) {
                Log.i("ELASTICSEARCH", "Bid search failed");
                e.printStackTrace();
            }

            return bids;
        }
    }

    /**
     * Add bid to remote server
     */
    public static class AddBidTask extends AsyncTask<Bid,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Bid... params) {

            verifyConfig();
            Boolean success = false;
            Bid bid = params[0];

            String id = bid.getBidId(); // Explicitly set the id to match the locally generated id
            Index index = new Index.Builder(bid).index(INDEX).type(BID_TYPE).id(id).build();
            try {
                DocumentResult execute = client.execute(index);
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add bid was successful");
                    Log.i("ADDED BID", id);
                    success = true;
                } else {
                    Log.e("ELASTICSEARCH", "Add bid failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    /**
     * Delete bid from remote server using bid_id
     */
    public static class RemoveBidTask extends AsyncTask<Bid,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Bid... params) {
            verifyConfig();
            Boolean success = false;
            Bid bidToDelete = params[0];
            try {
                DocumentResult execute = client.execute(new Delete.Builder(bidToDelete.getBidId()).index(INDEX).type(BID_TYPE).build());
                if(execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Delete bid was successful");
                    success = true;
                } else {
                    Log.e("ELASTICSEARCH", "Delete bid failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }
    }

    // If no client, add one
    private static void verifyConfig() {
        if(client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(SERVER);
            DroidClientConfig config = builder.build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
