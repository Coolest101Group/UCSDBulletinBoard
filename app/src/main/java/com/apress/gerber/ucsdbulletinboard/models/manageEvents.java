package com.apress.gerber.ucsdbulletinboard.models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Random;

import com.apress.gerber.ucsdbulletinboard.*;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * This class gets and pushes events to the database
 */
public class manageEvents {



    private Firebase db;
    String[] mEventNames;
    String[] mEventTimes;
    String[] mEventDescription;
    String[] mMasterList;

    public manageEvents(Firebase db){
        this.db = db;
    }

    public String[] getEventNames(){
        return mEventNames;
    }

    public String[] getEventTimes(){
        return mEventTimes;
    }

    public String[] getEventDescription(){
        return mEventDescription;
    }

    public String[] getMasterList(){
        return mMasterList;
    }

    /*
     *  Post an event to the db.  ALL parameters
     *  are strings!!!!
     *
     *  @param eventName - the name of the event
     *         eventTime - the time of the event
     *         eventDesc - the description of the event
     *
     *  @return Returns true if the operation completed succesfully
     */
    public boolean postEvent(String eventName, String eventTime, String eventDesc){

        // Generate random number
        Random randomGenerator = new Random();
        int eventNumber = randomGenerator.nextInt();

        // Create new myEvent object with the info
        myEvent event = new myEvent(eventName, eventTime, eventDesc);

        // Push event to db, the id of the event is the random number
        db.child("events").child(String.valueOf(eventNumber)).setValue(event);
        return true;
    }

    public boolean parseEvents(){
        Query queryDB = db.orderByChild("events");
        queryDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                myEvent singleEv = dataSnapshot.getValue(myEvent.class);

                // TODO: Add this event to a stack and maybe to arrays
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        })
        return true;
    }
}
