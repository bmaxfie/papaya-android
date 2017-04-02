package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SessionInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);
        Menu menu = new Menu(SessionInfo.this);
        //setup menu button listener
//        final ImageView button = (ImageView) findViewById(R.id.menuButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Menu menu = new Menu(SessionInfo.this);
//                menu.toggleMenu(SessionInfo.this);
//            }
//        });

        Intent studySession = getIntent(); // gets the previously created intent


    }


}
