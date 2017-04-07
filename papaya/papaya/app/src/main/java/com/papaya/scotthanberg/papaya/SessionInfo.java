package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionInfo extends AppCompatActivity {

    String locationDesription;
    String description;
    //doesn't have to be a student, just need to hold both id and username
    ArrayList<Student> people = new ArrayList<Student>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        //sets up the menu and all its buttons
        Menu menu = new Menu(SessionInfo.this);

        Intent studySession = getIntent(); // gets the previously created intent

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

        //todo: call /classes/{class-id}/sessions/{session-id} GET to get the information about the class
        getInfo();
    }


    private void getInfo() {
        //since classid and sessionid are in the url, they can't have '/' in them
        String classid = AccountData.getTappedSession().getClassObject().getClassID();
        String sessionid = AccountData.getTappedSession().getSessionID();
        classid = classid.replaceAll("/", "%2F"); //
        sessionid = sessionid.replaceAll("/", "%2F");

        String url="https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" +
                classid + "/" +
                "sessions/" + sessionid +
                "?user_id=" + AccountData.getUserID() +
                "&service=" + AccountData.getService() +
                "&authentication_key=" + AccountData.getAuthKey() +
                "&service_user_id=" + AccountData.getAuthKey();

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray arr = response.getJSONArray("users");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObj2 = arr.getJSONObject(i);
                                String id = jsonObj2.getString("user_id");
                                String name = jsonObj2.getString("username");
                                people.add(new Student(id, name));
                            }
                            locationDesription = response.getString("location_desc");
                            description = response.getString("description");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //testing: listOfFriends.add("hello world");
                        createView(); //updates the view with the new list of friends

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("error:");
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public void createView() {
        /*
        Populates the Location description and the Session Description
         */
        TextView locDesc = (TextView) findViewById(R.id.location_desc);
        locDesc.setText(locationDesription);

        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(this.description);

        /*
        Puts the list of people and buttons on the View
         */

        LinearLayout rl = (LinearLayout) findViewById(R.id.peopleContainer);
        rl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp;
        //lp.setMargins(0,0,0,5);

        int counter = 0;
        for (Student s : people) {
            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout sideways = new LinearLayout(this);
            sideways.setOrientation(LinearLayout.HORIZONTAL);

            //put person name to the left
            TextView myText = new TextView(this);
            myText.setText(s.getName());
            myText.setTextSize(20);
            //myText.setPadding(0, 0, 0, 15);
            //myText.setBackgroundColor(Color.WHITE);
            myText.setId(counter);
            sideways.addView(myText, lp);

            //todo: if they are already a friend, do not add button
            //put button next to name
            Button button = new Button(this);
            button.setText("Add Friend");
            sideways.addView(button);

            //set lp

            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rl.addView(sideways, lp);

            //add button listener
            button.setTag(s);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //todo: call function to add them as a friend
                    //use the userId that is held in the s.getUserID()
                    Button b = (Button) v;
                    Student student = (Student) b.getTag();
                    String studentid = student.getUserID();

                    JSONObject info = new JSONObject();
                    try {
                        info.put("user_id", AccountData.getUserID());
                        info.put("user_id2", studentid);
                        info.put("authentication_key",AccountData.getAuthKey());
                        info.put("service", AccountData.getService());
                        info.put("service_user_id", AccountData.getAuthKey());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/friends";

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, info, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        int code = response.getInt("code");
                                        System.out.println(response);
                                        if (code == 201) {
                                            Toast toast = Toast.makeText(SessionInfo.this, "You are now friends", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
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
                    

                }
            });

            counter++;


        }
    }

    public void buttonAddUserToSession(View view) {
        addUserToSession(AccountData.getTappedSession().getSessionID());
        backToHome(view);
    }

    public void addUserToSession(String sessionId) {
        JSONObject info = new JSONObject();
        try {
            info.put("user_id", AccountData.getUserID());
            info.put("authentication_key",AccountData.getAuthKey());
            info.put("service", AccountData.getService());
            info.put("service_user_id", AccountData.getAuthKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String classid = AccountData.getTappedSession().getClassObject().getClassID().replaceAll("/", "%2F");
        sessionId = sessionId.replaceAll("/", "%2F");
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/"
                + classid.replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                + "/sessions/" + sessionId.replaceAll("/", "%2F").replaceAll("\\+", "%2B");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, info, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        //todo: toast success?
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    public void backToHome(View view) {
        Intent home = new Intent(this, HomeScreen.class);
        home.putExtra("from", "SessionInfo");
        home.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(home);
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
