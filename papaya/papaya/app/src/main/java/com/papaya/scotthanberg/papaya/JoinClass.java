package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class JoinClass extends AppCompatActivity {

    //Main Menu Buttons
    private RelativeLayout dropDown;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        dropDown = (RelativeLayout) findViewById(R.id.dropDown);
        newStudySession = (Button) findViewById(R.id.NewStudySession);
        sortByClass = (Button) findViewById(R.id.SortByClass);
        manageClasses = (Button) findViewById(R.id.ManageClasses);
        findFriends = (Button) findViewById(R.id.FindFriends);
        joinNewClass = (Button) findViewById(R.id.JoinNewClass);

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
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/classes?authentication_key=" + AccountData.getAuthKey() + "&user_id=" + AccountData.getUserID() + "&service=" + AccountData.getService() + "&access_key=" + edit.getText().toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
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

        Intent home = new Intent(this, HomeScreen.class);
        home.putExtra("from", "JoinClass");
        home.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(home);
    }

    public void openMenu(View view) {
        if (dropDown.getVisibility()==View.VISIBLE) {
            dropDown.setVisibility(View.GONE);
            System.out.println("THIS IS WORKING");
            //   horizontalScroll.setVisibility(View.VISIBLE);
            //  backdrop.setVisibility(View.VISIBLE);
            newStudySession.setVisibility(View.GONE);
            sortByClass.setVisibility(View.GONE);
            manageClasses.setVisibility(View.GONE);
            findFriends.setVisibility(View.GONE);
            joinNewClass.setVisibility(View.GONE);
        } else {
            dropDown.setVisibility(View.VISIBLE);
            // backdrop.setVisibility(View.GONE);
            // horizontalScroll.setVisibility(View.GONE);
            newStudySession.setVisibility(View.VISIBLE);
            sortByClass.setVisibility(View.VISIBLE);
            manageClasses.setVisibility(View.VISIBLE);
            findFriends.setVisibility(View.VISIBLE);
            joinNewClass.setVisibility(View.VISIBLE);
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
     * @param savedInstancestate - supplied by Android OS
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstancestate) {
        super.onRestoreInstanceState(savedInstancestate);

        AccountData.data.clear();
        AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstancestate.getSerializable(AccountData.ACCOUNT_DATA));
        //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }
}
