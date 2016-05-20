package com.udacity.capstone.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.capstone.R;
import com.udacity.capstone.activities.profile.ProfilesActivity;

/**
 * Created by chyupa on 20-May-16.
 */
public class ProfilesCursorAdapter extends CursorAdapter {

    public ProfilesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {

        public final ImageView profileImageView;
        public final TextView profileNameView;
        public final TextView profileBioView;

        public ViewHolder(View view) {
            profileImageView = (ImageView) view.findViewById(R.id.profile_item_image);
            profileNameView = (TextView) view.findViewById(R.id.profile_item_name);
            profileBioView = (TextView) view.findViewById(R.id.profile_item_bio);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view =  LayoutInflater.from(context).inflate(R.layout.profile_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());

        String profileImage = cursor.getString(ProfilesActivity.PlaceholderFragment.COL_PROFILE_IMAGE);
        Glide.with(context)
                .load(profileImage)
                .fallback(R.drawable.ic_profile_image)
                .into(viewHolder.profileImageView);

        String profileName = cursor.getString(ProfilesActivity.PlaceholderFragment.COL_PROFILE_NAME);
        viewHolder.profileNameView.setText(profileName);

        String profileBio = cursor.getString(ProfilesActivity.PlaceholderFragment.COL_PROFILE_BIO);
        viewHolder.profileBioView.setText(profileBio);
    }
}
