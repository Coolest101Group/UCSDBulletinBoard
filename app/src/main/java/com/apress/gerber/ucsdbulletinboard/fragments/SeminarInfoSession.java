package com.apress.gerber.ucsdbulletinboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.ucsdbulletinboard.R;

/**
 * Created by danielmartin on 10/23/15.
 */
public class SeminarInfoSession extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mV = inflater.inflate(R.layout.fragment_seminarinfosession, container, false);
        return mV;
    }
}
