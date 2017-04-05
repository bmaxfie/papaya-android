package com.papaya.scotthanberg.papaya;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by scotthanberg on 2/27/17.
 */

public abstract class User implements Serializable {

    String UserID;
    String Name;
    LatLng Location;

    public abstract String getUserID();
    public abstract String getName();
    public abstract LatLng getLocation();
}
