package com.papaya.scotthanberg.papaya;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FriendsList extends AppCompatActivity {
    //Main Menu Buttons
    private RelativeLayout dropDown;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        dropDown = (RelativeLayout) findViewById(R.id.dropDown);
        horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        backdrop = (View) findViewById(R.id.horizontalBackdrop);
        newStudySession = (Button) findViewById(R.id.NewStudySession);
        sortByClass = (Button) findViewById(R.id.SortByClass);
        manageClasses = (Button) findViewById(R.id.ManageClasses);
        findFriends = (Button) findViewById(R.id.FindFriends);
        joinNewClass = (Button) findViewById(R.id.JoinNewClass);

        Intent list = getIntent(); // gets the previously created intent
        createFriendTextViews();
    }

    private ArrayList<String> getFriends() {
        ArrayList<String> result = new ArrayList<String>();

        //todo: fix to get friends from database

        for (int i= 0; i < 20; i++) {
            result.add("helloWorld"+i);
        }

        return result;
    }

    public void createFriendTextViews() {
        ArrayList<String> friends = getFriends();

        LinearLayout ll = (LinearLayout) findViewById(R.id.friendContainer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,5);

        for (String name: friends) {
            TextView myText = new TextView(this);
            myText.setText(name);
            myText.setTextSize(24);
            myText.setPadding(15, 15, 15, 15);
            myText.setBackgroundColor(Color.WHITE);
            /*
            <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="hello world1"
            android:textSize="24sp"
            android:padding="10sp"
            android:layout_marginBottom="3sp"
            android:background="@android:color/white"/>
            */


            ll.addView(myText, lp);
        }
    }
}
