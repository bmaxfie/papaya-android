package com.papaya.scotthanberg.papaya;

import com.google.android.gms.maps.model.LatLng;

import com.papaya.scotthanberg.papaya.Class;

import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class StudySession {
    private List<User> users;
    private LatLng location;
    private String duration;
    private String description;
    /*  List<Post> Posts; */
    private String sessionID;
    private String sponsored;
    private Class classObject;


    public StudySession(String sessionID, String duration, String location, String description, String sponsored) {
        this.location = new LatLng(Double.parseDouble(location.split(",")[0]), Double.parseDouble(location.split(",")[1]));
        this.sessionID = sessionID;
        this.duration = duration;
        this.description = description;
        this.sponsored = sponsored;
    }


    /*
        GETTERS:
     */
    public List<User> getUsers() {
        return users;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getSponsored() {
        return sponsored;
    }

    public Class getClassObject() { return classObject; }


    /*
        SETTERS:
     */
    public void setClassObject(Class c) {
        this.classObject = c;
    }

}
