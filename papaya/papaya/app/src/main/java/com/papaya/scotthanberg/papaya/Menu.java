package com.papaya.scotthanberg.papaya;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.papaya.scotthanberg.papaya.R.id.view;

/**
 * Created by calebflynn on 4/1/17.
 */

public class Menu extends AppCompatActivity {

    //Main Menu Buttons
    private ImageView menubutton;
    private TextView textView;
    private RelativeLayout dropDown;
    private View backdrop;
    private HorizontalScrollView horizontalScroll;
    private Button newStudySession, sortByClass, manageClasses, findFriends, joinNewClass;
    Context context;

    public Menu(Context context) {
        this.context = context;
        View view = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);

        //get the view of the <include> so that it doesnt break
        view = (View) ((View) view.getParent()).getParent();
        //inialzie variables
        dropDown = (RelativeLayout) view.findViewById(R.id.dropDown);
        //horizontalScroll = (HorizontalScrollView) view.findViewById(R.id.horizontalScroll);
        //backdrop = (View) view.findViewById(R.id.horizontalBackdrop);
        view = (View) dropDown;

        newStudySession = (Button) ((Activity)context).findViewById(R.id.NewStudySession);
        sortByClass = (Button) ((Activity)context).findViewById(R.id.SortByClass);
        manageClasses = (Button) ((Activity)context).findViewById(R.id.ManageClasses);
        findFriends = (Button) ((Activity)context).findViewById(R.id.FindFriends);
        joinNewClass = (Button) ((Activity)context).findViewById(R.id.JoinNewClass);
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
            dropDown.bringToFront();
            //backdrop.setVisibility(View.GONE);
            //horizontalScroll.setVisibility(View.GONE);
            newStudySession.setVisibility(View.VISIBLE);
            newStudySession.bringToFront();
            sortByClass.setVisibility(View.VISIBLE);
            sortByClass.bringToFront();
            manageClasses.setVisibility(View.VISIBLE);
            manageClasses.bringToFront();
            findFriends.setVisibility(View.VISIBLE);
            findFriends.bringToFront();
            joinNewClass.setVisibility(View.VISIBLE);
            joinNewClass.bringToFront();
//            final float scale = context.getResources().getDisplayMetrics().density;
//            int pixels = (int) (300 * scale + 0.5f);
//            RelativeLayout rl = (RelativeLayout) ((Activity)context).findViewById(R.id.view);
//            rl.getLayoutParams().height = pixels;
        }
    }
}
