package com.apress.gerber.ucsdbulletinboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class CreateAccountAct extends AppCompatActivity {

    //random comment
    //ui elements
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPassword1View;
    private EditText mPassword2View;
    private View mCreateAccView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mFirstNameView = (EditText) findViewById(R.id.firstName);
        mLastNameView = (EditText) findViewById(R.id.lastName);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPassword1View = (EditText) findViewById(R.id.password1);
        mPassword2View = (EditText) findViewById(R.id.password2);
        mCreateAccView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.login_progress);

        Button mSignupButton = (Button) findViewById(R.id.email_signup_button);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateAcc();
            }
        });

    }

    public void attemptCreateAcc() {
        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPassword1View.setError(null);
        mPassword2View.setError(null);

        // Store name, email and password values
        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String email = mEmailView.getText().toString();
        String password1 = mPassword1View.getText().toString();
        String password2 = mPassword2View.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //check if name fields are empty
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
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

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
            mPassword1View.setError("Password must be at least 6 characters with at least 1 number");
            focusView = mPassword1View;
            cancel = true;
        }

        /* if the first password check fails, don't need to check the second one right?
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
            mPassword2View.setError(getString(R.string.error_invalid_password));
            focusView = mPassword2View;
            cancel = true;
        }*/ //

        //check if both passwords are the same
        if ( !password1.equals(password2) ) {
            mPassword2View.setError("Password Mismatch");
            focusView = mPassword2View;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // No error, do the create user thing
            showProgress(true); //start the progress spinner stuff's happening animation
            MainActivity.databaseRef.createUser(email, password1, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    //adding account successful

                    //add names to new user account
                    Firebase userRef = MainActivity.databaseRef.child("users").child(email.split("@", 2)[0]); //split email to just the stuff before @
                    userRef.child("firstName").setValue(firstName);
                    userRef.child("lastName").setValue(lastName);

                    showProgress(false); //turn off spinner animation

                    //show creation successful alert
                    new AlertDialog.Builder(CreateAccountAct.this)
                            .setMessage("Account Creation Successful")
                            //when the user hits ok, close the alert and close the create account page
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            CreateAccountAct.this.finish();
                                        }
                                    }
                            )
                            .create()
                            .show();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    // there was an error
                    showProgress(false); //turn off spinner animation

                    //show error alert
                    new AlertDialog.Builder(CreateAccountAct.this)
                            .setMessage("Account Already Exists")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }
                            )
                            .create()
                            .show();
                }
            });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCreateAccView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCreateAccView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCreateAccView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mCreateAccView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
