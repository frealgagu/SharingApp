package com.example.sharingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Displays a list of all "Bidded" items
 */
@SuppressWarnings("WeakerAccess")
public class BiddedItemsFragment extends ItemsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        super.setUserId(this.getArguments());
        super.setVariables(R.layout.bidded_items_fragment, R.id.my_bidded_items);
        super.loadItems(BiddedItemsFragment.this);
        super.setFragmentOnItemLongClickListener();

        return rootView;
    }

    public List<Item> filterItems() {
        String status = "Bidded";
        return itemListController.filterItems(userId, status);
    }
}