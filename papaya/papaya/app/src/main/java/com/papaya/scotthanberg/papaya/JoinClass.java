package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JoinClass extends AppCompatActivity {
    private EditText edit;
    private String successMessage = "Joined Class!";
    private String failureMessage = "Failed to join class.  Try Again.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        Menu menu = new Menu(JoinClass.this);

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
        edit = (EditText)findViewById(R.id.editText2);

    }


    public void joinTheClass(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/classes";
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JSONObject jsObj = new JSONObject();
                try {
                    jsObj.put("access_key", edit.getText().toString());
                    jsObj.put("user_id", AccountData.getUserID());
                    jsObj.put("service", AccountData.getService());
                    jsObj.put("authentication_key",AccountData.getAuthKey());
                    jsObj.put("service_user_id", AccountData.getAuthKey());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsObj, future, future);
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
                try {
                    JSONObject response = future.get(5,TimeUnit.SECONDS);
                    int userCode = response.getInt("code");
                    System.out.println(response);
                    if (userCode == 201) {
                        runOnUiThread(show_toast_success);
                        backToHomescreen();
                    } else {
                        runOnUiThread(show_toast_failure);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    //check to see if the throwable in an instance of the volley error
                    if(e.getCause() instanceof VolleyError) {
                        //grab the volley error from the throwable and cast it back
                        VolleyError volleyError = (VolleyError) e.getCause();
                        System.out.println(volleyError.toString());
                    }
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void backToHomescreen() {
        Intent home = new Intent(this, HomeScreen.class);
        home.putExtra("from", "JoinClass");
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
     * @param savedInstancestate - supplied by Android OS
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstancestate) {
        super.onRestoreInstanceState(savedInstancestate);

        AccountData.data.clear();
        AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstancestate.getSerializable(AccountData.ACCOUNT_DATA));
        //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }

    private Runnable show_toast_success = new Runnable()
    {
        public void run()
        {
            Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT)
                    .show();
        }
    };
    private Runnable show_toast_failure = new Runnable()
    {
        public void run()
        {
            Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
