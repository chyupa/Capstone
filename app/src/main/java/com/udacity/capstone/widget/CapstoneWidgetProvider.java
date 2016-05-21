package com.udacity.capstone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.capstone.R;
import com.udacity.capstone.data.CapstoneContract;

/**
 * Created by Razvan on 21-May-16.
 */
public class CapstoneWidgetProvider extends AppWidgetProvider {

    private String name;
    private String bio;
    private String rate;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            Uri uri = CapstoneContract.ProfilesEntry.buildProfilesUri(100);

            String projection[] = {
                    CapstoneContract.ProfilesEntry._ID,
                    CapstoneContract.ProfilesEntry.COLUMN_NAME,
                    CapstoneContract.ProfilesEntry.COLUMN_BIO,
                    CapstoneContract.ProfilesEntry.COLUMN_RATE
            };

            Cursor cursor = context.getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    "RANDOM() LIMIT 1"
            );

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex(CapstoneContract.ProfilesEntry.COLUMN_NAME));
                bio = cursor.getString(cursor.getColumnIndex(CapstoneContract.ProfilesEntry.COLUMN_BIO));
                rate = cursor.getString(cursor.getColumnIndex(CapstoneContract.ProfilesEntry.COLUMN_RATE));
                cursor.close();
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.capstone_widget);

            remoteViews.setTextViewText(R.id.profile_item_name, name);
            remoteViews.setTextViewText(R.id.profile_item_bio, bio);
            remoteViews.setTextViewText(R.id.profile_item_rate, rate);

            Intent intent = new Intent(context, CapstoneWidgetProvider.class);
            intent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.refreshProfileBtn, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
