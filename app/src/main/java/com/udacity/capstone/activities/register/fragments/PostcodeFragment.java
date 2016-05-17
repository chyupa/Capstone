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

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chyupa on 05-May-16.
 */
public class PostcodeFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.postcode_fragment, container, false);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("userId", 0);

        Button updatePostcodeButton = (Button)rootView.findViewById(R.id.updatePostcodeButton);
        final EditText inputPostcode = (EditText)rootView.findViewById(R.id.input_postcode);

        updatePostcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = true;
                Pattern pattern = Pattern.compile("^[A-Z]{1,2}[0-9]{1,2} ?[0-9][A-Z]{2}(GIR 0AA)|((([ABCDEFGHIJKLMNOPRSTUWYZ][0-9][0-9]?)|(([ABCDEFGHIJKLMNOPRSTUWYZ][ABCDEFGHKLMNOPQRSTUVWXY][0-9][0-9]?)|(([ABCDEFGHIJKLMNOPRSTUWYZ][0-9][ABCDEFGHJKSTUW])|([ABCDEFGHIJKLMNOPRSTUWYZ][ABCDEFGHKLMNOPQRSTUVWXY][0-9][ABEHMNPRVWXY])))) [0-9][ABDEFGHJLNPQRSTUWXYZ]{2})");

                if (inputPostcode.getText().toString().equals("")) {
                    inputPostcode.setError("This field is required");
                    success = false;
                } else if (! pattern.matcher(inputPostcode.getText().toString().toUpperCase()).matches()) {
                    inputPostcode.setError("Postcode is invalid");
                    success = false;
                }

                if (success) {
                    final CapstoneWebService capstoneWebService = new CapstoneWebService();
                    Call<BasicResponse> basicResponseCall = capstoneWebService.service
                            .updatePostcode(sharedPreferences.getInt("userId", 0), inputPostcode.getText().toString());

                    basicResponseCall.enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            BasicResponse basicResponse = response.body();
                            if (basicResponse.isSuccess()) {
                                Fragment rateFragment = new RateFragment();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.register, rateFragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                inputPostcode.setError(basicResponse.getMsg());
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
