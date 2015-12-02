package com.apress.gerber.ucsdbulletinboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Profile extends AppCompatActivity {
    public static String name = "";
    public static String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText nameBox = (EditText) findViewById(R.id.profilename);
        EditText emailBox = (EditText) findViewById(R.id.profileemail);
        nameBox.setText(name);
        emailBox.setText(email);
    }
}
