package com.papaya.scotthanberg.papaya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.papaya.scotthanberg.papaya.R.id.NewStudySession;
import static com.papaya.scotthanberg.papaya.R.id.view;

/**
 * Created by calebflynn on 4/1/17.
 */

public class Menu extends AppCompatActivity {

    //Main Menu Buttons
    private ImageView menubutton;
    private TextView textView;
    private RelativeLayout dropDown, title;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;
    private Context context;

    public Menu(Context context) {
        this.context = context;

        final ImageView menuButton = (ImageView) ((Activity)context).findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleMenu(v.getContext());
            }
        });

        final Button newStudySessionButton = (Button) ((Activity)context).findViewById(R.id.NewStudySession);
        newStudySessionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonNewStudySession(v);
            }
        });

        final Button sortByClassButton = (Button) ((Activity)context).findViewById(R.id.SortByClass);
        sortByClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleMenu(v.getContext());
            }
        });

        final Button manageClassesButton = (Button) ((Activity)context).findViewById(R.id.ManageClasses);
        manageClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleMenu(v.getContext());
            }
        });

        final Button myFriendsButton = (Button) ((Activity)context).findViewById(R.id.FindFriends);
        myFriendsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonMyFriends(v);
            }
        });

        final Button joinNewClassButton = (Button) ((Activity)context).findViewById(R.id.JoinNewClass);
        joinNewClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonJoinClass(v);
            }
        });




        //inialzie variables
        dropDown = (RelativeLayout) ((Activity)context).findViewById(R.id.dropDown);
        //horizontalScroll = (HorizontalScrollView) ((Activity)context).findViewById(R.id.horizontalScroll);
        //backdrop = (View) ((Activity)context).findViewById(R.id.horizontalBackdrop);


        newStudySession = (Button) ((Activity)context).findViewById(R.id.NewStudySession);
        sortByClass = (Button) ((Activity)context).findViewById(R.id.SortByClass);
        manageClasses = (Button) ((Activity)context).findViewById(R.id.ManageClasses);
        findFriends = (Button) ((Activity)context).findViewById(R.id.FindFriends);
        joinNewClass = (Button) ((Activity)context).findViewById(R.id.JoinNewClass);

        title = (RelativeLayout) ((Activity)context).findViewById(R.id.title);

    }
    public void toggleMenu(Context context) {
        //get the view of the <include> so that it doesnt break
        this.context = context;
        View view = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        //toggle menu
        if (dropDown.getVisibility()==View.VISIBLE) {
            dropDown.setVisibility(View.GONE);
            //horizontalScroll.setVisibility(View.VISIBLE);
            //backdrop.setVisibility(View.VISIBLE);
            newStudySession.setVisibility(View.GONE);
            sortByClass.setVisibility(View.GONE);
            manageClasses.setVisibility(View.GONE);
            findFriends.setVisibility(View.GONE);
            joinNewClass.setVisibility(View.GONE);

//            final float scale = context.getResources().getDisplayMetrics().density;
//            int pixels = (int) (50 * scale + 0.5f);
//            RelativeLayout rl = (RelativeLayout) ((Activity)context).findViewById(R.id.view);
//            rl.getLayoutParams().height = pixels;

        } else {
            dropDown.setVisibility(View.VISIBLE);
            //dropDown.bringToFront();
            //backdrop.setVisibility(View.GONE);
            //horizontalScroll.setVisibility(View.GONE);
            newStudySession.setVisibility(View.VISIBLE);
            //newStudySession.bringToFront();
            sortByClass.setVisibility(View.VISIBLE);
            //sortByClass.bringToFront();
            manageClasses.setVisibility(View.VISIBLE);
            //manageClasses.bringToFront();
            findFriends.setVisibility(View.VISIBLE);
            //findFriends.bringToFront();
            joinNewClass.setVisibility(View.VISIBLE);

        }
    }

    public void buttonNewStudySession(View view) {
        Intent newStudySession = new Intent(context, CreateNewSession.class);
        context.startActivity(newStudySession);
    }

    public void buttonJoinClass(View view) {
        Intent joinClass = new Intent(context, JoinClass.class);
        context.startActivity(joinClass);
    }

    public void buttonMyFriends(View view) {
        Intent friendsList = new Intent(context, FriendsList.class);
        context.startActivity(friendsList);
    }

}
