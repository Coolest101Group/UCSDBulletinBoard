package com.apress.gerber.ucsdbulletinboard;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.apress.gerber.ucsdbulletinboard.*;
import com.apress.gerber.ucsdbulletinboard.fragments.FeaturedEvents;
import com.apress.gerber.ucsdbulletinboard.models.manageEvents;

import java.util.concurrent.TimeUnit;

public class CreateEvent extends AppCompatActivity {

    private EditText eTitleView;
    private EditText eDescView;
    private DatePicker ePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.create_event_toolbar);
        //setSupportActionBar(toolbar);
        //actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        // Get event information from create_event fragment
        eTitleView = (EditText)findViewById(R.id.event_title);
        eDescView = (EditText)findViewById(R.id.event_description);
        ePicker = (DatePicker)findViewById(R.id.event_date);

        // When user clicks "create" button invoke 'addEvent()' method
        Button mCreateEventButton = (Button) findViewById(R.id.event_sign_up_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventDB();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //finish();
        return true;
    }

    public boolean addEventDB() {
        // Reset errors
        eTitleView.setError(null);
        eDescView.setError(null);



        String mTitle = eTitleView.getText().toString();
        String mDesc = eDescView.getText().toString();
        int year = ePicker.getYear();
        int month = ePicker.getMonth();
        int day = ePicker.getDayOfMonth();

        String mDate = new StringBuilder().append(month +1)
                .append("-").append(day).append("-").append(year)
                .append(" ").toString();

        boolean success = MainActivity.mDB.postEvent(mTitle, mDate, mDesc, day, month, year);
        if(success){

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Event added successfully.", Toast.LENGTH_LONG).show();
                }
            });

            Intent intent = new Intent(this, FeaturedEvents.class);
            this.startActivity(intent);

        }
        else{
            Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
        }

        return true;
    }
}
