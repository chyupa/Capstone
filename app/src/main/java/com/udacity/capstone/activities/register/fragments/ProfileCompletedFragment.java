package com.udacity.capstone.activities.register.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.capstone.activities.profile.LoggedInProfileActivity;
import com.udacity.capstone.R;

/**
 * Created by chyupa on 09-May-16.
 */
public class ProfileCompletedFragment extends Fragment {

    private View rootView;
    private Button goToProfileBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_completed_fragment, container, false);

        goToProfileBtn = (Button) rootView.findViewById(R.id.goToProfile_btn);
        goToProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(getContext(), LoggedInProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        return rootView;
    }
}
