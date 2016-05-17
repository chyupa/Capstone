package com.udacity.capstone.models;

/**
 * Created by chyupa on 11-May-16.
 */
public class MapInfo {

    private String name;
    private String bio;
    private double lat;
    private double lon;

    public MapInfo(String profileName, String profileBio, double profileLat, double profileLon) {
        name = profileName;
        bio = profileBio;
        lat = profileLat;
        lon = profileLon;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
