package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateNewSession extends AppCompatActivity {

    //Main Menu Buttons
    private RelativeLayout dropDown;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;

    private Double myLatitude;
    private Double myLongitude;
    
    private EditText className, timeDuration;
    private ArrayList<StudySession> Sessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_session);

        Menu menu = new Menu(this);

        className = (EditText) findViewById(R.id.editText3);
        timeDuration = (EditText) findViewById(R.id.editText2);


        if (savedInstanceState != null) {
            AccountData.data.clear();
            AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA));
            //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA);
        }
        else if (getIntent().hasExtra(AccountData.ACCOUNT_DATA)) {
            AccountData.data.clear();
            AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA));
            //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA);
        }
        // Load data from AccountData into class variables:

        Log.d("AccountData", "username: " + AccountData.getUsername());

        Intent studySession = getIntent(); // gets the previously created intent
        myLatitude = studySession.getDoubleExtra("lat", 0);
        myLongitude = studySession.getDoubleExtra("lon", 0);
    }



    public void addStudySession(View view) {
        // Instantiate the RequestQueue.
        /* Replace Beta with /class/id/sessions or something like that
        *  https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/
        *  */
        String classId = "";

        for (int i = 0; i < AccountData.getClasses().size(); i++) {
            if (AccountData.getClasses().get(i).getClassName().equals(className.getText().toString())) {
                classId = AccountData.getClasses().get(i).getClassID();
            }
        }

        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classId + "/sessions";
        final JSONObject newJSONStudySession = new JSONObject();
        try {
            newJSONStudySession.put("user_id", AccountData.getUserID());
            newJSONStudySession.put("duration", Integer.parseInt(timeDuration.getText().toString()));
            newJSONStudySession.put("location_desc", "Location Description");
            newJSONStudySession.put("location_lat", myLatitude.floatValue());
            newJSONStudySession.put("location_long", myLongitude.floatValue());
            newJSONStudySession.put("description", "This will be a description");
            newJSONStudySession.put("service", AccountData.getService());
            newJSONStudySession.put("authentication_key", AccountData.getAuthKey());
            newJSONStudySession.put("sponsored", true);
            newJSONStudySession.put("service_user_id", AccountData.getAuthKey()); //todo: replace with correct service_user_id
        } catch (JSONException e) {
            System.out.println("LOL you got a JSONException");
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, newJSONStudySession, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("code") == 201) {
                                Toast.makeText(CreateNewSession.this, className.getText() + " created for " + timeDuration.getText() + " hour(s)", Toast.LENGTH_LONG).show();
                                Log.d("CREATE_NEW_SESSION", response.getString("code_description"));

                                // Confirm session_id and class_id in response probably.
                            }
                            else {
                                Toast.makeText(CreateNewSession.this, response.getString("code_description"), Toast.LENGTH_LONG).show();
                                Log.d("CREATE_NEW_SESSION", response.getString("code_description"));
                            }
                        } catch (JSONException jsone) {
                            Toast.makeText(CreateNewSession.this, "Malformed JSON Response ERROR.", Toast.LENGTH_LONG).show();
                            Log.d("CREATE_NEW_SESSION", "Malformed JSON Response ERROR.");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CREATE_NEW_SESSION", "onErrorResponse: " + error.getMessage());

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

        Intent homeScreen = new Intent(this, HomeScreen.class);
        homeScreen.putExtra("from", "CreateNewSession");
//        homeScreen.putExtra("sessions",HomeScreen.getSessions());
        homeScreen.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(homeScreen);
    }

    public void createSession(View view) {
        Intent sessionCreated = new Intent(this, HomeScreen.class);
        Toast toast = Toast.makeText(this, "Study Session Created", Toast.LENGTH_SHORT);
        toast.show();
        sessionCreated.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(sessionCreated);
    }

    /**
     * Android callback
     * Invoked when the activity may be temporarily destroyed, save the instance state here.
     * @param outState - supplised by Android OS
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);

        outState.putSerializable(AccountData.ACCOUNT_DATA, AccountData.data);
    }

    /**
     * Android callback
     * This callback is called only when there is a saved instance previously saved using
     * onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
     * other state here, possibly usable after onStart() has completed.
     * The savedInstanceState Bundle is same as the one used in onCreate().
     * @param savedInstanceState - supplied by Android OS
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        AccountData.data.clear();
        AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.get(AccountData.ACCOUNT_DATA));
        //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }

}
