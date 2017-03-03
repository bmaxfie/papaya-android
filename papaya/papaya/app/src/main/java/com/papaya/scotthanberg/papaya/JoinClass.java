package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class JoinClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
    }

    public void backToHome(View view) {
        Intent home = new Intent(this, HomeScreen.class);
        home.putExtra("from", "JoinClass");
        startActivity(home);
    }
}
