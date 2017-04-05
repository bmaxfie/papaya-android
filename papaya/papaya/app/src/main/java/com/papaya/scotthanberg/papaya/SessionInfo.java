package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        //TODO: replace the hard coding




        String url="https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" +
                AccountData.getTappedSession().getClassObject().getClassID() + "/" +
                "sessions/" + AccountData.getTappedSession().getSessionID() +
                "?user_id=" + AccountData.getUserID() +
                "&service=" + AccountData.getService() +
                "&authentication_key=" + AccountData.getAuthKey();


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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //testing: listOfFriends.add("hello world");
                        createPeopleTextViews(); //updates the view with the new list of friends

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

    public void createPeopleTextViews() {
        //testing: todo: remove this


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
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //todo: call function to add them as a friend
                    //use the userId that is held in the s.getUserID()
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
        // I hardcoded my user_id only because we're going to have to change it anyway with the new Data Manager
        JSONObject info = new JSONObject();
        try {
            info.put("user_id", "GXuFK5J9RLm1SgueLKJCFg==");
            info.put("authentication_key",AccountData.getAuthKey());
            info.put("service", AccountData.getService());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(sessionId);
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + "111" + "/sessions/" + sessionId;
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
