package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class FriendsList extends AppCompatActivity {
    //Main Menu Buttons
    private RelativeLayout dropDown;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;

    private ArrayList<String> listOfFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        dropDown = (RelativeLayout) findViewById(R.id.dropDown);
        horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        backdrop = (View) findViewById(R.id.horizontalBackdrop);
        newStudySession = (Button) findViewById(R.id.NewStudySession);
        sortByClass = (Button) findViewById(R.id.SortByClass);
        manageClasses = (Button) findViewById(R.id.ManageClasses);
        findFriends = (Button) findViewById(R.id.FindFriends);
        joinNewClass = (Button) findViewById(R.id.JoinNewClass);

        listOfFriends = new ArrayList<String>();

        Intent list = getIntent(); // gets the previously created intent
        getFriends(); //get friends from databse and update text views

    }



    private void getFriends() {
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
                                listOfFriends.add(temp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //testing: listOfFriends.add("hello world");
                        createFriendTextViews(); //updates the view with the new list of friends

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("error:");
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);


//for testing
//        for (int i= 0; i < 20; i++) {
//            result.add("helloWorld"+i);
//        }

    }

    public void createFriendTextViews() {

        LinearLayout ll = (LinearLayout) findViewById(R.id.friendContainer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,5);

        for (String name: listOfFriends) {
            TextView myText = new TextView(this);
            myText.setText(name);
            myText.setTextSize(24);
            myText.setPadding(15, 15, 15, 15);
            myText.setBackgroundColor(Color.WHITE);
            /*
            <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="hello world1"
            android:textSize="24sp"
            android:padding="10sp"
            android:layout_marginBottom="3sp"
            android:background="@android:color/white"/>
            */


            ll.addView(myText, lp);
        }
    }
}
