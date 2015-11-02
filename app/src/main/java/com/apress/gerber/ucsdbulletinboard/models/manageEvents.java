package com.apress.gerber.ucsdbulletinboard.models;

import com.firebase.client.Firebase;

import java.util.Random;

/**
 * This class gets and pushes events to the database
 */
public class manageEvents {

    private class myEvent{
        private String mEventName;
        private String mEventTime;
        private String mEventDesc;

        public myEvent(){}

        public myEvent(String mEN, String mET, String mED){
            mEventName = mEN;
            mEventTime = mET;
            mEventDesc = mED;
        }
    }

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
}
