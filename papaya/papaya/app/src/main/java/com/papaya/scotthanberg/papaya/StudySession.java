package com.papaya.scotthanberg.papaya;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class StudySession {
    private List<Student> Students;
    private LatLng Location;
    private Double duration;
    private String Description;
    /*  List<Post> Posts; */
    private String SessionID;
    private Boolean Sponsored;

    public StudySession() {

    }

    public StudySession(LatLng Location) {
        this.Location = Location;
    }

    public List<Student> getStudents() {
        return Students;
    }

    public LatLng getLocation() {
        return Location;
    }

    public Double getDuration() {
        return duration;
    }

    public String getDescription() {
        return Description;
    }

    public String getSessionID() {
        return SessionID;
    }

    public Boolean getSponsored() {
        return Sponsored;
    }
}
