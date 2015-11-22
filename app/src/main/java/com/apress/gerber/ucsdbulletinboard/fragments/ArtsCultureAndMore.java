package com.apress.gerber.ucsdbulletinboard.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.apress.gerber.ucsdbulletinboard.CreateEvent;
import com.apress.gerber.ucsdbulletinboard.R;

/**
 * Created by danielmartin on 10/23/15.
 */
public class ArtsCultureAndMore extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View mV = inflater.inflate(R.layout.fragment_artscultureandmore, container, false);

        ImageView img = (ImageView) mV.findViewById(R.id.animation);
        img.setBackgroundResource(R.drawable.animationconcert);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();


        ImageView addPNG = (ImageView) mV.findViewById(R.id.add_new_event_png);
        addPNG.setImageResource(R.drawable.add182);
        addPNG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivity(intent);
            }
        });
        /*
        Button createEvent = (Button) mV.findViewById(R.id.create_event_page_button);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivity(intent);
            }
        });*/

        return mV;
    }

}
