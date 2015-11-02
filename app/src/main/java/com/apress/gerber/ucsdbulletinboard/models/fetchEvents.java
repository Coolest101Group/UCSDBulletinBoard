package com.apress.gerber.ucsdbulletinboard.models;

import com.firebase.client.Firebase;

import java.util.Random;

/**
 * Created by danielmartin on 11/2/15.
 */
public class fetchEvents {

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

    public fetchEvents (Firebase db){
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

    public boolean postEvent(String eventName, String eventTime, String eventDesc){
        Random randomGenerator = new Random();
        int eventNumber = randomGenerator.nextInt();

        myEvent event = new myEvent(eventName, eventTime, eventDesc);

        db.child("users").child(String.valueOf(eventNumber)).setValue(event);
        return true;
    }
}
