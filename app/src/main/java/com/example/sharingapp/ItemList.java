package com.example.sharingapp;

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

    public void setItems(List<Item> item_list) {
        ITEMS.clear();
        ITEMS.addAll(item_list);
        notifyObservers();
    }

    public List<Item> getItems() {
        return new ArrayList<>(ITEMS);
    }

    @SuppressWarnings("unused")
    public void addItem(Item item) {
        ITEMS.add(item);
        notifyObservers();
    }

    @SuppressWarnings("unused")
    public void deleteItem(Item item) {
        ITEMS.remove(item);
        notifyObservers();
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
    public List<Item> filterItems(String user_id, String status) {
        List<Item> selected_items = new ArrayList<>();
        for (Item i: ITEMS) {
            if (i.getOwnerId().equals(user_id) && i.getStatus().equals(status)) {
                selected_items.add(i);
            }
        }
        return selected_items;
    }

    // Used by AllItemsFragment
    public List<Item> getMyItems(String user_id) {
        List<Item> selected_items = new ArrayList<>();
        for (Item i: ITEMS) {
            if (i.getOwnerId().equals(user_id)) {
                selected_items.add(i);
            }
        }
        return selected_items;
    }

    // Used by SearchItemsActivity
    public List<Item> getSearchItems(String user_id) {
        List<Item> selected_items = new ArrayList<>();
        for (Item i: ITEMS) {
            if (!i.getOwnerId().equals(user_id) && !i.getStatus().equals("Borrowed")) {
                selected_items.add(i);
            }
        }
        return selected_items;
    }

    // Used by BorrowedItemsActivity
    public List<Item> getBorrowedItemsByUsername(String username) {
        List<Item> selected_items = new ArrayList<>();
        for (Item i: ITEMS) {
            if (i != null && i.getBorrower() != null) {
                if (i.getBorrowerUsername().equals(username)) {
                    selected_items.add(i);
                }
            }
        }
        return selected_items;
    }

    public Item getItemById(String id){
        for (Item i: ITEMS) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    public void getRemoteItems(){
        ElasticSearchManager.GetItemListTask get_item_list_task = new ElasticSearchManager.GetItemListTask();
        get_item_list_task.execute();

        try {
            ITEMS.clear();
            ITEMS.addAll(get_item_list_task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        notifyObservers();
    }
}
