package com.udacity.capstone.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chyupa on 19-May-16.
 */
public class CapstoneContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.capstone";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PROFILES = "profiles";
    public static final String PATH_POSTCODES = "postcodes";

    public static final class ProfilesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;

        public static final String TABLE_NAME = "profiles";

        public static final String COLUMN_USER_ID = "user_id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_BIO = "bio";

        public static final String COLUMN_PROFILE_IMAGE = "profile_image";

        public static final String COLUMN_SKILLS = "skills";

        public static final String COLUMN_RATE = "rate";

        public static final String COLUMN_POSTCODE = "postcode";

        public static Uri buildProfilesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PostcodesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTCODES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTCODES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTCODES;

        public static final String TABLE_NAME = "postcodes";

        public static final String COLUMN_PROFILE_ID = "profile_id";

        public static final String COLUMN_LAT = "lat";

        public static final String COLUMN_LON = "lon";

        public static Uri buildPostcodeUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
