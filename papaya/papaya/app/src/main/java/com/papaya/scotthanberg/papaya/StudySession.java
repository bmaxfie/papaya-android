package com.papaya.scotthanberg.papaya;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class StudySession {
    private List<Student> Students;
    private LatLng Location;
    private String duration;
    private String Description;
    /*  List<Post> Posts; */
    private String SessionID;
    private String Sponsored;

    public StudySession() {

    }

    public StudySession(String SessionID, String duration, String Location, String Description, String Sponsored) {
        this.Location = new LatLng(Double.parseDouble(Location.split(",")[0]), Double.parseDouble(Location.split(",")[1]));
        this.SessionID = SessionID;
        this.duration = duration;
        this.Description = Description;
        this.Sponsored = Sponsored;
    }

    public List<Student> getStudents() {
        return Students;
    }

    public LatLng getLocation() {
        return Location;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return Description;
    }

    public String getSessionID() {
        return SessionID;
    }

    public String getSponsored() {
        return Sponsored;
    }
}
