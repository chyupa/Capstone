package com.udacity.capstone.activities.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.udacity.capstone.activities.map.MapsActivity;
import com.udacity.capstone.R;
import com.udacity.capstone.activities.login.LoginActivity;
import com.udacity.capstone.activities.main.MainActivity;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.MapInfo;
import com.udacity.capstone.models.User;

public class LoggedInProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private User profile;
    private SimpleDraweeView profileImage;
    private TextView profileName;
    private TextView profileEmail;

    //profile text views
    private TextView bioText;
    private TextView skillsText;
    private TextView postcodeText;
    private TextView rateText;

    private Button mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setTitle(getString(R.string.dashboard));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * get the navigation info
         */
        View header = navigationView.getHeaderView(0);

        profileImage = (SimpleDraweeView) header.findViewById(R.id.profileImage);
        profileName = (TextView) header.findViewById(R.id.profileName);
        profileEmail = (TextView) header.findViewById(R.id.profileEmail);

        /**
         * get the user info
         */
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userId", 0);
        final String userGson = sharedPreferences.getString("user", "");

        if (userGson.isEmpty()) {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        }

        profile = new Gson().fromJson(userGson, User.class);

        /**
         * set navigation info
         */
        setNavigationInfo();

        /**
         * get profile text views
         */

        bioText = (TextView) findViewById(R.id.bioText);
        skillsText = (TextView) findViewById(R.id.skillsText);
        postcodeText = (TextView) findViewById(R.id.postcodeText);
        rateText = (TextView) findViewById(R.id.rateText);

        /**
         * set profile info
         */
        setProfileInfo();

        mapBtn = (Button) findViewById(R.id.seeOnMapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);

                MapInfo mapInfo = new MapInfo(profile.getProfile().getName(),
                        profile.getProfile().getBio(),
                        profile.getProfile().getPostcodeInfo().getLat(),
                        profile.getProfile().getPostcodeInfo().getLon()
                );

                String mapInfoGson = new Gson().toJson(mapInfo);
                mapIntent.putExtra("mapInfo", mapInfoGson);
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_check_other_profiles) {
            Intent profilesIntent = new Intent(getApplicationContext(), ProfilesActivity.class);
            startActivity(profilesIntent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            CapstoneWebService capstoneWebService = new CapstoneWebService();
            capstoneWebService.service.logout();

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userId", 0);
            sharedPreferences
                    .edit()
                    .putString("user", "")
                    .apply();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setProfileInfo() {
        bioText.setText(profile.getProfile().getBio());
        skillsText.setText(profile.getProfile().getSkills());
        postcodeText.setText(profile.getProfile().getPostcode());
        rateText.setText(String.format(getString(R.string.rate), profile.getProfile().getRate()));
    }

    private void setNavigationInfo() {

        Uri profileImageUri = Uri.parse(profile.getProfile().getProfile_image());
        profileImage.setImageURI(profileImageUri);
        profileName.setText(profile.getProfile().getName());
        profileEmail.setText(profile.getEmail());
    }
}
