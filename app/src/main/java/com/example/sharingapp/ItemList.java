package com.example.sharingapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * ItemList class
 */
@SuppressWarnings("WeakerAccess")
public class ItemList extends Observable {

    private static final List<Item> ITEMS = new ArrayList<>();

    public ItemList() {
    }

    public void setItems(List<Item> itemList) {
        ITEMS.clear();
        ITEMS.addAll(itemList);
        notifyObservers();
    }

    public List<Item> getItems() {
        return new ArrayList<>(ITEMS);
    }

    public Item getItem(int index) {
        return ITEMS.get(index);
    }

    public boolean hasItem(Item item) {
        for (Item i : ITEMS) {
            if (item.getId().equals(i.getId())) {
                return true;
            }
        }
        return false;
    }

    public int getIndex(Item item) {
        int pos = 0;
        for (Item i : ITEMS) {
            if (item.getId().equals(i.getId())) {
                return pos;
            }
            pos = pos + 1;
        }
        return -1;
    }

    public int getSize() {
        return ITEMS.size();
    }

    // Used by AvailableItemsFragment, BorrowedItemsFragment, and BiddedItemsFragment
    public List<Item> filterItems(String userId, String status) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item i: ITEMS) {
            if (i.getOwnerId().equals(userId) && i.getStatus().equals(status)) {
                selectedItems.add(i);
            }
        }
        return selectedItems;
    }

    // Used by AllItemsFragment
    public List<Item> getMyItems(String userId) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item i: ITEMS) {
            if (i.getOwnerId().equals(userId)) {
                selectedItems.add(i);
            }
        }
        return selectedItems;
    }

    // Used by SearchItemsActivity
    public List<Item> getSearchItems(String userId) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item i: ITEMS) {
            if (!i.getOwnerId().equals(userId) && !i.getStatus().equals("Borrowed")) {
                selectedItems.add(i);
            }
        }
        return selectedItems;
    }

    // Used by BorrowedItemsActivity
    public List<Item> getBorrowedItemsByUsername(String username) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item i: ITEMS) {
            if (i != null && i.getBorrower() != null) {
                if (i.getBorrowerUsername().equals(username)) {
                    selectedItems.add(i);
                }
            }
        }
        return selectedItems;
    }

    public Item getItemById(String id){
        for (Item i: ITEMS) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    public void getRemoteItems() {
        ElasticSearchManager.GetItemListTask getItemListTask = new ElasticSearchManager.GetItemListTask();
        getItemListTask.execute();

        try {
            ITEMS.clear();
            ITEMS.addAll(getItemListTask.get());
        } catch (InterruptedException | ExecutionException ex) {
            Log.e("ITEM_LIST", ex.getMessage(), ex);
        }
        notifyObservers();
    }
}
