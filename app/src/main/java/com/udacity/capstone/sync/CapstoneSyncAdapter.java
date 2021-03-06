package com.udacity.capstone.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.data.CapstoneContract;
import com.udacity.capstone.models.Profile;
import com.udacity.capstone.models.response.ProfilesResponse;

import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chyupa on 19-May-16.
 */
public class CapstoneSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public final String LOG_TAG = getClass().getSimpleName();

    public CapstoneSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting Sync");

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int lastPageSynced = sharedPreferences.getInt(getContext().getString(R.string.page_sync_key), 1);

        CapstoneWebService webService = new CapstoneWebService();
        Call<ProfilesResponse> profilesResponseCall = webService.service.getProfiles(lastPageSynced);
        profilesResponseCall.enqueue(new Callback<ProfilesResponse>() {
            @Override
            public void onResponse(Call<ProfilesResponse> call, Response<ProfilesResponse> response) {
                ProfilesResponse profilesResponse = response.body();

                if (!profilesResponse.getData().isEmpty()) {
                    Vector<ContentValues> cvVector = new Vector<ContentValues>(10);
                    for (Profile profile : profilesResponse.getData()) {
                        ContentValues profileValues = new ContentValues();
                        profileValues.put(CapstoneContract.ProfilesEntry._ID, profile.getId());
                        profileValues.put(CapstoneContract.ProfilesEntry.COLUMN_USER_ID, profile.getUser_id());
                        profileValues.put(CapstoneContract.ProfilesEntry.COLUMN_BIO, profile.getBio());
                        profileValues.put(CapstoneContract.ProfilesEntry.COLUMN_PROFILE_IMAGE, profile.getProfile_image());
                        profileValues.put(CapstoneContract.ProfilesEntry.COLUMN_SKILLS, profile.getSkills());
                        profileValues.put(CapstoneContract.ProfilesEntry.COLUMN_RATE, profile.getRate());
                        profileValues.put(CapstoneContract.ProfilesEntry.COLUMN_POSTCODE, profile.getPostcode());

                        cvVector.add(profileValues);
                    }

                    int inserted = 0;
                    if (cvVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cvVector.size()];
                        cvVector.toArray(cvArray);
                        getContext().getContentResolver().bulkInsert(CapstoneContract.ProfilesEntry.CONTENT_URI, cvArray);
                    }
                    Log.d(LOG_TAG, "Sync finished" + cvVector.size() + " Inserted");
                } else {
                    sharedPreferences.edit()
                            .putInt(getContext().getString(R.string.page_sync_key), 1)
                            .apply();
                }
            }

            @Override
            public void onFailure(Call<ProfilesResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        //CapstoneSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        //ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
