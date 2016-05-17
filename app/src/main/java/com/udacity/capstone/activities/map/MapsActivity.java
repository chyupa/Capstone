package com.udacity.capstone.activities.map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.udacity.capstone.R;
import com.udacity.capstone.models.MapInfo;
import com.udacity.capstone.models.User;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String LOG_TAG = getClass().getSimpleName();

    private GoogleMap mMap;
    private BitmapDescriptor tltPin;

    private String userGson;
    private MapInfo profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userGson = getIntent().getStringExtra("mapInfo");
        profile = new Gson().fromJson(userGson, MapInfo.class);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng profileLocation = new LatLng(profile.getLat(), profile.getLon());
        Marker profileMarker = mMap.addMarker(new MarkerOptions()
                .position(profileLocation)
                .title(profile.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );

        profileMarker.showInfoWindow();

        if (profile.getBio().length() > 50) {
            profileMarker.setSnippet(profile.getBio().substring(0, 50));
        } else {
            profileMarker.setSnippet(profile.getBio());
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLng(profileLocation));

    }

}
