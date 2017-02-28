package com.papaya.scotthanberg.papaya;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by scotthanberg on 2/27/17.
 */

public abstract class User {

    String UserID;
    String Name;
    LatLng Location;

    public abstract String getUserID();
    public abstract String getName();
    public abstract LatLng getLocation();
}
