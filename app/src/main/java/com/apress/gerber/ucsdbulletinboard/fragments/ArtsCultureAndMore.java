package com.apress.gerber.ucsdbulletinboard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.apress.gerber.ucsdbulletinboard.CreateEvent;
import com.apress.gerber.ucsdbulletinboard.MainActivity;
import com.apress.gerber.ucsdbulletinboard.R;
import com.apress.gerber.ucsdbulletinboard.adapter.NavListAdapter;
import com.apress.gerber.ucsdbulletinboard.models.NavItem;
import com.apress.gerber.ucsdbulletinboard.myEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielmartin on 10/23/15.
 */
public class ArtsCultureAndMore extends Fragment {
    public static final int ART = 1;
    public static final int FITNESS = 2;
    public static final int INFO = 4;
    public static final int COMINV = 8;
    public static final int WEEKEND = 16;
    public static ListView navFE;
    public static List<NavItem> mNavItemList;
    public static List<Fragment> mFragmentList;
    String ce = "CreateEvent";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View mV = inflater.inflate(R.layout.fragment_artscultureandmore, container, false);
        navFE = (ListView) mV.findViewById(R.id.nav_list_fe);
        if(navFE == null) Log.i(ce, "navFE is null");

        ImageView addPNG = (ImageView) mV.findViewById(R.id.add_new_event_png);
        addPNG.setImageResource(R.drawable.add183);
        addPNG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the user is logged in
                if(MainActivity.loggedIn) {
                    System.out.println("logged in = true ran in art");
                    Intent intent = new Intent(getActivity(), CreateEvent.class);
                    startActivity(intent);
                }
                //if they are not logged in, give em hell
                else {
                    System.out.println("logged in = false ran in art");
                    new AlertDialog.Builder(getContext())
                            .setMessage("You must be logged in to create an event")
                            .setPositiveButton("Go To Login", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            getFragmentManager().popBackStack("Login",0);
                                            getActivity().setTitle("Login");
                                        }
                                    }
                            )
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }
                            )
                            .create()
                            .show();
                }
            }
        });
        populateList();

        return mV;
    }

    public void populateList(){
        // Create listview of events

        mNavItemList = new ArrayList<NavItem>();
        myEvent tmpEvent;
        ArrayList<myEvent> eventArrayList = MainActivity.eventArrayList;


        for (int i = 0; i < eventArrayList.size(); i++){
            tmpEvent = eventArrayList.get(i);
            Log.i("CreateEvent", "master array NOT EMPTY");
            if(CreateEvent.checkIndividualEventCategory(tmpEvent,ART)){
                String time = tmpEvent.getEventTime() + " at " + tmpEvent.getHour() + ":" + tmpEvent.getMinute();
                mNavItemList.add(new NavItem(tmpEvent.getEventName(), time, R.drawable.event8));
                Log.i("CreateEvent", "Arts array NOT EMPTY");
            }

        }
        NavListAdapter navListAdapter = new NavListAdapter(
                getActivity().getApplicationContext(), R.layout.item_nav_list, mNavItemList);


        if(navFE == null) Log.i(ce, "navFE is null");
        else if(navListAdapter == null) Log.i(ce, "Adapter is null");
        else Log.i(ce, "Neither navFE nor the adapter are null");
        navFE.setAdapter(navListAdapter);


        mFragmentList = new ArrayList<Fragment>();
        for(int i = 0; i < eventArrayList.size(); i++){
            tmpEvent = eventArrayList.get(i);

            if(CreateEvent.checkIndividualEventCategory(tmpEvent,ART)){
                Bundle bundle = new Bundle();
                bundle.putInt("idEvent", i);
                Fragment newF = new EventInfo();
                newF.setArguments(bundle);

                mFragmentList.add(newF);
            }

        }

        // set listener for navigation items
        navFE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.main_content, mFragmentList.get(position))
                        .commit();
                //setTitle(mNavItemList.get(position).getTitle());
                //navFE.setItemChecked(position, true);
            }
        });

        navListAdapter.notifyDataSetChanged();
    }

}
