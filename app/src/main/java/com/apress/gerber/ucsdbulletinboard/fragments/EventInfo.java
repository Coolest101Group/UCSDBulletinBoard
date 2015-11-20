package com.apress.gerber.ucsdbulletinboard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.ucsdbulletinboard.CreateEvent;
import com.apress.gerber.ucsdbulletinboard.MainActivity;
import com.apress.gerber.ucsdbulletinboard.R;
import com.apress.gerber.ucsdbulletinboard.myEvent;

/**
 * Created by danielmartin on 10/23/15.
 */
public class EventInfo extends Fragment {
    myEvent event;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mV = inflater.inflate(R.layout.evinfo, container, false);
        Bundle args = getArguments();
        int i = args.getInt("idEvent");
        event = MainActivity.eventArrayList.get(i);

        TextView eventT = (TextView) mV.findViewById(R.id.eventTitleInFragment);
        eventT.setText(event.getEventName());

        ImageView eventIm = (ImageView) mV.findViewById(R.id.eventImageInFragment);

        TextView eventDesc = (TextView) mV.findViewById(R.id.eventDescriptionInFragment);
        eventDesc.setText(event.getEventDesc());

        TextView eventTime = (TextView) mV.findViewById(R.id.eventTimeInFragment);
        eventTime.setText(event.getEventTime());

        return mV;
    }
}