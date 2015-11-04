package com.apress.gerber.ucsdbulletinboard;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

public class CreateEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.create_event_toolbar);
        //setSupportActionBar(toolbar);
        //actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //finish();
        return true;
    }
}
