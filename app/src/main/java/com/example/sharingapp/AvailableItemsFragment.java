package com.example.sharingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Displays a list of all "Available" items
 */
@SuppressWarnings("WeakerAccess")
public class AvailableItemsFragment extends ItemsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        super.setUserId(this.getArguments());
        super.setVariables(R.layout.available_items_fragment, R.id.my_available_items);
        super.loadItems(AvailableItemsFragment.this);
        super.setFragmentOnItemLongClickListener();

        return rootView;
    }

    public List<Item> filterItems() {
        String status = "Available";
        return itemListController.filterItems(userId, status);
    }
}
