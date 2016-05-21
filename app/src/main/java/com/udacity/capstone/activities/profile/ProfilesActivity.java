package com.udacity.capstone.activities.profile;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.Loader;
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

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.capstone.R;
import com.udacity.capstone.adapters.ProfilesAdapter;
import com.udacity.capstone.adapters.ProfilesCursorAdapter;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.data.CapstoneContract;
import com.udacity.capstone.models.Profile;
import com.udacity.capstone.models.response.ProfilesResponse;
import com.udacity.capstone.sync.CapstoneSyncAdapter;

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

//        CapstoneSyncAdapter.syncImmediately(getApplication());

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
    public static class PlaceholderFragment extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static final String PROFILES = "profiles";
        public static final String PAGE = "page";
        public static final String PROFILE_ID = "profileId";

        private static final int PROFILES_LOADER = 0;

        private static final String[] PROFILES_COLUMNS = {
                CapstoneContract.ProfilesEntry.TABLE_NAME + "." + CapstoneContract.ProfilesEntry._ID,
                CapstoneContract.ProfilesEntry.COLUMN_BIO,
                CapstoneContract.ProfilesEntry.COLUMN_NAME,
//                CapstoneContract.ProfilesEntry.COLUMN_SKILLS,
                CapstoneContract.ProfilesEntry.COLUMN_PROFILE_IMAGE,
//                CapstoneContract.ProfilesEntry.COLUMN_POSTCODE,
//                CapstoneContract.ProfilesEntry.COLUMN_RATE,
//                CapstoneContract.PostcodesEntry.COLUMN_LAT,
//                CapstoneContract.PostcodesEntry.COLUMN_LON
        };

        public static final int COL_PROFILE_ID = 0;
        public static final int COL_PROFILE_BIO = 1;
        public static final int COL_PROFILE_NAME = 2;
        public static final int COL_PROFILE_IMAGE = 3;

        private View rootView;

        private final String LOG_TAG = getClass().getSimpleName();
        private boolean needMoreProfiles = false;
        private ArrayList<Profile> profileArrayList = new ArrayList<>();
        private int page = 1;
        private ProfilesAdapter profilesAdapter;
        private ProfilesCursorAdapter profilesCursorAdapter;
        private boolean executing = true;
        private boolean noMoreProfiles = false;
        private ProgressBar progressBar;
        private int mPosition = ListView.INVALID_POSITION;
        private static final String SELECTED_KEY = "selected_position";
        private ListView listView = null;

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

            if (mPosition != ListView.INVALID_POSITION) {
                outState.putInt(SELECTED_KEY, mPosition);
            }
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(PROFILES_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_profiles, container, false);

                progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                profilesAdapter = new ProfilesAdapter(getContext(), profileArrayList);
                listView = (ListView) rootView.findViewById(R.id.profiles_list);

                profilesCursorAdapter = new ProfilesCursorAdapter(getActivity(), null, 0);
                listView.setAdapter(profilesCursorAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        if (cursor != null) {
                            Intent profileSingleIntent = new Intent(getContext(), ProfileActivity.class);
                            profileSingleIntent.putExtra("profileId", Integer.valueOf(cursor.getString(COL_PROFILE_ID)));
                            startActivity(profileSingleIntent);
                        }
                        mPosition = position;

                    }
                });

                if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                    // The listview probably hasn't even been populated yet.  Actually perform the
                    // swapout in onLoadFinished.
                    mPosition = savedInstanceState.getInt(SELECTED_KEY);
                }

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        Log.d(LOG_TAG, "first visible item " + firstVisibleItem);
                        Log.d(LOG_TAG, "visible item count " + visibleItemCount);
                        Log.d(LOG_TAG, "total item count " + totalItemCount);
                        if (firstVisibleItem + visibleItemCount == totalItemCount) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            int lastPageSynced = sharedPreferences.getInt(getString(R.string.page_sync_key), 1);

                            sharedPreferences.edit()
                                    .putInt(getString(R.string.page_sync_key), lastPageSynced + 1)
                                    .commit();

                            CapstoneSyncAdapter.syncImmediately(getContext());
                        }
                    }
                });

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.activity_maps, container, false);

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
                        if (!(profile.getPostcodeInfo() == null)) {
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
                    singleProfileIntent.putExtra(PROFILE_ID, profile.getId());
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
                    Log.d(LOG_TAG, t.getMessage());
                }
            });
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri profilesUri = CapstoneContract.ProfilesEntry.buildProfilesUri(100);
//
            return new CursorLoader(getActivity(),
                    profilesUri,
                    PROFILES_COLUMNS,
                    null,
                    null,
                    null
                    );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.d(LOG_TAG, "query finished");
            if (profilesCursorAdapter != null) {
                profilesCursorAdapter.swapCursor(data);
            }
            if (progressBar != null) {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.GONE);
            }
            if (mPosition != ListView.INVALID_POSITION) {
                // If we don't need to restart the loader, and there's a desired position to restore
                // to, do so now.
                listView.smoothScrollToPosition(mPosition);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            profilesCursorAdapter.swapCursor(null);
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
