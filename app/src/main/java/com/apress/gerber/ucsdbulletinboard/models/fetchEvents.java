package com.apress.gerber.ucsdbulletinboard.models;

import com.firebase.client.Firebase;

/**
 * Created by danielmartin on 11/2/15.
 */
public class fetchEvents {

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
}
