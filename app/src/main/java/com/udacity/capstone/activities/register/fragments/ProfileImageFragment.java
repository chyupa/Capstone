package com.udacity.capstone.activities.register.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.api.CapstoneWebService;
import com.udacity.capstone.models.response.BasicResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by chyupa on 13-May-16.
 */
public class ProfileImageFragment extends Fragment {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final String LOG_TAG = getClass().getSimpleName();
    private View rootView;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;

    private ImageView previewProfileImage;
    private TextView profileImageError;
    private int userId;
    private byte[] imageByteArray;

    private Uri file;
    private File tempFile;
    private boolean imageCaptured = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        verifyStoragePermissions(getActivity());

        rootView = inflater.inflate(R.layout.profile_image_fragment, container, false);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("userId", 0);
        userId = sharedPreferences.getInt("userId", 0);

        previewProfileImage = (ImageView) rootView.findViewById(R.id.profileImagePreview);
        profileImageError = (TextView) rootView.findViewById(R.id.profileImageError);

        Button updateProfileImageBtn = (Button) rootView.findViewById(R.id.updateProfileImageButton);
        ImageButton selectImageBtn = (ImageButton) rootView.findViewById(R.id.profileImageBtn);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempFile = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.png");
                file = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        });

        updateProfileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = true;
                if (!imageCaptured) {
                    success = false;
                    profileImageError.setVisibility(View.VISIBLE);
                }

                if (success) {
                    RequestBody requestBody =
                            RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);

                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", tempFile.getName(), requestBody);

                    //do something here
                    CapstoneWebService capstoneWebService = new CapstoneWebService();
                    Call<BasicResponse> basicResponseCall = capstoneWebService.service.updateProfileImage(
                            userId,
                            body
                    );

                    basicResponseCall.enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            BasicResponse basicResponse = response.body();
                            if (basicResponse.isSuccess()) {
                                Fragment bioFragment = new BioFragment();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.register, bioFragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Log.d(LOG_TAG, "api went wrong");
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            Log.e(LOG_TAG, t.getMessage());
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageCaptured = true;
                previewProfileImage.setImageURI(file);
                previewProfileImage.setVisibility(View.VISIBLE);
            } else {
                Log.d(LOG_TAG, "data is null");
            }
        }
    }

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
