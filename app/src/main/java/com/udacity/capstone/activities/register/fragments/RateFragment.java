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
import android.widget.NumberPicker;

import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.response.BasicResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chyupa on 16-May-16.
 */
public class RateFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rate_fragment, container, false);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("userId", 0);

        Button updateRateButton = (Button)rootView.findViewById(R.id.updateRateButton);
        final NumberPicker inputRate = (NumberPicker)rootView.findViewById(R.id.input_rate);
        inputRate.setMinValue(0);
        inputRate.setMaxValue(500);

        updateRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = true;
                if (inputRate.getValue() == 0) {
                    success = false;
                }

                if (success) {
                    final CapstoneWebService capstoneWebService = new CapstoneWebService();
                    Call<BasicResponse> basicResponseCall = capstoneWebService.service
                            .updateRate(sharedPreferences.getInt("userId", 0), inputRate.getValue());

                    basicResponseCall.enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            BasicResponse basicResponse = response.body();
                            if (basicResponse.isSuccess()) {
                                Fragment profileCompletedFragment = new ProfileCompletedFragment();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.register, profileCompletedFragment)
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
