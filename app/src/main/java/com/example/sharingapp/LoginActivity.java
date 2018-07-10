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
 * Users must log into the app
 */
public class LoginActivity extends AppCompatActivity {

    private UserList userList = new UserList();
    private UserListController userListController = new UserListController(userList);

    private EditText username;
    private EditText email;
    private TextView emailTextView;
    private Context context;
    private String usernameString;
    private String emailString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        emailTextView = (TextView) findViewById(R.id.email_tv);

        email.setVisibility(View.GONE);
        emailTextView.setVisibility(View.GONE);

        context = getApplicationContext();
        userListController.getRemoteUsers();
    }

    public void login(View view) {
        usernameString = username.getText().toString();
        emailString = email.getText().toString();
        String userId;

        if (userListController.getUserByUsername(usernameString) == null && email.getVisibility() == View.GONE) {
            email.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);
            email.setError("New user! Must enter email!");
            return;
        }

        // User does not already have an account
        if (userListController.getUserByUsername(usernameString) == null && email.getVisibility() == View.VISIBLE){
            if(!validateInput()){
                return;
            }

            User user = new User(usernameString, emailString, null);
            UserController userController = new UserController(user);
            userId = userController.getId();

            boolean success = userListController.addUser(user);
            if (!success){
                return;
            }

            Toast.makeText(context, "Profile created.", Toast.LENGTH_SHORT).show();
        } else { // User already has an account
            userId = userListController.getUserIdByUsername(usernameString);
        }

        // Either way, start MainActivity
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.USER_ID, userId);

        // Delay launch of MainActivity to allow server enough time to process request
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }, 750);
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

        if (userListController.getUserByUsername(usernameString) != null) {
            username.setError("Username already taken!");
            return false;
        }

        return true;
    }
}
