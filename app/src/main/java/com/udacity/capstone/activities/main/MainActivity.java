package com.udacity.capstone.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.udacity.capstone.activities.profile.LoggedInProfileActivity;
import com.udacity.capstone.activities.login.LoginActivity;
import com.udacity.capstone.activities.profile.ProfilesActivity;
import com.udacity.capstone.R;
import com.udacity.capstone.activities.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userId", 0);

        String gsonUser = sharedPreferences.getString("user", "");
        if (!gsonUser.isEmpty()) {
            Intent loggedInIntent = new Intent(getApplicationContext(), LoggedInProfileActivity.class);
            startActivity(loggedInIntent);
        }

        Button createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                startActivity(intent);
            }
        });

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                startActivity(intent);
            }
        });

        Button lookForProfilesBtn = (Button) findViewById(R.id.lookForProfilesBtn);
        lookForProfilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilesActivity.class);
                startActivity(intent);
            }
        });

    }
}
