package com.apress.gerber.ucsdbulletinboard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
public class FitnessAndWellBeing extends Fragment {
    public static final int ART = 1;
    public static final int FITNESS = 2;
    public static final int INFO = 4;
    public static final int COMINV = 8;
    public static final int WEEKEND = 16;
    public static ListView navFE;
    public static List<NavItem> mNavItemList;
    public static List<Fragment> mFragmentList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mV = inflater.inflate(R.layout.fragment_fitnessandwellbeing, container, false);
        navFE = (ListView) mV.findViewById(R.id.nav_list_fe);

        /* createEvent = (Button) mV.findViewById(R.id.create_event_page_button);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivity(intent);
            }
        });*/
        ImageView addPNG = (ImageView) mV.findViewById(R.id.add_new_event_png);
        addPNG.setImageResource(R.drawable.add183);
        addPNG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the user is logged in
                if(MainActivity.loggedIn) {
                    Intent intent = new Intent(getActivity(), CreateEvent.class);
                    startActivity(intent);
                }
                //if they are not logged in, give em hell
                else {
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
            if(CreateEvent.checkIndividualEventCategory(tmpEvent,FITNESS)){
                String time = tmpEvent.getEventTime() + " at " + tmpEvent.getHour() + ":" + tmpEvent.getMinute();
                mNavItemList.add(new NavItem(tmpEvent.getEventName(), time, R.drawable.event8));
            }

        }
        NavListAdapter navListAdapter = new NavListAdapter(
                getActivity().getApplicationContext(), R.layout.item_nav_list, mNavItemList);


        navFE.setAdapter(navListAdapter);


        mFragmentList = new ArrayList<Fragment>();
        for(int i = 0; i < eventArrayList.size(); i++){
            tmpEvent = eventArrayList.get(i);
            if(CreateEvent.checkIndividualEventCategory(tmpEvent,FITNESS)){
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
