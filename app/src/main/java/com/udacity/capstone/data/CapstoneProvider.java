package com.udacity.capstone.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by chyupa on 19-May-16.
 */
public class CapstoneProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CapstoneDbHelper mOpenHelper;

    static final int PROFILES = 100;
    static final int POSTCODES = 200;

    private static final SQLiteQueryBuilder sProfileQueryBuilder;

    static {
        sProfileQueryBuilder = new SQLiteQueryBuilder();

        sProfileQueryBuilder.setTables(
                CapstoneContract.ProfilesEntry.TABLE_NAME + " LEFT JOIN " +
                        CapstoneContract.PostcodesEntry.TABLE_NAME +
                        " ON " + CapstoneContract.ProfilesEntry.TABLE_NAME +
                        "." + CapstoneContract.ProfilesEntry._ID +
                        " = " + CapstoneContract.PostcodesEntry.TABLE_NAME +
                        "." + CapstoneContract.PostcodesEntry.COLUMN_PROFILE_ID
        );
    }

    private static final String sProfileSelection =
            CapstoneContract.ProfilesEntry.TABLE_NAME +
                    "." + CapstoneContract.ProfilesEntry._ID +
                    " = ? ";

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CapstoneContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CapstoneContract.PATH_PROFILES, PROFILES);
        matcher.addURI(authority, CapstoneContract.PATH_POSTCODES, POSTCODES);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CapstoneDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case PROFILES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CapstoneContract.ProfilesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case POSTCODES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CapstoneContract.PostcodesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PROFILES:
                return CapstoneContract.ProfilesEntry.CONTENT_ITEM_TYPE;
            case POSTCODES:
                return CapstoneContract.PostcodesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PROFILES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CapstoneContract.ProfilesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PROFILES: {
                long _id = db.insert(CapstoneContract.ProfilesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = CapstoneContract.ProfilesEntry.buildProfilesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POSTCODES: {
                long _id = db.insert(CapstoneContract.PostcodesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = CapstoneContract.PostcodesEntry.buildPostcodeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
