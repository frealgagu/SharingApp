package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Superclass of AvailableItemsFragment, BorrowedItemsFragment and AllItemsFragment
 */
@SuppressWarnings("WeakerAccess")
public abstract class ItemsFragment extends Fragment implements Observer {

    private ItemList itemList = new ItemList();

    ItemListController itemListController = new ItemListController(itemList);
    String userId;
    View rootView;

    private ListView listView;
    private ArrayAdapter<Item> adapter;
    private List<Item> selectedItems;
    private LayoutInflater inflater;
    private ViewGroup container;
    private Context context;
    private Fragment fragment;
    private boolean update = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();

        itemListController.getRemoteItems(); // Call to update() suppressed
        update = true; // Future calls to update() permitted

        this.inflater = inflater;
        this.container = container;

        return rootView;
    }

    public void setVariables(int resource, int id ) {
        rootView = inflater.inflate(resource, container, false);
        listView = rootView.findViewById(id);
        selectedItems = filterItems();
    }

    public void setUserId(Bundle b) {
        this.userId = b.getString(Constants.USER_ID, userId);
    }

    public void loadItems(Fragment fragment){
        this.fragment = fragment;
        itemListController.addObserver(this);
        itemListController.getRemoteItems();
    }

    public void setFragmentOnItemLongClickListener(){
        // When item is long clicked, this starts EditItemActivity
        listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Item item = adapter.getItem(pos);

                int metaPos = itemListController.getIndex(item);
                if (metaPos >= 0) {

                    Intent edit = new Intent(context, EditItemActivity.class);
                    edit.putExtra(Constants.USER_ID, userId);
                    edit.putExtra("position", metaPos);
                    startActivity(edit);
                }
                return true;
            }
        });
    }

    /**
     * filterItems is implemented independently by AvailableItemsFragment, BorrowedItemsFragment and AllItemsFragment
     * @return selectedItems
     */
    public abstract List<Item> filterItems();

    /**
     * Called when the activity is destroyed, thus we remove this fragment as an observer
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        itemListController.removeObserver(this);
    }

    /**
     * Update the view
     */
    public void update(){
        if (update) {
            selectedItems = filterItems(); // Ensure items are filtered
            adapter = new ItemFragmentAdapter(context, selectedItems, fragment);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
