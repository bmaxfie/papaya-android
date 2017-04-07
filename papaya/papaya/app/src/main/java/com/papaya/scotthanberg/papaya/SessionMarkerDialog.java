package com.papaya.scotthanberg.papaya;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by scotthanberg on 3/31/17.
 */

public class SessionMarkerDialog extends DialogFragment {
    // will need a class id for session as well
    String sessionId;
    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionId = getArguments().getString("id");

    }
    */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Join this study session?")
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addUserToSession(sessionId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void addUserToSession(String sessionId) {
        // I hardcoded my user_id only because we're going to have to change it anyway with the new Data Manager
        JSONObject info = new JSONObject();
        try {
            info.put("user_id", "bNvqxLf+m6VbMx1x8OCQrw==");
            info.put("authentication_key",AccountData.getAuthKey());
            info.put("service", AccountData.getService());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(sessionId);
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + "111" + "/sessions/" + sessionId.replaceAll("/", "%2F").replaceAll("\\+", "%2B");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, info, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
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


    public void setSessionId(String title) {
        sessionId = title;
    }
}
