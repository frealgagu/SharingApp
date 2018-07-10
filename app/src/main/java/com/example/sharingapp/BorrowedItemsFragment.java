package com.example.sharingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Displays a list of all "Borrowed" items
 */
@SuppressWarnings("WeakerAccess")
public class BorrowedItemsFragment extends ItemsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        super.setUserId(this.getArguments());
        super.setVariables(R.layout.borrowed_items_fragment, R.id.my_borrowed_items);
        super.loadItems(BorrowedItemsFragment.this);
        super.setFragmentOnItemLongClickListener();

        return rootView;
    }

    public List<Item> filterItems() {
        String status = "Borrowed";
        return item_list_controller.filterItems(user_id, status);
    }
}
