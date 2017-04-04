package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
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
    private boolean shouldMove; //whether or not the map will snap back to the location of the user on location update
    private static ArrayList<StudySession> Sessions;
    private static ArrayList<StudySession> filtered; //arrayList holding only the sessions that are in the specified class
    private Timer oneMinute;
    private TimerTask markStudySessions;
    User currentUser;
    View mapView;

    //Main Menu Buttons
    private ImageView menubutton;
    private TextView textView;
    private RelativeLayout dropDown;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        shouldMove = true;
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
        filtered = new ArrayList<StudySession>();
        oneMinute = new Timer();

        dropDown = (RelativeLayout) findViewById(R.id.dropDown);
        horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        backdrop = (View) findViewById(R.id.horizontalBackdrop);
        newStudySession = (Button) findViewById(R.id.NewStudySession);
        sortByClass = (Button) findViewById(R.id.SortByClass);
        manageClasses = (Button) findViewById(R.id.ManageClasses);
        findFriends = (Button) findViewById(R.id.FindFriends);
        joinNewClass = (Button) findViewById(R.id.JoinNewClass);

        createClassButtons();
        //set filtered
        for (StudySession s : Sessions) {
            filtered.add(s);
        }


    }

/*    public void addStudySession(View view) {
        Intent studySession = new Intent(this, CreateNewSession.class);
        dropDown.setVisibility(View.GONE);
        newStudySession.setVisibility(View.GONE);
        startActivity(studySession);
    }
 */

    public void openMenu(View view) {
        if (dropDown.getVisibility()==View.VISIBLE) {
            dropDown.setVisibility(View.GONE);
            horizontalScroll.setVisibility(View.VISIBLE);
            backdrop.setVisibility(View.VISIBLE);
            newStudySession.setVisibility(View.GONE);
            sortByClass.setVisibility(View.GONE);
            manageClasses.setVisibility(View.GONE);
            findFriends.setVisibility(View.GONE);
            joinNewClass.setVisibility(View.GONE);
        } else {
            dropDown.setVisibility(View.VISIBLE);
            backdrop.setVisibility(View.GONE);
            horizontalScroll.setVisibility(View.GONE);
            newStudySession.setVisibility(View.VISIBLE);
            sortByClass.setVisibility(View.VISIBLE);
            manageClasses.setVisibility(View.VISIBLE);
            findFriends.setVisibility(View.VISIBLE);
            joinNewClass.setVisibility(View.VISIBLE);
        }
    }

    public void createClassButtons() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.scrollContainer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button all = new Button(this);
        all.setText("All");
        all.setTag("all_button");
        all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filtered.clear();
                for (StudySession s: Sessions){
                    filtered.add(s);
                }
                updateMarkers();
            }
        });
        ll.addView(all, lp);
        for (int i = 0; i < 4; i++) {
            Button myButton = new Button(this);
            //TODO:change this to getString method accessing Lambda sending it index: i
            myButton.setText("Class" + i);
            myButton.setTag("Class" + i);
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Object x = v.getTag();
                    filterClass(x);
                }
            });
            ll.addView(myButton, lp);
        }
        updateMarkers();
    }

    public void filterClass(Object x) {
        //TODO: lambda stuff goes here
        filtered.clear();
        mMap.clear();
        for (StudySession s: Sessions){
            if(s.getDescription().equals(x)){
                filtered.add(s);
            }
        }

        if (filtered.isEmpty()) {
            for (StudySession s: Sessions){
                filtered.add(s);
            }
            Toast toast = Toast.makeText(this.getApplicationContext() ,"No study sessions for this class", Toast.LENGTH_SHORT);
            toast.show();
        }
        updateMarkers();
    }

    public void updateMarkers() {
        getUsersInStudySession(new StudySession("91wBXfOeGxf8kOsZkG3nug==", "test", "1.2,1.3", "test", "test"));

        for (StudySession s : filtered) {
            if (s != null)
                getUsersInStudySession(s);
                if (s.getLocation() != null) {
                    mMap.addMarker(new MarkerOptions()
                            .position(s.getLocation()));
                }
        }
    }

    public void myCurrentStudySession() {
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/currentsession";
    }

    public void getUsersInStudySession(StudySession session) {
        //String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + "111" + "/sessions/" + session.getSessionID() + "?authentication_key=" + GPlusFragment.getAuthentication_key() + "&user_id=" + GPlusFragment.getPersonId() + "&service=" + GPlusFragment.getService();
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + "111/sessions/91wBXfOeGxf8kOsZkG3nug==";/*?user_id=bNvqxLf+m6VbMx1x8OCQrw==&service=GOOGLE&authentication_key=1234512345123451234512345123451234512345";*/

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    System.out.println(response.toString());
                                    JSONArray arr = response.getJSONArray("users");
                                    for (int i = 0; i < arr.length(); i++) {
                                        System.out.println(arr.getJSONObject(i).get("user_id").toString());
                                        System.out.println(arr.getJSONObject(i).get("username").toString());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                             if (true)
                                 System.out.println("Test");
                             }
                        });
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
        mMap.setPadding(0, dp_to_pixels(112), 0, 0); //padding left:0, top:112dp, right: 0, bottom:0
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
            layoutParams.setMargins(0, 0, dp_to_pixels(17), dp_to_pixels(63));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                buttonSessionInfo();

                /*
                //OLD DIALOG WAY OF JOINING A SESSION
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog
                SessionMarkerDialog smd = new SessionMarkerDialog();
                smd.setSessionId(marker.getTitle());
                smd.show(ft, "dialog");
                */
                return true;
            }
        });
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
        if(shouldMove) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLatitude, myLongitude)));
            shouldMove = false;
        }


    }

    public boolean checkIfUsersInStudySessionAreFriends(String session_id) {
        // Get a list of friends (an array of userid)
        final ArrayList<String> friends = new ArrayList<String>();
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/" + "/friends/" + "?authentication_key=" + GPlusFragment.getAuthentication_key() + "&user_id=" + GPlusFragment.getPersonId() + "&service=" + GPlusFragment.getService();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());
                            JSONArray arr = response.getJSONArray("friends");
                            for (int i = 0; i < arr.length(); i++) {
                                friends.add(arr.getJSONObject(i).get("user_id").toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(true)
                            System.out.println("Test");
                    }
                });

        final Boolean[] thereAreFriends = {false};
        url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + "111" + "/sessions/" + session_id + "?authentication_key=" + GPlusFragment.getAuthentication_key() + "&user_id=" + GPlusFragment.getPersonId() + "&service=" + GPlusFragment.getService();
        JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());
                            JSONArray arr = response.getJSONArray("users");
                            for (int i = 0; i < arr.length(); i++) {
                                if (friends.contains((arr.getJSONObject(i).getString("user_id")))) {
                                    thereAreFriends[0] = true;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        if(true)
                            System.out.println("Test");
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest1);
        return thereAreFriends[0];
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
                        // first delete everything
                        mMap.clear();
                        // then add everything from the database
                        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + "111" + "/sessions?authentication_key=" + GPlusFragment.getAuthentication_key() + "&user_id=" + GPlusFragment.getPersonId() + "&service=" + GPlusFragment.getService();
                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            //System.out.println(response.toString());
                                            JSONArray arr = response.getJSONArray("sessions");
                                            for (int i = 0; i < arr.length(); i++) {
                                                Sessions.add(new StudySession(
                                                        arr.getJSONObject(i).get("session_id").toString(),
                                                        arr.getJSONObject(i).get("duration").toString(),
                                                        arr.getJSONObject(i).get("location_lat").toString() + "," + arr.getJSONObject(i).get("location_long").toString(),
                                                        arr.getJSONObject(i).get("description").toString(),
                                                        ""
                                                        //arr.getJSONObject(i).get("sponsored").toString()
                                                )
                                                );
                                            }

                                            /*
                                            System.out.println(response.toString());
                                            JSONArray arr = response.getJSONArray("sessions");
                                            */

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
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

                        /* TODO callToFilterClass(Sessions, class)
                         * alters Sessions to contain the correct values
                         * move all this to another method
                         *
                         */

                        for (StudySession s : Sessions) {
                            if (s != null) {
                                if (s.getLocation() != null) {
                                    if (checkIfUsersInStudySessionAreFriends(s.getSessionID()) == false){
                                        mMap.addMarker(new MarkerOptions()
                                                .position(s.getLocation())
                                                .title(s.getSessionID())
                                        );
                                    } else {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(s.getLocation())
                                                .title(s.getSessionID())
                                                .icon(BitmapDescriptorFactory
                                                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                        );
                                    }
                                }
                            }

                        }

                    }
                });
            }
        };
        oneMinute.schedule(markStudySessions, 0, 6000);
    }

    @Override
    protected void onResume() {
        Intent homeScreen = getIntent(); // gets the previously created intent
        String activity = homeScreen.getStringExtra("from");
        System.out.println("THIS ACTIVITY IS" + activity);
        if(activity.equals("CreateNewSession")){
            //updateMarkers();
        }
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

    public void buttonAddStudySession(View view){
        Intent studySession = new Intent(this, CreateNewSession.class);
        studySession.putExtra("lat",myLatitude);
        studySession.putExtra("lon",myLongitude);
      //  studySession.putExtra("session",Sessions);
        startActivity(studySession);
    }

    public int dp_to_pixels(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static ArrayList<StudySession> getSessions() {
        return Sessions;
    }

    public static void addToSessions(StudySession studySession) {
        Sessions.add(studySession);
    }

    public void buttonJoinClass(View view) {
        Intent joinClass = new Intent(this, JoinClass.class);
        startActivity(joinClass);
    }

    public void buttonMyFriends(View view) {
        Intent friendsList = new Intent(this, FriendsList.class);
        startActivity(friendsList);
    }
    public void buttonSessionInfo() {
        Intent sessionInfo = new Intent(this, SessionInfo.class);
        startActivity(sessionInfo);
    }
}
