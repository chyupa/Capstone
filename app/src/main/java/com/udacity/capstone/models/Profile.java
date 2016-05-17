package com.udacity.capstone.models;

/**
 * Created by chyupa on 05-May-16.
 */
public class Profile {

    private int id;
    private String name;
    private String profile_image;
    private String bio;
    private String skills;
    private String postcode;
    private PostcodeInfo postcodeInfo;
    private User user;
    private double rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public PostcodeInfo getPostcodeInfo() {
        return postcodeInfo;
    }

    public void setPostcodeInfo(PostcodeInfo postcodeInfo) {
        this.postcodeInfo = postcodeInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
