package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button dropDown;

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

        if (savedInstanceState != null)
            AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstanceState.getSerializable(AccountData.ACCOUNT_DATA);
        else
            AccountData.data = (HashMap<AccountData.AccountDataType, Object>) getIntent().getSerializableExtra(AccountData.ACCOUNT_DATA);
    }


    public void loadMap(View view) {
        Intent homeScreen = new Intent(this, HomeScreen.class);
        homeScreen.putExtra("from","LoginActivity");
        homeScreen.putExtra(AccountData.ACCOUNT_DATA, AccountData.data);
        startActivity(homeScreen);
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

        AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }

    // TODO: Remove on Resume callback and put it in Create. If in onCreate, check for null parameter.

}
