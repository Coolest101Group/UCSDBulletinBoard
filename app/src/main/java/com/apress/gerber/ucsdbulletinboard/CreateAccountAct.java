package com.apress.gerber.ucsdbulletinboard;

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

    //ui elements
    private AutoCompleteTextView mEmailView;
    private EditText mPassword1View;
    private EditText mPassword2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPassword1View = (EditText) findViewById(R.id.password1);
        mPassword2View = (EditText) findViewById(R.id.password2);

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
        mEmailView.setError(null);
        mPassword1View.setError(null);
        mPassword2View.setError(null);

        // Store email and password values
        String email = mEmailView.getText().toString();
        String password1 = mPassword1View.getText().toString();
        String password2 = mPassword2View.getText().toString();

        boolean cancel = false;
        View focusView = null;

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
            mPassword1View.setError(getString(R.string.error_invalid_password));
            focusView = mPassword1View;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
            mPassword2View.setError(getString(R.string.error_invalid_password));
            focusView = mPassword2View;
            cancel = true;
        }

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
            MainActivity.databaseRef.createUser(email, password1, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    Toast.makeText(getApplicationContext(), "Account creation successful", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    // there was an error
                    Toast.makeText(getApplicationContext(), "Account already exists", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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
}
