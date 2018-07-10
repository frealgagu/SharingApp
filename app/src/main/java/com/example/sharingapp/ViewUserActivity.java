package com.example.sharingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewUserActivity extends AppCompatActivity {

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Intent intent = getIntent(); // Get intent from EditItemActivity
        String usernameString = intent.getStringExtra(Constants.BORROWER_USERNAME_STR);

        TextView username = (TextView) findViewById(R.id.username_right_tv);
        TextView email = (TextView) findViewById(R.id.email_right_tv);

        userListController.getRemoteUsers();

        User user = userListController.getUserByUsername(usernameString);
        UserController userController = new UserController(user);

        username.setText(usernameString);
        email.setText(userController.getEmail());
    }
}
