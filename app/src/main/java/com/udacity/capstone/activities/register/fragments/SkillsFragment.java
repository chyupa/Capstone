package com.udacity.capstone.activities.register.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.response.BasicResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chyupa on 05-May-16.
 */
public class SkillsFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.skills_fragment, container, false);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("userId", 0);

        Button updateSkillsButton = (Button)rootView.findViewById(R.id.updateSkillsButton);
        final EditText inputSkills = (EditText)rootView.findViewById(R.id.input_skills);

        updateSkillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = true;
                if (inputSkills.getText().toString().equals("")) {
                    inputSkills.setError("This field is required");
                    success = false;
                }

                if (success) {
                    final CapstoneWebService capstoneWebService = new CapstoneWebService();
                    Call<BasicResponse> basicResponseCall = capstoneWebService.service
                            .updateSkills(sharedPreferences.getInt("userId", 0), inputSkills.getText().toString());

                    basicResponseCall.enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            BasicResponse basicResponse = response.body();
                            if (basicResponse.isSuccess()) {
                                Fragment postcodeFragment = new PostcodeFragment();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.register, postcodeFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });

        return rootView;
    }
}
