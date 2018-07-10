package com.example.sharingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@SuppressWarnings("WeakerAccess")
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private String userId;

    public SectionsPagerAdapter(FragmentManager fm, String userId) {
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        switch (position) {
            case 0:
                AllItemsFragment allItemsFragment = new AllItemsFragment();
                allItemsFragment.setArguments(bundle);
                return allItemsFragment;
            case 1:
                AvailableItemsFragment availableItemsFragment = new AvailableItemsFragment();
                availableItemsFragment.setArguments(bundle);
                return availableItemsFragment;
            case 2:
                BiddedItemsFragment biddedItemsFragment = new BiddedItemsFragment();
                biddedItemsFragment.setArguments(bundle);
                return biddedItemsFragment;
            case 3:
                BorrowedItemsFragment borrowedItemsFragment = new BorrowedItemsFragment();
                borrowedItemsFragment.setArguments(bundle);
                return borrowedItemsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4; // Four pages
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "Available";
            case 2:
                return "Bidded";
            case 3:
                return "Borrowed";

        }
        return null;
    }
}