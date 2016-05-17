package com.udacity.capstone.activities.profile;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

//import com.google.android.gms.maps.GoogleMap;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.OnMapReadyCallback;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
import com.androidmapsextensions.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
import com.androidmapsextensions.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.capstone.R;
import com.udacity.capstone.adapters.ProfilesAdapter;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.Profile;
import com.udacity.capstone.models.response.ProfilesResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilesActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ProfilesResponse profilesResponse;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnMapReadyCallback {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private final String LOG_TAG = getClass().getSimpleName();
        private boolean needMoreProfiles = false;
        private ArrayList<Profile> profileArrayList = new ArrayList<>();
        private int page = 1;
        private ProfilesAdapter profilesAdapter;
        private boolean executing = false;
        private boolean noMoreProfiles = false;
        private ProgressBar progressBar;

        private GoogleMap gMap;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            String profilesGson = new Gson().toJson(profileArrayList);
            outState.putString("profiles", profilesGson);

            outState.putInt("page", page);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_profiles, container, false);

                progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                profilesAdapter = new ProfilesAdapter(getContext(), profileArrayList);
                final ListView listView = (ListView) rootView.findViewById(R.id.profiles_list);
                if (savedInstanceState != null) {
                    String profilesGson = savedInstanceState.getString("profiles");
                    Type profilesType = new TypeToken<ArrayList<Profile>>() {}.getType();
                    profileArrayList = new Gson().fromJson(profilesGson, profilesType);
                    profilesAdapter.addAll(profileArrayList);

                    page = savedInstanceState.getInt("page");
                } else {
                    fetchMoreProfiles();
                }

                listView.setAdapter(profilesAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int selectedProfileId = i;
                        Profile selectedProfile = profileArrayList.get(selectedProfileId);
                        Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                        profileIntent.putExtra("profileId", selectedProfile.getId());
                        startActivity(profileIntent);
                    }
                });

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if ((firstVisibleItem + visibleItemCount) >= totalItemCount && !executing) {
                            needMoreProfiles = true;
                            fetchMoreProfiles();
                            page++;
                        }
                    }
                });
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.activity_maps, container, false);

//                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                        .findFragmentById(R.id.map);
//                mapFragment.getMapAsync(this);
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getExtendedMapAsync(this);

            }
            return rootView;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;

            CapstoneWebService capstoneWebService = new CapstoneWebService();
            Call<ProfilesResponse> basicResponseCall = capstoneWebService.service.getProfiles(1);
            basicResponseCall.enqueue(new Callback<ProfilesResponse>() {
                @Override
                public void onResponse(Call<ProfilesResponse> call, Response<ProfilesResponse> response) {
                    ProfilesResponse profilesResponse = response.body();
                    for (Profile profile : profilesResponse.getData()) {
                        LatLng profileLocation = new LatLng(
                                profile.getPostcodeInfo().getLat(),
                                profile.getPostcodeInfo().getLon()
                        );

                        Marker profileMarker = gMap.addMarker(new MarkerOptions()
                                .position(profileLocation)
                                .title(profile.getName())
                        );

                        profileMarker.setData(profile);
                    }
                }

                @Override
                public void onFailure(Call<ProfilesResponse> call, Throwable t) {

                }
            });

            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Profile profile = marker.getData();

                    Intent singleProfileIntent = new Intent(getContext(), ProfileActivity.class);
                    singleProfileIntent.putExtra("profileId", profile.getId());
                    startActivity(singleProfileIntent);
                }
            });
        }

        public void fetchMoreProfiles() {
            executing = true;
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            CapstoneWebService capstoneWebService = new CapstoneWebService();
            Call<ProfilesResponse> profilesResponseCall = capstoneWebService.service.getProfiles(page);
            profilesResponseCall.enqueue(new Callback<ProfilesResponse>() {
                @Override
                public void onResponse(Call<ProfilesResponse> call, Response<ProfilesResponse> response) {
                    ProfilesResponse profilesResponse = response.body();
                    ArrayList<Profile> tempProfileArrayList = new ArrayList<>();
                    if (profilesResponse.getNext_page_url() != null) {
                        for (Profile profile : profilesResponse.getData()) {
                            tempProfileArrayList.add(profile);
                        }
                        profilesAdapter.addAll(tempProfileArrayList);
                        profilesAdapter.notifyDataSetChanged();
                        profileArrayList.addAll(tempProfileArrayList);
                    } else {
                        noMoreProfiles = true;
                    }
                    executing = false;
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ProfilesResponse> call, Throwable t) {
                    Log.d("fuck this", t.getMessage());
                }
            });
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "List";
                case 1:
                    return "Map";
            }
            return null;
        }
    }
}
