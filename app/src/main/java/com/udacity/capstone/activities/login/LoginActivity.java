package com.udacity.capstone.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udacity.capstone.activities.profile.LoggedInProfileActivity;
import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    private TextView loginError;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplicationContext().getSharedPreferences("userId", 0);

        emailInput = (EditText) findViewById(R.id.input_email);
        passwordInput = (EditText) findViewById(R.id.input_password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginError = (TextView) findViewById(R.id.loginError);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = true;
                loginError.setText("");

                if (emailInput.getText().toString().isEmpty()) {
                    emailInput.setError("Email is required");
                    success = false;
                } else if (!isEmailValid(emailInput.getText().toString())) {
                    emailInput.setError("Invalid Email Address");
                    success = false;
                }

                if (passwordInput.getText().toString().isEmpty()) {
                    passwordInput.setError("Password is required");
                    success = false;
                }

                if (success) {
                    final CapstoneWebService capstoneWebService = new CapstoneWebService();
                    Call<LoginResponse> loginResponseCall = capstoneWebService.service.login(
                            emailInput.getText().toString(),
                            passwordInput.getText().toString());

                    loginResponseCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse loginResponse = response.body();
                            if (! loginResponse.isSuccess()) {
                                loginError.setText(loginResponse.getMsg());
                            } else {
                                String user = new Gson().toJson(loginResponse.getUser());
                                sharedPreferences.edit()
                                        .putString("user", user)
                                        .apply();

                                Intent intent = new Intent(getApplicationContext(), LoggedInProfileActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(LOG_TAG, "failure?!");
                            Log.e(LOG_TAG, t.getMessage());
                        }
                    });
                }
            }
        });
    }

    public boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
