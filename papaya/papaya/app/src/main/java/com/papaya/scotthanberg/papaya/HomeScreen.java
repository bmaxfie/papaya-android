package com.papaya.scotthanberg.papaya;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.android.volley.toolbox.RequestFuture;
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLongClickListener {

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
    private String currentStudySession;
    private Boolean leaveWarning = false;
    private CountDownTimer sponsoredSessionTimer;

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

        if (savedInstanceState != null) {
            AccountData.data.clear();
            AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA));
            //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA);
        } else if (getIntent().hasExtra(AccountData.ACCOUNT_DATA)) {
            AccountData.data.clear();
            AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA));
            //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA);
        }
        Log.d("AccountData", "username: " + AccountData.getUsername());

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

        /*newStudySession.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        sponsoredSessionTimer = new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                //Nothing to do
                            }

                            @Override
                            public void onFinish() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(HomeScreen.this).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setMessage("You are too far from the study session. Going any further will make you leave the session");
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });
                            }
                        }.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        sponsoredSessionTimer.cancel();
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });*/
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        AlertDialog alertDialog = new AlertDialog.Builder(HomeScreen.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Create sponsored study session at this location?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountData.setSponsored(true);
                        Intent StudySession = new Intent(mapView.getContext(), CreateNewSession.class);
                        StudySession.putExtra("lat", latLng.latitude);
                        StudySession.putExtra("lon", latLng.longitude);
                        StudySession.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
                        startActivity(StudySession);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void buttonAddStudySession(View view) {
        AccountData.setSponsored(false);
        Intent studySession = new Intent(this, CreateNewSession.class);
        studySession.putExtra("lat", myLatitude);
        studySession.putExtra("lon", myLongitude);
        //  studySession.putExtra("session",Sessions);
        studySession.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(studySession);
    }
/*    public void addStudySession(View view) {
        Intent studySession = new Intent(this, CreateNewSession.class);
        dropDown.setVisibility(View.GONE);
        newStudySession.setVisibility(View.GONE);
        startActivity(studySession);
    }
 */

    public void openMenu(View view) {
        if (dropDown.getVisibility() == View.VISIBLE) {
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
        ll.removeAllViews();
        Button all = new Button(this);
        all.setText("All");
        all.setTag("all_button");
        all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filtered.clear();
                for (StudySession s : Sessions) {
                    filtered.add(s);
                }
                updateMarkers(Sessions);
            }
        });
        ll.addView(all, lp);
        /* Below Conditional only there so it doesn't crash.  The issue will be fixed in the loading screen */
        if (AccountData.getClasses() == null) {
            AccountData.setClasses(new ArrayList<Class>());
        }
        for (int i = 0; i < AccountData.getClasses().size(); i++) {
            final ArrayList<Class> classes = AccountData.getClasses();
            final int index = i;
            Class currentClass = classes.get(i);
            Button myButton = new Button(this);
            myButton.setText(currentClass.getClassName());
            myButton.setTag(currentClass.getClassName());
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    updateMarkers(filterClass(classes.get(index)));
                }
            });
            ll.addView(myButton, lp);
        }
        //updateMarkers();
    }


    public ArrayList<StudySession> filterClass(Class a_class) {
        filtered.clear();
        for (StudySession s : Sessions) {
            if (s.getClassObject().getClassID().equals(a_class.getClassID())) {
                filtered.add(s);
            }
        }

        if (filtered.isEmpty()) {
            Toast toast = Toast.makeText(this.getApplicationContext(), "No study sessions for this class", Toast.LENGTH_SHORT);
            toast.show();
        }
        return filtered;
    }

    public void updateMarkers(ArrayList<StudySession> listOfSessions) {
        mMap.clear();
        for (StudySession s : listOfSessions) {
            if (s != null) {
                if (s.getLocation() != null) {
                    if (s.getSponsored()) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(s.getLocation())
                                .title(s.getSessionID())
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        );
                        marker.setTag(s);
                    } else if (s.isFriendsInSession() == false) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(s.getLocation())
                                        .title(s.getSessionID())
                                //.icon(BitmapDescriptorFactory
                                //        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        );
                        marker.setTag(s);
                    } else {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(s.getLocation())
                                .title(s.getSessionID())
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        );
                        marker.setTag(s);
                    }
                    //tag is used to store the session object inside each marker
                }
            }

        }

    }

    public void myCurrentStudySession() {
        Thread run1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String currentStudySession = null;
                String user_id = AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B");
                final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/currentsession/?user_id=" + user_id + "&service=" + AccountData.getService() + "&authentication_key=" + AccountData.getAuthKey() + "&service_user_id=" + AccountData.getAuthKey();
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JSONObject newJSONStudySession = new JSONObject();
                JsonObjectRequest jsObjRequestGET = new JsonObjectRequest
                        (Request.Method.GET, url, newJSONStudySession, future, future);
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequestGET);
                try {
                    JSONObject response = future.get(10, TimeUnit.SECONDS);   // This will block
                    currentStudySession = response.getString("current_session_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                } catch (InterruptedException e) {
                } catch (TimeoutException e) {
                    System.out.println(e.toString());
                }
                if (currentStudySession != null)
                    AccountData.setCurrentSession(currentStudySession);
                //  currentStudySession = AccountData.getCurrentSession().getSessionID();
                //  currentStudySession = "'Gc8QfLpJIJ6V1dsR9EEQ5w=='";

            }
        });
        run1.start();
        /*try {
            run1.join();
        } catch (InterruptedException ie) {
            Log.d("run1 Thread", "Join on run1 failed? ");
            ie.getStackTrace();
        }*/

        Thread run2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String currentStudySession = AccountData.getCurrentSession();
                if (currentStudySession == null || currentStudySession == "")
                    return;
                for (int i = 0; i < Sessions.size(); i++) {
                    if (Sessions.get(i).getSessionID().equals(currentStudySession)) {
                        StudySession current = Sessions.get(i);
                        Location locationA = new Location("point A");
                        Location locationB = new Location("point B");
                        locationA.setLatitude(myLatitude);
                        locationA.setLongitude(myLongitude);
                        locationB.setLatitude(current.getLocation().latitude);
                        locationB.setLongitude(current.getLocation().longitude);
                        float distance = locationA.distanceTo(locationB);
                        if (Sessions.get(i).getClassObject().getRole() == 1) {
                            if (distance < 15) {
                                leaveWarning = false;
                            } else if (distance >= 15 && distance <= 30) {
                                if (!leaveWarning) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            AlertDialog alertDialog = new AlertDialog.Builder(HomeScreen.this).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setMessage("You are too far from the study session. Going any further will make you leave the session");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                            leaveWarning = true;
                                        }
                                    });
                                }
                            } else if (distance >= 30 && currentStudySession != null) {
                                //String user_id = AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B");
                                final String url1 = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/currentsession";
                                //System.out.println(url1);
                                //final String url1 = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/currentsession/?";
                                RequestFuture<JSONObject> future1 = RequestFuture.newFuture();
                                JSONObject newJSONStudySession1 = new JSONObject();
                                try {
                                    newJSONStudySession1.put("user_id", AccountData.getUserID());
                                    newJSONStudySession1.put("service_user_id", AccountData.getAuthKey());
                                    newJSONStudySession1.put("service", AccountData.getService());
                                    newJSONStudySession1.put("authentication_key", AccountData.getAuthKey());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest jsObjRequestDEL = new JsonObjectRequest
                                        (Request.Method.PUT, url1, newJSONStudySession1, future1, future1);
                                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequestDEL);
                                try {
                                    JSONObject response = future1.get(10, TimeUnit.SECONDS);   // This will block
                                    //System.out.println(response.toString());
                                    //System.out.println("IT WORKS! BY GOD IT WORKS! EUREKA!");
                                /*} catch (JSONException e) {
                                    e.printStackTrace();*/
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                    VolleyError ve = (VolleyError) e.getCause();
                                    System.out.println("ve = " + ve.toString());
                                    System.out.println("ve.networkResponse = " + ve.networkResponse.toString());
                                    System.out.println("ve.networkResponse.status = " + ve.networkResponse.statusCode);
                                    System.out.println("ve.networkResponse.data = " + new String(ve.networkResponse.data));
                                } catch (InterruptedException e) {
                                } catch (TimeoutException e) {
                                    System.out.println(e.toString());
                                }
                                    /* runOnUiThread(new Runnable() {
                                         public void run() {
                                             Toast leaving = Toast.makeText(getApplicationContext(), "You have left the study session", Toast.LENGTH_SHORT);
                                             leaving.show();
                                         }
                                     });*/
                            }
                        }
                    }
                }
            }
        });
        run2.start();
        /*try {
            run2.join();
        } catch (InterruptedException ie) {
            Log.d("run2 Thread", "Join on run2 failed? ");
            ie.getStackTrace();
        }*/
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
                                    //System.out.println(response.toString());
                                    JSONArray arr = response.getJSONArray("users");
                                    for (int i = 0; i < arr.length(); i++) {
                                       // System.out.println(arr.getJSONObject(i).get("user_id").toString());
                                        //System.out.println(arr.getJSONObject(i).get("username").toString());
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
                                //if (true)
                                //                                System.out.println("Test");
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
                StudySession sess = (StudySession) marker.getTag();
                AccountData.setTappedSession((StudySession) marker.getTag());
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

        mMap.setOnMapLongClickListener(HomeScreen.this);
        mMap.setOnMapLoadedCallback(
                new GoogleMap.OnMapLoadedCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onMapLoaded() {
                        setUp();
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
        AccountData.setLocation(new LatLng(myLatitude, myLongitude));
        myCurrentStudySession();
        /*
        * Use the below for debugging if need be
        latitudeText.setText("Latitude :" + String.valueOf(myLatitude));
        longitudeText.setText("Longitude :" + String.valueOf(myLongitude));
        */
        // Change the camera to follow you!
        if (shouldMove) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLatitude, myLongitude)));
            shouldMove = false;
        }


    }

    public void checkIfUsersInStudySessionAreFriends(final String session_id) {
        // Get a list of friends (an array of userid)
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> friends = new ArrayList<String>();
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/" + "friends/" + "?authentication_key=" + AccountData.getAuthKey() + "&service_user_id=" + AccountData.getAuthKey() + "&user_id=" + AccountData.getUserID() + "&service=" + AccountData.getService();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, future, future);
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
                try {
                    JSONObject response = future.get(10, TimeUnit.SECONDS);
                    //System.out.println(response.toString());
                    JSONArray arr = response.getJSONArray("friends");
                    for (int i = 0; i < arr.length(); i++) {
                        friends.add(arr.getJSONObject(i).get("user_id").toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

                final Boolean[] thereAreFriends = {false};
                String classId = "";
                for (int i = 0; i < Sessions.size(); i++) {
                    if (Sessions.get(i).getSessionID().equals(session_id)) {
                        classId = Sessions.get(i).getClassObject().getClassID();
                    }
                }
                url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classId + "/sessions/" + session_id.replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "?authentication_key=" + AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&service_user_id=" + AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&user_id=" + AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&service=" + AccountData.getService().replaceAll("/", "%2F").replaceAll("\\+", "%2B");
                JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //                 System.out.println(response.toString());
                                    JSONArray arr = response.getJSONArray("users");
                                    for (int i = 0; i < arr.length(); i++) {
                                        if (friends.contains((arr.getJSONObject(i).getString("user_id")))) {
                                            thereAreFriends[0] = true;
                                            for (int j = 0; j < Sessions.size(); j++) {
                                                if (Sessions.get(j).getSessionID().equals(session_id)) {
                                                    Sessions.get(j).setFriendsInSession(true);
                                                }
                                            }
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
                                // if(true)
                                //    System.out.println("Test");
                            }
                        });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest1);

            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        markStudySessions = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        setUp();
                    }

                });
            }
        };
        oneMinute.schedule(markStudySessions, 0, 3000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setUp() {
        // first delete everything
        //mMap.clear();
        // then add sessions and classes from the database
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user?authentication_key="
                + AccountData.getAuthKey()
                + "&user_id=" + AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                + "&service=" + AccountData.getService().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                + "&service_user_id=" + AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B"); //todo: fix hard coding
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //update the arrayList Sessions and corresponding classes
                            Sessions.clear();
                            ArrayList<Class> classes = new ArrayList<Class>();
                            ArrayList<StudySession> temp = new ArrayList<StudySession>();
                            JSONArray arr = response.getJSONArray("classes");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject sessionsObject = arr.getJSONObject(i);
                                JSONArray sessionsArray = sessionsObject.getJSONArray("sessions");
                                for (int j = 0; j < sessionsArray.length(); j++) {
                                    //adds sessions to the temp arraylist
                                    temp.add(new StudySession(
                                                    sessionsArray.getJSONObject(j).get("duration").toString(),
                                                    sessionsArray.getJSONObject(j).get("location_long").toString(),
                                                    sessionsArray.getJSONObject(j).get("start_time").toString(),
                                                    sessionsArray.getJSONObject(j).get("session_id").toString(),
                                                    sessionsArray.getJSONObject(j).get("location_desc").toString(),
                                                    sessionsArray.getJSONObject(j).get("description").toString(),
                                                    sessionsArray.getJSONObject(j).get("sponsored").toString(),
                                                    sessionsArray.getJSONObject(j).get("host_id").toString(),
                                                    sessionsArray.getJSONObject(j).get("location_lat").toString()
                                            )
                                    );
                                } //end loop that traverses all sessions in a class

                                //populates the classes arrayList one at a time
                                Class tempClass = new Class(
                                        sessionsObject.getString("class_id"), sessionsObject.getString("classname"), sessionsObject.getString("descriptions"), temp, sessionsObject.getInt("user_role")
                                );
                                classes.add(tempClass);

                                //push temp onto Sessions variable
                                for (int a = temp.size() - 1; a >= 0; a--) {
                                    temp.get(a).setClassObject(tempClass);
                                    Sessions.add(temp.get(a)); //adds the study sesssion to the variable
                                    checkIfUsersInStudySessionAreFriends(temp.get(a).getSessionID()); // This will update the session as to whether or not there is a friend in it
                                    temp.remove(a); //clears temp for next set of sessions
                                }
                            } //end loop traversing all classes

                            //add classes arrayList to the AccountData
                            AccountData.setClasses(classes);
                            //add sessions to the arrayList
                            AccountData.setSessions(Sessions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        updateMarkers(Sessions);
        createClassButtons();

        /*Check for notifications*/

        url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes//sessions//invitations?"
                + "user_id=" + AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                + "&service_user_id=" + AccountData.getAuthKey()
                + "&service=" + AccountData.getService()
                + "&authentication_key=" + AccountData.getAuthKey();

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("posts");
                            if (arr.length() != 0) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject inviteObject = arr.getJSONObject(i);
                                    String className = inviteObject.get("classname").toString();
                                    String class_id = inviteObject.get("class_id").toString();
                                    String session_id = inviteObject.get("session_id").toString();
                                    String username = inviteObject.get("username").toString();
                                    NotificationClass.incrementCounter(); //increment counter for notification id so it's always different
                                    sendNotification(NotificationClass.getCounter(), "New Invitation for " + className, username + " invited you to a study session", session_id);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        Intent homeScreen = getIntent(); // gets the previously created intent
        String activity = homeScreen.getStringExtra("from");
        System.out.println("THIS ACTIVITY IS" + activity);
        if (activity.equals("CreateNewSession")) {
            //updateMarkers();
        }
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onResume();
        String activity = intent.getStringExtra("from");
        if (activity.equals("Notification")) {
            String notificationSession = intent.getStringExtra("session");
            boolean found = false;
            for (StudySession s : Sessions) {
                if (s.getSessionID().equals(notificationSession)) {
                    found = true;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(s.getLocation(), 18));
                }
            }
            if (!found) {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Session no longer exists", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
        setUp();
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


    public int dp_to_pixels(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static ArrayList<StudySession> getSessions() {
        return Sessions;
    }

    public void buttonMyFriends(View view) {
        Intent friendsList = new Intent(this, FriendsList.class);
        friendsList.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(friendsList);
    }

    public void buttonJoinClass(View view) {
        Intent joinClass = new Intent(this, JoinClass.class);
        joinClass.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(joinClass);
    }

    public void buttonSessionInfo() {
        Intent sessionInfo = new Intent(this, SessionInfo.class);
        sessionInfo.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(sessionInfo);
    }

    /**
     * Android callback
     * Invoked when the activity may be temporarily destroyed, save the instance state here.
     *
     * @param outState - supplised by Android OS
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(AccountData.ACCOUNT_DATA, AccountData.data);

        Log.d("HomeScreen", "onSaveInstanceState() called!");
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    /**
     * Android callback
     * This callback is called only when there is a saved instance previously saved using
     * onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
     * other state here, possibly usable after onStart() has completed.
     * The savedInstanceState Bundle is same as the one used in onCreate().
     *
     * @param savedInstanceState - supplied by Android OS
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("HomeScreen", "onRestoreInstanceState() called!");

        AccountData.data.clear();
        AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA));
        //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotification(int notificationId, String title, String content, String sessionId) {

        //Get an instance of NotificationManager//

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 14));
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this, 0, new Intent(this, HomeScreen.class), PendingIntent.FLAG_UPDATE_CURRENT
//        );

        Intent mainIntent = new Intent(this, HomeScreen.class);
        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("from", "Notification");
        mainIntent.putExtra("session", sessionId);
        mainIntent.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setTicker("New Invitation")
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        for(StudySession s : Sessions) {
            if (s.getSessionID().equals(sessionId)) {
                Notification notification = mBuilder.build();
                mNotificationManager.notify(notificationId, notification); //only add the notificaiton the first time called
                break;
            }
        }





    }
}
