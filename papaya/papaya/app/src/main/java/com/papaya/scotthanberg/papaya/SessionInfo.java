package com.papaya.scotthanberg.papaya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SessionInfo extends AppCompatActivity {
    ArrayList<commentPost> commentPostsArray = new ArrayList<commentPost>();
    ArrayAdapter<commentPost> adapter;
    String locationDesription;
    String description, classid, sessionid;
    Button post;
    int role;
    InputMethodManager mgr;
    EditText commentText;
    boolean firstTime = true;
    RelativeLayout scrollableText;
    //doesn't have to be a student, just need to hold both id and username
    ArrayList<Student> people = new ArrayList<Student>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        //sets up the menu and all its buttons
        Menu menu = new Menu(SessionInfo.this);

        Intent studySession = getIntent(); // gets the previously created intent
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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

        //todo: call /classes/{class-id}/sessions/{session-id} GET to get the information about the class
        getInfo();
    }


    private void getInfo() {
        //since classid and sessionid are in the url, they can't have '/' in them
        classid = AccountData.getTappedSession().getClassObject().getClassID();
        sessionid = AccountData.getTappedSession().getSessionID();
        classid = classid.replaceAll("/", "%2F"); //
        sessionid = sessionid.replaceAll("/", "%2F");

        String url="https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" +
                classid + "/" +
                "sessions/" + sessionid +
                "?user_id=" + AccountData.getUserID() +
                "&service=" + AccountData.getService() +
                "&authentication_key=" + AccountData.getAuthKey() +
                "&service_user_id=" + AccountData.getAuthKey();

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray arr = response.getJSONArray("users");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObj2 = arr.getJSONObject(i);
                                String id = jsonObj2.getString("user_id");
                                String name = jsonObj2.getString("username");
                                people.add(new Student(id, name));
                            }
                            locationDesription = response.getString("location_desc");
                            description = response.getString("description");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //testing: listOfFriends.add("hello world");
                        createView(); //updates the view with the new list of friends

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("error:");
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        while(!jsObjRequest.hasHadResponseDelivered()) {
            //wait until it has responeded
        }
    }

    public void createView() {
        /*
        Populates the Location description and the Session Description
         */
        TextView locDesc = (TextView) findViewById(R.id.location_desc);
        locDesc.setText(locationDesription);

        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(this.description);

        /*
        Puts the list of people and buttons on the View
         */

        LinearLayout rl = (LinearLayout) findViewById(R.id.peopleContainer);
        rl.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp;
        //lp.setMargins(0,0,0,5);

        int counter = 0;
        for (Student s : people) {
            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout sideways = new LinearLayout(this);
            sideways.setOrientation(LinearLayout.HORIZONTAL);

            //put person name to the left
            TextView myText = new TextView(this);
            myText.setText(s.getName());
            myText.setTextSize(20);
            //myText.setPadding(0, 0, 0, 15);
            //myText.setBackgroundColor(Color.WHITE);
            myText.setId(counter);
            sideways.addView(myText, lp);

            //todo: if they are already a friend, do not add button
            //put button next to name
            Button button = new Button(this);
            button.setText("Add Friend");
            sideways.addView(button);

            //set lp

            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rl.addView(sideways, lp);

            //add button listener
            button.setTag(s);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //todo: call function to add them as a friend
                    //use the userId that is held in the s.getUserID()
                    Button b = (Button) v;
                    Student student = (Student) b.getTag();
                    String studentid = student.getUserID();

                    JSONObject info = new JSONObject();
                    try {
                        info.put("user_id", AccountData.getUserID());
                        info.put("user_id2", studentid);
                        info.put("authentication_key",AccountData.getAuthKey());
                        info.put("service", AccountData.getService());
                        info.put("service_user_id", AccountData.getAuthKey());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/friends";

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, info, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        int code = response.getInt("code");
                                        System.out.println(response);
                                        if (code == 201) {
                                            Toast toast = Toast.makeText(SessionInfo.this, "You are now friends", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
            });

            counter++;


        }
    }

    public void viewCommentsButton(View view) {
        commentPostsArray.clear();
        setContentView(R.layout.comments_board);
        commentText = (EditText) findViewById(R.id.commentText);
        scrollableText = (RelativeLayout) findViewById(R.id.scrollableText);
        final ListView lv = (ListView) findViewById(R.id.commentsBoardView);
        adapter = new commentsAdapter(this, commentPostsArray);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final commentPost item = (commentPost) lv.getItemAtPosition(position);
                for (int i = 0; i < AccountData.getClasses().size(); i++) {
                    if (AccountData.getClasses().get(i).getClassID().equals(classid)) {
                        if (AccountData.getClasses().get(i).getRole() == 1) {
                            System.out.println("Do not have the permissins to delete a comment");
                            return;
                        } else {
                            // Create a dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(SessionInfo.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Delete this comment?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // HTTP Request call to delete it
                                            JSONObject info = new JSONObject();
                                            try {
                                                info.put("user_id", AccountData.getUserID());
                                                info.put("authentication_key",AccountData.getAuthKey());
                                                info.put("service", AccountData.getService());
                                                info.put("service_user_id", AccountData.getAuthKey());
                                                info.put("visibility", "1");
                                                info.put("post_id", item.getPostId());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classid +"/sessions/"+ sessionid +"/posts";
                                            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                                    (Request.Method.PUT, url, info, new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            System.out.println("Deleted from the table");
                                                        }
                                                    }, new Response.ErrorListener() {

                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            // TODO Auto-generated method stub

                                                        }
                                                    });
                                            // Access the RequestQueue through your singleton class.
                                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
                                            System.out.println("DELETED");
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                }
            }
        });
        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classid +"/sessions/"+ sessionid +"/posts/"
                        + "?user_id=" + AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&service=" + AccountData.getService().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&authentication_key=12345123451234512345123451234512345123451234512345"
                        + "&service_user_id=12345123451234512345123451234512345123451234512345";
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JSONObject newJSONStudySession = new JSONObject();
                JsonObjectRequest jsObjRequestGET = new JsonObjectRequest
                        (Request.Method.GET, url, newJSONStudySession, future, future);
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequestGET);
                try {
                    JSONObject response = future.get(10, TimeUnit.SECONDS);  // This will block
                    JSONArray posts = response.getJSONArray("posts");
                    for (int i =0;i<posts.length();i++) {
                        JSONObject jsonobject = (JSONObject) posts.get(i);
                        commentPostsArray.add(new commentPost(jsonobject.optString("username"), jsonobject.optString("message"), jsonobject.optString("post_id")));
                        System.out.println("Please Work");
                    }
                } catch (JSONException e) {
                } catch (ExecutionException e) {
                } catch (InterruptedException e) {
                } catch (TimeoutException e) {
                    System.out.println(e.toString());
                }
            }
        });
        run.start();
      /*  Thread update = new Thread(new Runnable() {
            public void run() {
                updateComments();
            }
        }); update.start();*/
    }

    public void updateComments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classid +"/sessions/"+ sessionid +"/posts/"
                        + "?user_id=" + AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&service=" + AccountData.getService().replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                        + "&authentication_key=12345123451234512345123451234512345123451234512345"
                        + "&service_user_id=12345123451234512345123451234512345123451234512345";
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JSONObject newJSONStudySession = new JSONObject();
                JsonObjectRequest jsObjRequestGET = new JsonObjectRequest
                        (Request.Method.GET, url, newJSONStudySession, future, future);
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequestGET);
                try {
                    JSONObject response = future.get(10, TimeUnit.SECONDS);  // This will block
                    JSONArray posts = response.getJSONArray("posts");
                    commentPostsArray.clear();
                    for (int i =0;i<posts.length();i++) {
                        JSONObject jsonobject = (JSONObject) posts.get(i);
                        commentPostsArray.add(new commentPost(jsonobject.optString("username"), jsonobject.optString("message"), jsonobject.optString("post_id")));
                        System.out.println("Please Work");
                    }
                } catch (JSONException e) {
                } catch (ExecutionException e) {
                } catch (InterruptedException e) {
                } catch (TimeoutException e) {
                    System.out.println(e.toString());
                }
            }
        }).start();
    }

    public void refresh(View view) {
        adapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(SessionInfo.this, "Comments Updated", Toast.LENGTH_SHORT);
        toast.show();
        if (!firstTime) {
            updateComments();
            adapter.notifyDataSetChanged();
        }
        firstTime = false;
        adapter.notifyDataSetChanged();
    }

    public void postComment(View view) {
        addComment(commentText.getText().toString());
        commentText.setText("");
        mgr.hideSoftInputFromWindow(commentText.getWindowToken(), 0);

    }

    public void addComment(final String comment) { // Access determines if they are a student(0), TA(1), or prof(2)
        Thread run1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String user_id = AccountData.getUserID().replaceAll("/", "%2F").replaceAll("\\+", "%2B");
                final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/" + classid +"/sessions/"+ sessionid +"/posts";
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JSONObject newJSONStudySession = new JSONObject();
                try {
                    newJSONStudySession.put("service", AccountData.getService());
                    newJSONStudySession.put("authentication_key", AccountData.getAuthKey());
                    newJSONStudySession.put("service_user_id", AccountData.getAuthKey());
                    newJSONStudySession.put("user_id", user_id);
                    newJSONStudySession.put("message", comment);
                } catch (JSONException e) {}
                JsonObjectRequest jsObjRequestPOST = new JsonObjectRequest
                        (Request.Method.POST, url, newJSONStudySession, future, future);
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequestPOST);
                try {
                    JSONObject response = future.get(10, TimeUnit.SECONDS);   // This will block
                    addCommentToarray(comment,response.getString("post_id"));
                    System.out.println("IT WORKED");
                } catch (ExecutionException e) {
                } catch (InterruptedException e) {
                } catch (TimeoutException e) {
                    System.out.println(e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        run1.start();
        if (commentPostsArray.size()>5) {
            scrollableText.setVisibility(View.VISIBLE);
        }
        updateComments();
        adapter.notifyDataSetChanged();
    }
    public void addCommentToarray(String comment, String postId) {
        commentPostsArray.add(new commentPost(AccountData.getUsername(), comment, postId));
    }

    public void buttonAddUserToSession(View view) {
        addUserToSession(AccountData.getTappedSession().getSessionID());
        backToHome(view);
    }

    public void addUserToSession(String sessionId) {
        JSONObject info = new JSONObject();
        try {
            info.put("user_id", AccountData.getUserID());
            info.put("authentication_key",AccountData.getAuthKey());
            info.put("service", AccountData.getService());
            info.put("service_user_id", AccountData.getAuthKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String classid = AccountData.getTappedSession().getClassObject().getClassID().replaceAll("/", "%2F");
        sessionId = sessionId.replaceAll("/", "%2F");
        String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/classes/"
                + classid.replaceAll("/", "%2F").replaceAll("\\+", "%2B")
                + "/sessions/" + sessionId.replaceAll("/", "%2F").replaceAll("\\+", "%2B");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, info, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        //todo: toast success?
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

    public void backToHome(View view) {
        Intent home = new Intent(this, HomeScreen.class);
        home.putExtra("from", "SessionInfo");
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
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);

        outState.putSerializable(AccountData.ACCOUNT_DATA, AccountData.data);
    }

    /**
     * Android callback
     * This callback is called only when there is a saved instance previously saved using
     * onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
     * other state here, possibly usable after onStart() has completed.
     * The savedInstanceState Bundle is same as the one used in onCreate().
     * @param savedInstanceState - supplied by Android OS
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        AccountData.data.clear();
        AccountData.data.putAll((HashMap<AccountData.AccountDataType, Object>) savedInstanceState.get(AccountData.ACCOUNT_DATA));
        //AccountData.data = (HashMap<AccountData.AccountDataType, Object>) savedInstancestate.get(AccountData.ACCOUNT_DATA);
    }
}
