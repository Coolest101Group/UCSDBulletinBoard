package com.apress.gerber.ucsdbulletinboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.ucsdbulletinboard.MainActivity;
import com.apress.gerber.ucsdbulletinboard.R;
import com.firebase.client.Firebase;

/**
 * Created by danielmartin on 10/23/15.
 */
public class Login extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //trying out writing to firebase db

        //pull the database object from main activity
        Firebase databaseRef = ((MainActivity)getActivity()).getdb();

        //sending a test message to see if the database is receiving data
        databaseRef.child("message").setValue("Hey I clicked login! Am I sending data?");

        View mV = inflater.inflate(R.layout.fragment_login, container, false);
        return mV;
    }
}
