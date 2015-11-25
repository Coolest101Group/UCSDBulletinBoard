package com.apress.gerber.ucsdbulletinboard.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.ucsdbulletinboard.CreateAccountAct;
import com.apress.gerber.ucsdbulletinboard.CreateEvent;
import com.apress.gerber.ucsdbulletinboard.LoginActivity;
import com.apress.gerber.ucsdbulletinboard.MainActivity;
import com.apress.gerber.ucsdbulletinboard.Profile;
import com.apress.gerber.ucsdbulletinboard.R;
import com.apress.gerber.ucsdbulletinboard.adapter.NavListAdapter;
import com.apress.gerber.ucsdbulletinboard.models.NavItem;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shaded.fasterxml.jackson.databind.ser.std.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by danielmartin on 10/23/15.
 */

public class Login extends Fragment {
    @Nullable


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mV;
    public String firstName;
    public String lastName;
    public String email;
    public String password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        //this login class also gets called to logout
        //this is the logout logic
        if(MainActivity.loggedIn && !MainActivity.firstTimeLogin) {
            MainActivity.mNavItemList.get(0).setTitle("Login");
            MainActivity.mNavItemList.get(0).setResIcon(R.drawable.login17);
            MainActivity.mNavItemList.remove(1);
            MainActivity.loggedIn = false;

            new AlertDialog.Builder(getContext())
                    .setMessage("You've been successfully logged out")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    //pop back to main activity
                                    getFragmentManager().popBackStack();
                                }
                            }
                    )
                    .create()
                    .show();
        }

        mV = inflater.inflate(R.layout.activity_login, container, false);


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) mV.findViewById(R.id.email);


        mPasswordView = (EditText) mV.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(savedInstanceState);
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) mV.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(savedInstanceState); //makes global loggedin bool true if works
            }
        });

        Button mRegisterButton = (Button) mV.findViewById(R.id.create_new_account_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateAccountAct.class);
                startActivity(intent);
            }
        });

        mLoginFormView = mV.findViewById(R.id.login_form);
        mProgressView = mV.findViewById(R.id.login_progress);

        return mV;
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(final Bundle savedInstanceState) {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        firstName = "";
        lastName = "";

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Password must be at least 6 characters with at least 1 number");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            MainActivity.databaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    //login successful
                    //retrieve first and last names from database
                    MainActivity.databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            names theNames = snapshot.child("users")
                                    .child(email.split("@", 2)[0]) //check just the email address before @
                                    .getValue(names.class);

                            firstName = theNames.getFirstName();
                            lastName = theNames.getLastName();

                            if (firstName != null && lastName != null) putNamesOnMainList();


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                    ///change state to logged in
                    MainActivity.loggedIn = true;

                    showProgress(false); //stop progress spinner

                    //show success message alert
                    new AlertDialog.Builder(getContext())
                            .setMessage("Login Successful")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            System.out.println("This logged in IF statement is running");
                                            MainActivity.mNavItemList.get(0).setTitle("Logout");
                                            MainActivity.mNavItemList.get(0).setResIcon(R.drawable.logout);

                                            MainActivity.mNavItemList.add(1, new NavItem("Manage Account", " ", R.drawable.mng_account));


                                            NavListAdapter navListAdapter = new NavListAdapter(
                                                    getActivity().getApplicationContext(), R.layout.item_nav_list, MainActivity.mNavItemList);

                                            MainActivity.lvNav.setAdapter(navListAdapter);
                                            MainActivity.firstTimeLogin = false; //now its no longer the first time

                                            //pop back to main activity
                                            getFragmentManager().popBackStack();
                                        }
                                    }
                                        )

                            .create()
                            .show();
                            }

                    @Override
                    public void onAuthenticationError (FirebaseError firebaseError){
                        // there was an error
                        showProgress(false); //stop progress spinner

                        //show error message alert
                        new AlertDialog.Builder(getContext())
                                .setMessage(firebaseError.getMessage())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }
                                )
                                .create()
                                .show();
                    }
                }

                );

        }
    if(MainActivity.loggedIn) {
        putNamesOnMainList();
        getFragmentManager().popBackStack();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //password has to be at least 6 characters with at least 1 number
        return (password.length() > 5 && password.matches(".*\\d+.*"));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private static class names{
        String firstName;
        String lastName;
        public names(){}
        public void setFirstName(String firstName){this.firstName = firstName;}
        public void setLastName(String lastName) {this.lastName = lastName;}
        public String getFirstName(){return firstName;}
        public String getLastName(){return lastName;}

    }

    public void putNamesOnMainList(){
        System.out.println("does this run?");
        TextView t = (TextView) getActivity().findViewById(R.id.name_mainMenu);
        t.setText((firstName + " " + lastName).toString());

        Profile.name = (firstName + " " + lastName).toString();

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}
