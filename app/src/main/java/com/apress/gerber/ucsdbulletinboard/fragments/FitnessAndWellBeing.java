package com.apress.gerber.ucsdbulletinboard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apress.gerber.ucsdbulletinboard.CreateEvent;
import com.apress.gerber.ucsdbulletinboard.R;

/**
 * Created by danielmartin on 10/23/15.
 */
public class FitnessAndWellBeing extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mV = inflater.inflate(R.layout.fragment_fitnessandwellbeing, container, false);

        Button createEvent = (Button) mV.findViewById(R.id.create_event_page_button);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivity(intent);
            }
        });

        return mV;
    }
}
