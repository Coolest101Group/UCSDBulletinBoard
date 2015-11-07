package com.apress.gerber.ucsdbulletinboard;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by danielmartin on 11/6/15.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    protected int hour;
    protected int min;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        CreateEvent.hr = hourOfDay;
        CreateEvent.min = minute;
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //Set a message for user
        tv.setText("Your chosen time is... " + myEvent.getTimeString(CreateEvent.hr, CreateEvent.min)+ "\n\n");

    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return min;
    }
}