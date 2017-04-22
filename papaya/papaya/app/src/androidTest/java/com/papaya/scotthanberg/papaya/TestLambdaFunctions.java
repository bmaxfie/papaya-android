package com.papaya.scotthanberg.papaya;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestLambdaFunctions {
    @Test
    public void testInsertSession() throws Exception {
        /*
        Set the AccountData that the lambda function
         */
        AccountData.setUserID("MO8Ls4UfgB2lk81HV4YBqg==");
        AccountData.setService("GOOGLE");
        AccountData.setAuthKey("0123456789012345678901234567890123456789");
        AccountData.setSponsored(false);

        /*
        Needed local variables that were previously in the CreateNewSessionClass
         */
        String description = "test description";
        Double myLatitude = 40.426234;
        Double myLongitude = -86.916162;
        int timeDuration = 10;
        String classId = "value2";

        /*
        Copied code from the class that calls the lambda function
         */

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classId + "/sessions";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final JSONObject newJSONStudySession = new JSONObject();

        try {
            newJSONStudySession.put("user_id", AccountData.getUserID());
            newJSONStudySession.put("duration", timeDuration);
            newJSONStudySession.put("location_desc", "Location Description");
            newJSONStudySession.put("location_lat", myLatitude.floatValue());
            newJSONStudySession.put("location_long", myLongitude.floatValue());
            newJSONStudySession.put("start_time", formatter.format(new Date()));
            newJSONStudySession.put("description", description);
            newJSONStudySession.put("service", AccountData.getService());
            newJSONStudySession.put("authentication_key", AccountData.getAuthKey());
            newJSONStudySession.put("sponsored", AccountData.getSponsored());
            newJSONStudySession.put("service_user_id", AccountData.getAuthKey()); //todo: replace with correct service_user_id
            newJSONStudySession.put("start_time", sdf.format(new Date()));
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
                                assert(true);
                                // Confirm session_id and class_id in response probably.
                            }
                            else {
                                assert(false);
                            }
                        } catch (JSONException json) {
                            assert(false);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CREATE_NEW_SESSION", "onErrorResponse: " + error.getMessage());
                        assert(false);
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(InstrumentationRegistry.getTargetContext()).addToRequestQueue(jsObjRequest);

        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait...
        }
    }

    @Test
    public void testInviteFriends() throws Exception{
        AccountData.setUserID("MO8Ls4UfgB2lk81HV4YBqg==");
        AccountData.setService("GOOGLE");
        AccountData.setAuthKey("0123456789012345678901234567890123456789");

        String classId = "value2";
        String sessionId = "Inhkfyrv0fEzH1te%2BySYXg==";

        String studentid = "sFW27p447QDzKhHd7d7BvA==";

        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classId + "/sessions/" + sessionId+"/invitations";

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
                        System.out.println(response);
                        assert(true);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        assert(false);
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(InstrumentationRegistry.getTargetContext()).addToRequestQueue(jsObjRequest);
        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait...
        }
    }
}
