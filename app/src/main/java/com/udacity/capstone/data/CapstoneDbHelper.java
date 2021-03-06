package com.udacity.capstone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by chyupa on 19-May-16.
 */
public class CapstoneDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "profiles.db";

    public CapstoneDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PROFILES_TABLE = "CREATE TABLE " + CapstoneContract.ProfilesEntry.TABLE_NAME + " (" +
                CapstoneContract.ProfilesEntry._ID + " INTEGER PRIMARY KEY," +
                CapstoneContract.ProfilesEntry.COLUMN_USER_ID + " INT NOT NULL, " +
                CapstoneContract.ProfilesEntry.COLUMN_NAME + " TEXT, " +
                CapstoneContract.ProfilesEntry.COLUMN_PROFILE_IMAGE + " TEXT, " +
                CapstoneContract.ProfilesEntry.COLUMN_BIO + " TEXT, " +
                CapstoneContract.ProfilesEntry.COLUMN_SKILLS + " TEXT, " +
                CapstoneContract.ProfilesEntry.COLUMN_RATE + " REAL, " +
                CapstoneContract.ProfilesEntry.COLUMN_POSTCODE + " TEXT" +
                " );";

        final String SQL_CREATE_POSTCODES_TABLE = "CREATE TABLE " + CapstoneContract.PostcodesEntry.TABLE_NAME + " (" +
                CapstoneContract.PostcodesEntry._ID + " INTEGER PRIMARY KEY," +
                CapstoneContract.PostcodesEntry.COLUMN_PROFILE_ID + " INTEGER," +
                CapstoneContract.PostcodesEntry.COLUMN_LAT + " REAL," +
                CapstoneContract.PostcodesEntry.COLUMN_LON + " REAL," +
                " FOREIGN KEY (" + CapstoneContract.PostcodesEntry.COLUMN_PROFILE_ID + ") REFERENCES " +
                CapstoneContract.ProfilesEntry.TABLE_NAME + "(" + CapstoneContract.ProfilesEntry._ID + ")"+
                ")";

        db.execSQL(SQL_CREATE_PROFILES_TABLE);
        db.execSQL(SQL_CREATE_POSTCODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CapstoneContract.ProfilesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CapstoneContract.PostcodesEntry.TABLE_NAME);
        onCreate(db);
    }
}
