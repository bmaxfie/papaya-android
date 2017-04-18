package com.papaya.scotthanberg.papaya;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CreateNewSessionUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testLambda() throws Exception {
        AccountData.setUserID("MO8Ls4UfgB2lk81HV4YBqg==");
        AccountData.setService("GOOGLE");
        AccountData.setAuthKey("0123456789012345678901234567890123456789");
        AccountData.setSponsored(false);

        String bla =AccountData.getUserID();

        //Needed local variables
        String description = "test description";
        Double myLatitude = 10.0;
        Double myLongitude = 10.0;
        int timeDuration = 10;
        String classId = "value2";



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
                            if (response.getInt("code") == 201) {

                                // Confirm session_id and class_id in response probably.
                            }
                            else {

                            }
                        } catch (JSONException jsone) {

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CREATE_NEW_SESSION", "onErrorResponse: " + error.getMessage());

                    }
                });

        // Access the RequestQueue through your singleton class.
        //todo: singleton....
        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait...
        }
        assert(true);

    }
}
