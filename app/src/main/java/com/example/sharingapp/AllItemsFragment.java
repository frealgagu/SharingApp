package com.example.sharingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Displays a list of all items
 */
@SuppressWarnings("WeakerAccess")
public class AllItemsFragment extends ItemsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        super.setUserId(this.getArguments());
        super.setVariables(R.layout.all_items_fragment, R.id.my_items);
        super.loadItems(AllItemsFragment.this);
        super.setFragmentOnItemLongClickListener();

        return rootView;
    }

    public List<Item> filterItems() {
        return itemListController.getMyItems(userId);
    }
}
