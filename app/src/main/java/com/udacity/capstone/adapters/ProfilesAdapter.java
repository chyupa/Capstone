package com.udacity.capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.capstone.R;
import com.udacity.capstone.models.Profile;

import java.util.ArrayList;

/**
 * Created by chyupa on 11-May-16.
 */
public class ProfilesAdapter extends ArrayAdapter<Profile> {

    public ProfilesAdapter(Context context, ArrayList<Profile> profiles) {
        super(context, 0, profiles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_list_item, parent, false);
        }

        Profile profile = getItem(position);

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.profile_item_image);
        TextView itemName = (TextView) convertView.findViewById(R.id.profile_item_name);
        TextView itemBio = (TextView) convertView.findViewById(R.id.profile_item_bio);

        Glide.with(getContext())
                .load(profile.getProfile_image())
                .fallback(R.drawable.ic_profile_image)
                .into(itemImage);

        itemName.setText(profile.getName());
        itemBio.setText(profile.getBio());

        return convertView;
    }

    @Override
    public Profile getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
