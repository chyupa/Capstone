package com.udacity.capstone.api;

import com.udacity.capstone.models.Profile;
import com.udacity.capstone.models.response.BasicResponse;
import com.udacity.capstone.models.response.LoginResponse;
import com.udacity.capstone.models.response.LogoutResponse;
import com.udacity.capstone.models.response.ProfileResponse;
import com.udacity.capstone.models.response.ProfilesResponse;
import com.udacity.capstone.models.response.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by chyupa on 05-May-16.
 */
public interface CapstoneApi {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/user")
    Call<UserResponse> createUser(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @Headers("Accept: application/json")
//    @FormUrlEncoded
    @Multipart
    @POST("/api/profile/image/{userId}")
    Call<BasicResponse> updateProfileImage(@Path("userId") int userId, @Part MultipartBody.Part file);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @Headers("Accept: application/json")
    @GET("/api/logout")
    Call<LogoutResponse> logout();

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/profile/bio/{userId}")
    Call<BasicResponse> updateBio(@Path("userId") int userId, @Field("bio") String bio);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/profile/rate/{userId}")
    Call<BasicResponse> updateRate(@Path("userId") int userId, @Field("rate") int rate);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/profile/skills/{userId}")
    Call<BasicResponse> updateSkills(@Path("userId") int userId, @Field("skills") String skills);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/profile/postcode/{userId}")
    Call<BasicResponse> updatePostcode(@Path("userId") int userId, @Field("postcode") String postcode);

    @Headers("Accept: application/json")
    @GET("/api/profiles/")
    Call<ProfilesResponse> getProfiles(@Query("page") int page);

    @Headers("Accept: application/json")
    @GET("/api/postcode/search/{postcode}")
    void getProfilesByPostcode(@Path("postcode") String postcode, @Query("page") int page);

    @Headers("Accept: application/json")
    @GET("/api/profile/{userId}")
    Call<Profile> getProfileById(@Path("userId") int userId);
}
