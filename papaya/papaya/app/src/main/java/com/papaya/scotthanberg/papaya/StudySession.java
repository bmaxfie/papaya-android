package com.papaya.scotthanberg.papaya;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import com.papaya.scotthanberg.papaya.Class;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scotthanberg on 2/27/17.
 */

public class StudySession implements Serializable {
    private List<User> users;
    //private LatLng location;
    private String duration;
    private String description;
    /*  List<Post> Posts; */
    private String sessionID;
    private String sponsored;
    private Class classObject;
    private String startTime;
    private String location_desc;
    private String host_id;
    private String location_long;
    private String location_lat;



    public StudySession(String duration, String location_long, String start_time, String session_id, String location_desc,
                        String description, String sponsored, String host_id, String location_lat) {
        this.duration = duration;
        //this.location = new LatLng(Double.parseDouble(location_lat), Double.parseDouble(location_long));
        this.location_long = location_long;
        this.location_lat = location_lat;
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
        //return location;
        return new LatLng(Double.parseDouble(location_lat), Double.parseDouble(location_long));
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

//
//    //parcelling part
//    public StudySession(Parcel in){
//        // the order needs to be the same as in writeToParcel() method
//        this.duration = in.readString();
//        this.location_lat = in.readString();
//        this.location_long = in.readString();
//        this.startTime = in.readString();
//        this.sessionID = in.readString();
//        this.location_desc = in.readString();
//        this.description = in.readString();
//        this.sponsored = in.readString();
//        this.host_id = in.readString();
//        this.classObject = in.readTypedObject()
//    }
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(this.duration);
//        parcel.writeString(this.location_lat);
//        parcel.writeString(this.location_long);
//        parcel.writeString(this.startTime);
//        parcel.writeString(this.sessionID);
//        parcel.writeString(this.location_desc);
//        parcel.writeString(this.description);
//        parcel.writeString(this.sponsored);
//        parcel.writeString(this.host_id);
//        parcel.writeParcelable(Class.class, 0);
//
//
//
//
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//        public StudySession createFromParcel(Parcel in) {
//            return new StudySession(in);
//        }
//
//        public StudySession[] newArray(int size) {
//            return new StudySession[size];
//        }
//    };
}
