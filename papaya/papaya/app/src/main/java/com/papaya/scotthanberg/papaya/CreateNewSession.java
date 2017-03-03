package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CreateNewSession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_session);
    }

    public void createSession(View view) {
        Intent sessionCreated = new Intent(this, HomeScreen.class);
        Toast toast = Toast.makeText(this, "Study Session Created", Toast.LENGTH_SHORT);
        toast.show();
        startActivity(sessionCreated);
    }

    public void openMenu(View view) {
        Toast toast = Toast.makeText(this, "Already creating session", Toast.LENGTH_SHORT);
        toast.show();
    }

}
