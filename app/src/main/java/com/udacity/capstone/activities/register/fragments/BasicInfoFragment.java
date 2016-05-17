package com.udacity.capstone.activities.register.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.response.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chyupa on 05-May-16.
 */
public class BasicInfoFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();
    private View rootView = null;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.basic_info_fragment, container, false);

        Button createAccountBtn = (Button) rootView.findViewById(R.id.step1Btn);

        final EditText inputName = (EditText)rootView.findViewById(R.id.input_name);
        final EditText inputEmail = (EditText)rootView.findViewById(R.id.input_email);
        final EditText inputPassword = (EditText)rootView.findViewById(R.id.input_password);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("userId", 0);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean success = true;

                if (inputName.getText().toString().equals("")) {
                    success = false;
                    inputName.setError("Name is required");
                }

                if (inputEmail.getText().toString().equals("")) {
                    success = false;
                    inputEmail.setError("Email is required");
                } else if (!isEmailValid(inputEmail.getText().toString())) {
                    success = false;
                    inputEmail.setError("Email is invalid");
                }

                if (inputPassword.getText().toString().equals("")) {
                    success = false;
                    inputPassword.setError("Password is required");
                } else if (inputPassword.getText().toString().length() < 6) {
                    success = false;
                    inputPassword.setError("Password must be at least 6 characters");
                }

                if (success) {
                    final CapstoneWebService capstoneWebService = new CapstoneWebService();
                    Call<UserResponse> userResponseCall = capstoneWebService.service.createUser(
                            inputName.getText().toString(),
                            inputEmail.getText().toString(),
                            inputPassword.getText().toString()
                    );

                    userResponseCall.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            UserResponse userResponse;
                            userResponse = response.body();
                            if (userResponse.isSuccess()) {
                                sharedPreferences.edit()
                                        .putInt("userId", userResponse.getId())
                                        .apply();

                                Fragment profileImageFragment = new ProfileImageFragment();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.register, profileImageFragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                inputName.setError(userResponse.getNameError());
                                inputEmail.setError(userResponse.getEmailError());
                                inputPassword.setError(userResponse.getPasswordError());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Log.e(LOG_TAG, t.getMessage());
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity = getActivity();

    }

    public boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
