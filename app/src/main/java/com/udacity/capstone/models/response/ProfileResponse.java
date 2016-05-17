package com.udacity.capstone.models.response;

import com.udacity.capstone.models.PostcodeInfo;
import com.udacity.capstone.models.User;

/**
 * Created by chyupa on 09-May-16.
 */
public class ProfileResponse {

    private int id;
    private int user_id;
    private String name;
    private String bio;
    private String skills;
    private PostcodeInfo postcodeInfo;
    private String created_at;
    private String updated_at;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public PostcodeInfo getPostcodeInfo() {
        return postcodeInfo;
    }

    public void setPostcodeInfo(PostcodeInfo postcodeInfo) {
        this.postcodeInfo = postcodeInfo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
