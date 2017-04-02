package com.papaya.scotthanberg.papaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ChristianLock on 3/1/17.
 */

public class GPlusFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GPlusFragent";
    private int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button signOutButton;
    private Button continue_to_papaya;
    private Button disconnectButton;
    private LinearLayout signOutView;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private ImageView imgProfilePic;
    private GoogleSignInAccount acct;
    private static String personName;
    private static String personGivenName;
    private static String personFamilyName;
    private static String personEmail;
    private static String personId;
    private static String authentication_key;
    private static String service;
    private Uri personPhoto;
    private static String idToken;
    private ImageView papayaPic;
    private boolean FBlogin;
    private PreferenceHandler preferenceHandler;
    private final AtomicInteger userCode = new AtomicInteger();

    //Facebook Login Variables
    private LoginButton loginButton;
    private ImageView profilePicImageView;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private boolean signedIn = false;


    
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("GPlusFragment", "Success!");
        }

        @Override
        public void onCancel() {
            Log.d("GPlusFragment", "Cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("GPlusFragment", String.format("Error: %s", error.toString()));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Facebook SDK init..
        FacebookSdk.sdkInitialize(getActivity());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    signInButton.setVisibility(View.VISIBLE);
                    continue_to_papaya.setVisibility(View.GONE);
                    profilePicImageView.setVisibility(View.GONE);
                    imgProfilePic.setVisibility(View.GONE);
                    papayaPic.setVisibility(View.VISIBLE);
                }
            }
        };
    }
    final android.os.Handler dataHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message m) {
            Bundle myData = m.getData();
            String user_id = myData.getString("user_id");
            if (user_id != null) {
                personId = user_id;
            }
        }
    };
    public void connectToDatabase() {
        // Update Auth needs: "Auth_option:1 or 2", "Username", Optional("Email"), "Service Name: FACEBOOK or GOOGLE", "Auth Key"
        // Create New needs: "Username", "Service Name", "Auth Key", Optional.. "Phone", "Email"
        // Since you CANNOT block the main thread, this has to be done on a "worker" or "background" thread
        final boolean[] done = {false};
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "https://a1ii3mxcs8.execute-api.us-west-2.amazonaws.com/Beta/user/";
                final JSONObject newJSONStudySession = new JSONObject();
                try {
                    if (FBlogin == true) {
                        newJSONStudySession.put("service", "FACEBOOK");
                        service = "FACEBOOK";
                    } else {
                        newJSONStudySession.put("service", "GOOGLE");
                        service = "GOOGLE";
                    }
                    newJSONStudySession.put("auth_option", 2);
                    newJSONStudySession.put("username", personName);
                    System.out.println("This is the username" + personName);
                    newJSONStudySession.put("authentication_key", authentication_key);
                    System.out.println("This is the authentication_key" + authentication_key);
                     /* Below code is not worky
                        TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                      String mPhoneNumber = tMgr.getLine1Number();
                       */
                    newJSONStudySession.put("phone", 5742386463l);
                    newJSONStudySession.put("email", personEmail);
                } catch (JSONException e) {
                    System.out.println("LOL you got a JSONException");
                }
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JsonObjectRequest jsObjRequestPUT = new JsonObjectRequest
                        (Request.Method.PUT, url, newJSONStudySession, future, future);
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequestPUT);
                try {
                    JSONObject response = future.get(10, TimeUnit.SECONDS);   // This will block
                    System.out.println("\n\n\n" + response.toString() + "\n\n\n");
                    // personId = response.getString("user_id");
                    userCode.set(response.getInt("code"));
                    personId = response.getString("user_id");
                    System.out.println("error checking");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                } catch (InterruptedException e) {
                } catch (TimeoutException e) {
                    System.out.println(e.toString());
                }
                if (userCode.get() == 404) {
                    RequestFuture<JSONObject> future1 = RequestFuture.newFuture();
                    JsonObjectRequest jsObjRequestPOST = new JsonObjectRequest
                            (Request.Method.POST, url, newJSONStudySession, future1, future1);
                    MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequestPOST);
                    try {
                        JSONObject response = future1.get();   // This will block
                        personId = response.getString("user_id");
                        done[0] = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch(ExecutionException e) {
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        preferenceHandler = new PreferenceHandler(getContext());
        String value = preferenceHandler.readFromSharedPref("user_id");
        if (value.equals("Value does not exist.")) {
            signedIn = false;
        } else {
            signedIn = true;
        }
        */
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            signInButton.setVisibility(View.GONE);
            imgProfilePic.setVisibility(View.GONE);
            signOutButton.setVisibility(View.GONE);
            continue_to_papaya.setVisibility(View.VISIBLE);
            profilePicImageView.setVisibility(View.VISIBLE);
            FBlogin = true;
        } else {
            signInButton.setVisibility(View.VISIBLE);
            imgProfilePic.setVisibility(View.GONE);
            continue_to_papaya.setVisibility(View.GONE);
            profilePicImageView.setVisibility(View.GONE);
            FBlogin = false;
        }
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            if (FBlogin == false) {
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            }
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            if (FBlogin == false) {
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Facebook Handling
        View v = inflater.inflate(R.layout.fragment_gplus, parent, false);
        loginButton = (LoginButton) v.findViewById(R.id.loginButton);
        loginButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        // Facebook Callback Registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast toast = Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT);
                toast.show();
                signInButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.GONE);
                profilePicImageView.setVisibility(View.VISIBLE);
                continue_to_papaya.setVisibility(View.VISIBLE);
                Profile p = Profile.getCurrentProfile();
                papayaPic.setVisibility(View.GONE);
                new LoadProfileImage(profilePicImageView).execute(p.getProfilePictureUri(200, 200).toString());
                updateUI();
            }

            @Override
            public void onCancel() {
                // App code
                papayaPic.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                profilePicImageView.setVisibility(View.GONE);
                continue_to_papaya.setVisibility(View.GONE);
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                papayaPic.setVisibility(View.VISIBLE);
                profilePicImageView.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                continue_to_papaya.setVisibility(View.VISIBLE);
                updateUI();
            }
        });

        profilePicImageView = (ImageView) v.findViewById(R.id.profilePicture);
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user_default);
        profilePicImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        });

        papayaPic = (ImageView) v.findViewById(R.id.papaya_picture);

        // Google Handling
        signInButton = (SignInButton) v.findViewById(R.id.sign_in_button);
        signOutButton = (Button) v.findViewById(R.id.sign_out_button);
        continue_to_papaya = (Button) v.findViewById(R.id.continue_to_papaya);
        imgProfilePic = (ImageView) v.findViewById(R.id.img_profile_pic);

        // mStatusTextView = (TextView) v.findViewById(R.id.status);
        imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                updateUI(false);
                            }
                        });
            }

        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Facebook Callback..
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //      mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //Similarly you can get the email and photourl using acct.getEmail() and  acct.getPhotoUrl()
            if (FBlogin == true) {
                service = "FACEBOOK";
            } else {
                service = "GOOGLE";
            }
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            authentication_key = acct.getId();
            personPhoto = acct.getPhotoUrl();
            idToken = acct.getIdToken();
            connectToDatabase();
            //addUserToDatabase();  // Should write to shared prefs for later
            // Below should only need to be executed on non first time sign ins
            /*
            if (personId == null) {
                String value = preferenceHandler.readFromSharedPref("user_id");
                if (value.equals("Value does not exist.")) {
                    System.out.println("No userid yet");
                } else {
                    personId = value;
                }
            }*/

            if (acct.getPhotoUrl() != null)
                new LoadProfileImage(imgProfilePic).execute(acct.getPhotoUrl().toString());
            /*
            if (!signedIn || signedIn) {
                Boolean value = preferenceHandler.writeToSharedPref("user_id", getPersonId());
                if (value == false) {
                    System.out.println("ERROR.  SHARED PREF NOT WRITING CORRECTLY");
                }
                try {
                    addUserToDatabase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            */
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            new LoadProfileImage(profilePicImageView).execute(profile.getProfilePictureUri(200, 200).toString());
        } else {
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user_default);
            profilePicImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            signInButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            continue_to_papaya.setVisibility(View.VISIBLE);
            imgProfilePic.setVisibility(View.VISIBLE);
            profilePicImageView.setVisibility(View.GONE);
            papayaPic.setVisibility(View.GONE);
            //  Intent toy = new Intent(this.getActivity(), HomeScreen.class);
        } else {
            // mStatusTextView.setText(R.string.signed_out);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user_default);
            imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
            signInButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            imgProfilePic.setVisibility(View.GONE);
            profilePicImageView.setVisibility(View.GONE);
            signOutButton.setVisibility(View.GONE);
            continue_to_papaya.setVisibility(View.GONE);
            papayaPic.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }

    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {


                Bitmap resized = Bitmap.createScaledBitmap(result, 200, 200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), resized, 250, 200, 200, false, false, false, false));

            }
        }
    }

    public static String getPersonId() {
        return personId;
    }

    public static String getAuthentication_key() {
        return authentication_key;
    }

    public static String getPersonEmail() {
        return personEmail;
    }

    public static String getPersonFamilyName() {
        return personFamilyName;
    }

    public static String getPersonGivenName() {
        return personGivenName;
    }

    public static String getPersonName() {
        return personName;
    }

    public static String getService() {
        return service;
    }
}

