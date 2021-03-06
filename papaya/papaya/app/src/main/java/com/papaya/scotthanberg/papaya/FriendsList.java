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
import java.util.HashMap;

public class FriendsList extends AppCompatActivity {


    private ArrayList<User> listOfFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

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

        Menu menu = new Menu(FriendsList.this);

        listOfFriends = new ArrayList<User>();

        Intent list = getIntent(); // gets the previously created intent
        getFriends(); //get friends from databse and update text views

    }

    private void getFriends() {
        //TODO: replace the hard coding
        String url="https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/friends?" +
                "user_id="+ AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B") +
                "&service="+ AccountData.getService().replaceAll("/", "%2F").replaceAll("\\+", "%2B") +
                "&authentication_key=" + AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B") +
                "&service_user_id=" + AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B"); //todo:AccountData.getServiceUserId();

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray arr = response.getJSONArray("friends");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObj2 = arr.getJSONObject(i);
                                //we can use Students because we just need their name
                                Student temp = new Student(jsonObj2.getString("user_id"),jsonObj2.getString("username"));
                                listOfFriends.add(temp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AccountData.setFriends(listOfFriends);
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

        for (User user: listOfFriends) {
            String name = user.getName();

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

    /**
     * Android callback
     * Invoked when the activity may be temporarily destroyed, save the instance state here.
     * @param outState - supplised by Android OS
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(AccountData.ACCOUNT_DATA, AccountData.data);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
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
        AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA));
        //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }
}
