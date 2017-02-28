package com.papaya.scotthanberg.papaya;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class Professor extends User {

    private List<Class> classes;
    private String AccessCode;

    public Professor() {

    }

    public LatLng getLocation() {
        return Location;
    }

    public String getUserID() {
        return UserID;
    }

    @Override
    public String getName() {
        return Name;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public String getAccessCode() {
        return AccessCode;
    }
}
