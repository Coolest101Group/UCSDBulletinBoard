package com.apress.gerber.ucsdbulletinboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Profile extends AppCompatActivity {
    public static String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText labelView = (EditText) findViewById(R.id.profilename);
        labelView.setText(name);
    }
}
