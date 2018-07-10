package com.example.sharingapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass of Item, ItemList, User, UserList, Bid, BidList
 */
@SuppressWarnings("WeakerAccess")
public class Observable {

    private final List<Observer> observers;

    public Observable(){
        observers = new ArrayList<>();
    }

    // Notify observers when need to update any changes made to model
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }
}
