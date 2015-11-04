package com.apress.gerber.ucsdbulletinboard.models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import com.apress.gerber.ucsdbulletinboard.*;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * This class gets and pushes events to the database
 */
public class manageEvents {



    private Firebase db;
    Vector<String> mEventNames;
    Vector<String> mEventTimes;
    Vector<String> mEventDescription;
    Vector<String> mMasterList;
    Stack<myEvent> mEventStack;

    public manageEvents(Firebase db){
        this.db = db;
    }

    public Vector<String> getEventNames(){
        return mEventNames;
    }

    public Vector<String> getEventTimes(){
        return mEventTimes;
    }

    public Vector<String> getEventDescription(){
        return mEventDescription;
    }

    public Vector<String> getMasterList(){
        return mMasterList;
    }

    /*
     *  Post an event to the db.  ALL parameters
     *  are strings!!!!
     *
     *  @param eventName   the name of the event
     *  @param eventTime   the time of the event
     *  @param eventDesc   the description of the event
     *  @param day         the day of the event
     *  @param month       the month of the event
     *  @param year        the year of the event
     *
     *  @return Returns true if the operation completed succesfully
     */
    public boolean postEvent(String eventName, String eventTime, String eventDesc, int day, int month, int year){

        // Generate random number
        Random randomGenerator = new Random();
        int eventNumber = randomGenerator.nextInt();

        // Create new myEvent object with the info
        myEvent event = new myEvent(eventName, eventTime, eventDesc, day, month, year);

        // Push event to db, the id of the event is the random number
        Firebase eventRef = MainActivity.databaseRef.child("events").child(String.valueOf(eventNumber));
        eventRef.setValue(event);

        return true;
    }

    /*
     * Fetches the events from the database and adds them to a stack
     * and a list.
     */
    public boolean parseEvents(){

        Firebase ref = MainActivity.databaseRef.child("events");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    myEvent singleEvent = eventSnapshot.getValue(myEvent.class);
                    mEventStack.push(singleEvent);
                    mEventNames.add(singleEvent.getEventName());
                    mEventTimes.add(singleEvent.getEventTime());
                    mEventDescription.add(singleEvent.getEventDesc());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return true;
    }

    public Stack<myEvent> getEventStack() {
        return mEventStack;
    }
}
