package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Editing a pre-existing user consists of deleting the old user and adding a new user with the old
 * users's id.
 */
public class EditUserActivity extends AppCompatActivity implements Observer {

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private User user;
    private EditText email;
    private TextView username;
    private Context context;
    private boolean onCreateUpdate = true;

    private String emailString;
    private String userId;
    private String usernameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent = getIntent(); // Get intent from MainActivity
        userId = intent.getStringExtra(Constants.USER_ID);

        username = (TextView) findViewById(R.id.username);

        context = getApplicationContext();
        userListController.addObserver(this);
        userListController.getRemoteUsers(); // First call to update()
        onCreateUpdate = false; // Suppress any further calls to update()
    }

    public void saveUser(View view) {
        emailString = email.getText().toString();
        usernameString = username.getText().toString();

        if(!validateInput()){
            return;
        }

        // Reuse the user id
        User updatedUser = new User(usernameString, emailString, userId);

        boolean success = userListController.editUser(user, updatedUser);
        if (!success){
            return;
        }

        // End EditUserActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Profile edited.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);

    }

    /**
     * Called when the activity is destroyed, thus we remove this activity as a listener
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        userListController.removeObserver(this);
    }

    /**
     * Only need to update the view from the onCreate method
     */
    public void update(){
        if (onCreateUpdate) {

            user = userListController.getUserByUserId(userId);
            UserController userController = new UserController(user);

            username = (TextView) findViewById(R.id.username);
            email = (EditText) findViewById(R.id.email);

            username.setText(userController.getUsername());
            email.setText(userController.getEmail());
        }
    }

    private boolean validateInput(){
        if (emailString.equals("")) {
            email.setError("Empty field!");
            return false;
        }

        if (!emailString.contains("@")) {
            email.setError("Must be an email address!");
            return false;
        }

        // Check that username is unique AND username is changed (Note: if username was not changed
        // then this should be fine, because it was already unique.)
        if (!userListController.isUsernameAvailable(usernameString) &&
                !(user.getUsername().equals(usernameString))){
            username.setError("Username already taken!");
            return false;
        }

        return true;
    }
}
