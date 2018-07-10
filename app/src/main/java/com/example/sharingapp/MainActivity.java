package com.example.sharingapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Home Activity of the App
 */
public class MainActivity extends AppCompatActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); // from LoginActivity
        userId = intent.getStringExtra(Constants.USER_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), userId);

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                searchIntent.putExtra(Constants.USER_ID, userId);
                startActivity(searchIntent);
                return true;
            case R.id.borrowed_items:
                Intent borrowedIntent = new Intent(this, BorrowedItemsActivity.class);
                borrowedIntent.putExtra(Constants.USER_ID, userId);
                startActivity(borrowedIntent);
                return true;
            case R.id.edit_profile:
                Intent profileIntent = new Intent(this, EditUserActivity.class);
                profileIntent.putExtra(Constants.USER_ID, userId);
                startActivity(profileIntent);
                return true;
            case R.id.logout:
                Intent logoutIntent = new Intent(this, LoginActivity.class);
                Toast.makeText(getApplicationContext(), "Goodbye", Toast.LENGTH_SHORT).show();
                startActivity(logoutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
    }

    public void addItemActivity(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
    }
}
