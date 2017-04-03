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

public class SessionInfo extends AppCompatActivity {

    ArrayList<String> people = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        //sets up the menu and all its buttons
        Menu menu = new Menu(SessionInfo.this);

        Intent studySession = getIntent(); // gets the previously created intent

        getUsers();
    }

    private void getUsers() {
        //TODO: replace the hard coding
        String url="https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/friends?" +
                "user_id="+ "mX9hzcEETRVfVWqD6nKz5A==" +"&" + //GPlusFragment.getPersonId();
                "service="+ "GOOGLE" +"&" +
                "authentication_key=" + "1234567890123456789012345678901234567890";

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray arr = response.getJSONArray("friends");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObj2 = arr.getJSONObject(i);
                                String temp = jsonObj2.getString("username");
                                people.add(temp);
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
        //testing:
        for(int i = 0; i < 20; i++) {
            people.add("Person " + i);
        }

        LinearLayout rl = (LinearLayout) findViewById(R.id.peopleContainer);
        rl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp;
        //lp.setMargins(0,0,0,5);

        int counter = 0;
        for (String name: people) {
            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout sideways = new LinearLayout(this);
            sideways.setOrientation(LinearLayout.HORIZONTAL);

            //put person name to the left
            TextView myText = new TextView(this);
            myText.setText(name);
            myText.setTextSize(20);
            //myText.setPadding(0, 0, 0, 15);
            //myText.setBackgroundColor(Color.WHITE);
            myText.setId(counter);
            sideways.addView(myText, lp);

            //put button next to name
            Button button = new Button(this);
            button.setText("Add Friend");
            sideways.addView(button);

            //set lp

            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rl.addView(sideways, lp);

            //add button listener

            counter++;


        }
    }

}
