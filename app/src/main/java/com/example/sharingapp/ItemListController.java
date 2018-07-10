package com.example.sharingapp;

import java.util.List;

/**
 * ItemListController is responsible for all communication between views and ItemList model
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ItemListController {

    private ItemList itemList;

    public ItemListController(ItemList itemList){
        this.itemList = itemList;
    }

    public void setItems(List<Item> itemList) {
        this.itemList.setItems(itemList);
    }

    public List<Item> getItems() {
        return itemList.getItems();
    }

    public List<Item> getMyItems(String userId) {
        return itemList.getMyItems(userId);
    }

    public boolean addItem(Item item){
        AddItemCommand addItemCommand = new AddItemCommand(item);
        addItemCommand.execute();
        return addItemCommand.isExecuted();
    }

    public boolean deleteItem(Item item) {
        DeleteItemCommand deleteItemCommand = new DeleteItemCommand(item);
        deleteItemCommand.execute();
        return deleteItemCommand.isExecuted();
    }

    public boolean editItem(Item item, Item updatedItem){
        EditItemCommand editItemCommand = new EditItemCommand(item, updatedItem);
        editItemCommand.execute();
        return editItemCommand.isExecuted();
    }

    public Item getItem(int index) {
        return itemList.getItem(index);
    }

    public boolean hasItem(Item item) {
        return itemList.hasItem(item);
    }

    public int getIndex(Item item) {
        return itemList.getIndex(item);
    }

    public int getSize() {
        return itemList.getSize();
    }

    public List<Item> filterItems(String userId, String status) {
        return itemList.filterItems(userId, status);
    }

    public List<Item> getSearchItems(String userId) {
        return itemList.getSearchItems(userId);
    }

    public List<Item> getBorrowedItemsByUsername(String username) {
        return itemList.getBorrowedItemsByUsername(username);
    }

    public Item getItemById(String id){
        return itemList.getItemById(id);
    }

    public void addObserver(Observer observer) {
        itemList.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        itemList.removeObserver(observer);
    }

    public void getRemoteItems(){
        itemList.getRemoteItems();
    }
}
