package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button dropDown;
    public static String EXTRA_LOADED_CLASSES = "LOADED_CLASSES";
    private static ArrayList<Class> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new GPlusFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public void loadMap(View view) {
        Intent homeScreen = new Intent(this, HomeScreen.class);
        homeScreen.putExtra("from","LoginActivity");
        homeScreen.putExtra(EXTRA_LOADED_CLASSES, classes);
        startActivity(homeScreen);
    }

    public static ArrayList<Class> loadClasses() {
        classes = new ArrayList<Class>();

        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/";
        final JSONObject newJSONStudySession = new JSONObject();
        try {
            if (FBlogin == true) {
                newJSONStudySession.put("service", "FACEBOOK");
                service = "FACEBOOK";
            } else {
                newJSONStudySession.put("service", "GOOGLE");
                service = "GOOGLE";
            }
            newJSONStudySession.put("username", personName);
            System.out.println("This is the username" + personName);
            newJSONStudySession.put("authentication_key", authentication_key);
            System.out.println("This is the authentication_key" + authentication_key);
            /* Below code is not worky
            TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            */
            newJSONStudySession.put("phone", 5742386463l);
            newJSONStudySession.put("email", personEmail);
        } catch (JSONException e) {
            System.out.println("LOL you got a JSONException");
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, newJSONStudySession, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("\n\n\n" + response.toString() + "\n\n\n");
                            personId = response.getString("user_id");
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }
}
