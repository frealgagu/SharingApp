package com.example.sharingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ItemActivityAdapter is responsible for what information is displayed in ListView entries.
 */
@SuppressWarnings("WeakerAccess")
public class ItemActivityAdapter extends ArrayAdapter<Item> {

    private LayoutInflater inflater;

    public ItemActivityAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        ItemController itemController = new ItemController(item);

        String title = "Title: " + itemController.getTitle();
        String description = "Description: " + itemController.getDescription();
        Bitmap thumbnail = itemController.getImage();
        String status = "Status: " + itemController.getStatus();

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemlist_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.title_tv);
        TextView statusTextView = convertView.findViewById(R.id.status_tv);
        TextView descriptionTextView = convertView.findViewById(R.id.description_tv);
        ImageView photo = convertView.findViewById(R.id.image_view);

        if (thumbnail != null) {
            photo.setImageBitmap(thumbnail);
        } else {
            photo.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        statusTextView.setText(status);

        return convertView;
    }
}

