package com.papaya.scotthanberg.papaya;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private Double myLatitude;
    private Double myLongitude;
    /*
    private TextView latitudeText;
    private TextView longitudeText;
    */
    private ArrayList<StudySession> Sessions;
    private Timer oneMinute;
    private TimerTask markStudySessions;
    String url = "google.com";
    User currentUser;
    View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000); // This pulls once every minute
        locationRequest.setFastestInterval(15 * 1000); // This is the fastest interval
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        Sessions = new ArrayList<StudySession>();
        oneMinute = new Timer();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setPadding(0, 225, 0, 0); //padding left:0, top:200px, right: 0, bottom:0
        enableMyLocation();

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            int padding_in_dp = 17; //dps
            final float scale = getResources().getDisplayMetrics().density;
            int bottom = (int) (padding_in_dp * scale + 0.5f);
            padding_in_dp = 63; //dps
            int right = (int) (padding_in_dp * scale + 0.5f);
            layoutParams.setMargins(0, 0, bottom, right);
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        /*
        * Use the below for debugging if need be
        latitudeText.setText("Latitude :" + String.valueOf(myLatitude));
        longitudeText.setText("Longitude :" + String.valueOf(myLongitude));
        */
        // Change the camera to follow you!
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLatitude, myLongitude)));


    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        markStudySessions = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        for (StudySession s : Sessions) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(s.getLocation()));
                        }
                    }
                });
            }
        };
        oneMinute.schedule(markStudySessions, 0, 6000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    /**
     * TODO: ADD BUTTON METHODS HERE
     */
    public void addStudySession(View view) {
        //this is where we will call
        System.out.println("hi");

        // Instantiate the RequestQueue.
        /* Replace Beta with /class/id/sessions or something like that
        *  https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/
        *  */

        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/class/45digitid/sessions";
        final JSONObject newJSONStudySession = new JSONObject();
        try {
            newJSONStudySession.put("UserID_Host", "thiswillbeauserid");
            newJSONStudySession.put("Duration", 0.0);
            newJSONStudySession.put("Location", myLatitude.toString() + "," + myLongitude.toString());
            newJSONStudySession.put("Description", "This will be a description");
            newJSONStudySession.put("Sponsored", true);
        } catch (JSONException e) {
            System.out.println("LOL you got a JSONException");
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, newJSONStudySession, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Sessions.add(new StudySession(newJSONStudySession.get("UserID_Host").toString(), newJSONStudySession.get("Duration").toString()
                            , newJSONStudySession.get("Location").toString(), newJSONStudySession.get("Description").toString(), newJSONStudySession.get("Sponsored").toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        /*
        StudySession Triangle = new StudySession( new LatLng(40.425611, -86.916916));
        StudySession Beering = new StudySession( new LatLng(40.425885, -86.915894));
        StudySession Honors = new StudySession( new LatLng(40.427173, -86.919783));

        Sessions.add(Triangle);
        Sessions.add(Beering);
        Sessions.add(Honors);
        */
    }
}
