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
    private String startTime;
    private String location_desc;
    private String host_id;



    public StudySession(String duration, String location_long, String start_time, String session_id, String location_desc,
                        String description, String sponsored, String host_id, String location_lat) {
        this.duration = duration;
        this.location = new LatLng(Double.parseDouble(location_lat), Double.parseDouble(location_long));
        this.startTime = start_time;
        this.sessionID = session_id;
        this.location_desc = location_desc;
        this.description = description;
        this.sponsored = sponsored;
        this.host_id = host_id;
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
