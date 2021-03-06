package com.papaya.scotthanberg.papaya;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InviteFriends extends AppCompatActivity {


    private ArrayList<User> listOfFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

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

        Menu menu = new Menu(InviteFriends.this);

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


        /*
        Puts the list of people and buttons on the View
         */

        LinearLayout rl = (LinearLayout) findViewById(R.id.friendContainer);
        rl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp;

        int counter = 0;
        for (User user: listOfFriends) {
            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout sideways = new LinearLayout(this);
            sideways.setOrientation(LinearLayout.HORIZONTAL);
            sideways.setPadding(15, 15, 15, 15);
            sideways.setBackgroundColor(Color.WHITE);


            //put person name to the left
            TextView myText = new TextView(this);
            myText.setText(user.getName());
            myText.setTextSize(20);
            //myText.setPadding(0, 0, 0, 15);
            //myText.setBackgroundColor(Color.WHITE);
            myText.setId(counter);
            sideways.addView(myText, lp);

            //todo: if they are already a friend, do not add button
            //put button next to name
            Button button = new Button(this);
            button.setText("Invite");
            sideways.addView(button);

            //set lp

            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 5);
            rl.addView(sideways, lp);

            //add button listener
            button.setTag(user);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button b = (Button) v;
                    final Student student = (Student) b.getTag();
                    String studentid = student.getUserID();
                    StudySession session = AccountData.getTappedSession();
                    String sessionId = session.getSessionID().replaceAll("/", "%2F").replaceAll("\\+", "%2B");
                    String classId = session.getClassObject().getClassID().replaceAll("/", "%2F").replaceAll("\\+", "%2B");

                    String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/"
                            + classId + "/sessions/" +
                            sessionId + "/invitations";

                    final JSONObject newJSONStudySession = new JSONObject();
                    try {
                        newJSONStudySession.put("user_id", AccountData.getUserID());
                        newJSONStudySession.put("user_id2", studentid);
                        newJSONStudySession.put("service", AccountData.getService());
                        newJSONStudySession.put("authentication_key", AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B"));
                        newJSONStudySession.put("service_user_id", AccountData.getAuthKey().replaceAll("/", "%2F").replaceAll("\\+", "%2B")); //todo: replace with correct service_user_id
                    } catch (JSONException e) {
                        System.out.println("LOL you got a JSONException");
                    }

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, newJSONStudySession, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        System.out.println(response);
                                        if (response.getInt("code") == 201) {
                                            Toast.makeText(InviteFriends.this, "Invitation sent to " + student.getName(), Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(InviteFriends.this, response.getString("code_description"), Toast.LENGTH_LONG).show();
                                            Log.d("INVITEFRIENDS", response.getString("code_description"));
                                        }
                                    } catch (JSONException jsone) {
                                        Toast.makeText(InviteFriends.this, "Malformed JSON Response ERROR.", Toast.LENGTH_LONG).show();
                                        Log.d("INVITEFRIENDS", "Malformed JSON Response ERROR.");
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("CREATE_NEW_SESSION", "onErrorResponse: " + error.getMessage());

                                }
                            });

                    // Access the RequestQueue through your singleton class.
                    MySingleton.getInstance(InviteFriends.this).addToRequestQueue(jsObjRequest);

                }
            });

            counter++;

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
