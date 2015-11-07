package com.apress.gerber.ucsdbulletinboard;


import android.app.Dialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        CreateEvent.day = day;
        CreateEvent.mnth = month + 1;
        CreateEvent.yr = year;
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv = (TextView) getActivity().findViewById(R.id.tv2);
        //Set a message for user
        tv.setText("Your chosen day is... " + new StringBuilder()
                .append(CreateEvent.mnth).append("/")
                .append(CreateEvent.day)
                .append("/")
                .append(CreateEvent.yr)
                .toString()+"\n\n");
    }
}