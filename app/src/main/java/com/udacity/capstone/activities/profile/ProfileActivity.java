package com.udacity.capstone.activities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.udacity.capstone.activities.map.MapsActivity;
import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.MapInfo;
import com.udacity.capstone.models.Profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private String LOG_TAG = getClass().getSimpleName();
    private Profile profile;

    private TextView profileName;
    private ImageView profileImage;
    private TextView profileBio;
    private TextView profileSkills;
    private TextView profilePostcode;
    private Button seeOnMapBtn;
    private FloatingActionButton fab;
    private boolean isRestored = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String profileInfoGson = new Gson().toJson(profile);
        outState.putString("profileInfo", profileInfoGson);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileName = (TextView) findViewById(R.id.profileName);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        profileBio = (TextView) findViewById(R.id.bioText);
        profileSkills = (TextView) findViewById(R.id.skillsText);
        profilePostcode = (TextView) findViewById(R.id.postcodeText);
        seeOnMapBtn = (Button) findViewById(R.id.seeOnMapBtn);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int profileId = getIntent().getIntExtra("profileId", 0);
        if (profileId == 0) {
            returnToProfilesActivityWithError();
        }

        if (savedInstanceState != null) {
            String profileInfoGson = savedInstanceState.getString("profileInfo");
            profile = new Gson().fromJson(profileInfoGson, Profile.class);
            setInfo(profile);
        } else {
            CapstoneWebService capstoneWebService = new CapstoneWebService();
            Call<Profile> profileCall = capstoneWebService.service.getProfileById(profileId);
            profileCall.enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    profile = response.body();
                    setInfo(profile);
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    returnToProfilesActivityWithError();
                }
            });
        }
    }

    private void setInfo(final Profile profile) {
        Glide.with(getApplicationContext())
                .load(profile.getProfile_image())
                .centerCrop()
                .fallback(R.drawable.ic_profile_image)
                .into(profileImage);

        profileName.setText(profile.getName());
        profileBio.setText(profile.getBio());
        profileSkills.setText(profile.getSkills());
        profilePostcode.setText(profile.getPostcode());

        if (profile.getPostcode().isEmpty()) {
            seeOnMapBtn.setClickable(false);
            seeOnMapBtn.setVisibility(View.INVISIBLE);
        } else {
            seeOnMapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                    MapInfo mapInfo = new MapInfo(profile.getName(),
                            profile.getBio(),
                            profile.getPostcodeInfo().getLat(),
                            profile.getPostcodeInfo().getLon()
                    );

                    String mapInfoGson = new Gson().toJson(mapInfo);
                    mapIntent.putExtra("mapInfo", mapInfoGson);
                    startActivity(mapIntent);
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, profile.getUser().getEmail());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Capstone - Request for session");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }

    private void returnToProfilesActivityWithError() {
        Intent profilesIntent = new Intent(getApplicationContext(), ProfilesActivity.class);
        profilesIntent.putExtra("profileError", "Profile was not found");
        startActivity(profilesIntent);
    }
}
