package com.papaya.scotthanberg.papaya;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        AccountData.setUserID("wBkaf4TqQtnZClGCF5fqQ==");
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
                                Assert.fail();
                            }
                        } catch (JSONException json) {
                            Assert.fail();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CREATE_NEW_SESSION", "onErrorResponse: " + error.getMessage());
                        Assert.fail();
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
        AccountData.setUserID("wBkaf4TqQtnZClGCF5fqQ==");
        AccountData.setService("GOOGLE");
        AccountData.setAuthKey("0123456789012345678901234567890123456789");

        String classId = "value2";
        String sessionId = "Inhkfyrv0fEzH1te%2BySYXg==";

        String studentid = "wBkaf4TqQtnZClGCF5fqQ==";

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
                        Assert.fail();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(InstrumentationRegistry.getTargetContext()).addToRequestQueue(jsObjRequest);
        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait...
        }
    }
    @Test
    public void testSessionDump() throws Exception {
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/website/sessions&professor_access_key=" + "12341236";
/*
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }) */
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        assert(true);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Assert.fail();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(InstrumentationRegistry.getTargetContext()).addToRequestQueue(jsObjRequest);
        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait...
        }
    }

    @Test
    public void testHideComment() {
        /*set variables*/
        AccountData.setUserID("wBkaf4TqQtnZClGCF5fqQ==");
        AccountData.setService("GOOGLE");
        AccountData.setAuthKey("0123456789012345678901234567890123456789");
        String classid = "value2";
        String sessionid = "8qg7B7T3N0Mlan2llmq2Gw==";
        classid = classid.replaceAll("/", "%2F").replaceAll("\\+", "%2B");
        sessionid = sessionid.replaceAll("/", "%2F").replaceAll("\\+", "%2B");
        String comment = "test comment";

        String post_id = ""; //to be set in creating a post part


        //create a comment to hide

        String user_id = AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B");
        final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classid +"/sessions/"+ sessionid +"/posts";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject newJSONStudySession = new JSONObject();
        try {
            newJSONStudySession.put("service", AccountData.getService());
            newJSONStudySession.put("authentication_key", AccountData.getAuthKey());
            newJSONStudySession.put("service_user_id", AccountData.getAuthKey());
            newJSONStudySession.put("user_id", user_id);
            newJSONStudySession.put("message", comment);
        } catch (JSONException e) {}
        JsonObjectRequest jsObjRequestPOST = new JsonObjectRequest
                (Request.Method.POST, url, newJSONStudySession, future, future);
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(InstrumentationRegistry.getTargetContext()).addToRequestQueue(jsObjRequestPOST);
        try {
            JSONObject response = future.get(10, TimeUnit.SECONDS);   // This will block
            if (response.getInt("code") == 201)
                post_id = response.getString("post_id");
        } catch (ExecutionException e) {Assert.fail();
        } catch (InterruptedException e) {Assert.fail();
        } catch (TimeoutException e) {
            System.out.println(e.toString());
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }


        /* Now Hide comment*/

        // HTTP Request call to delete it
        JSONObject info = new JSONObject();
        try {
            info.put("user_id", AccountData.getUserID());
            info.put("authentication_key",AccountData.getAuthKey());
            info.put("service", AccountData.getService());
            info.put("service_user_id", AccountData.getAuthKey());
            info.put("visibility", "1");
            info.put("post_id", post_id);
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }
        final String url2 = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classid +"/sessions/"+ sessionid +"/posts";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url2, info, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Deleted from the table");
                        try {
                            if (response.getInt("code") == 201) {
                                assert(true);
                            }
                            else {
                                Assert.fail();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Assert.fail();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Assert.fail();

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(InstrumentationRegistry.getTargetContext()).addToRequestQueue(jsObjRequest);
        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait...
        }
    }
}
