package com.apress.gerber.ucsdbulletinboard.models;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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
    ArrayList<myEvent> myEventArrayList;

    public manageEvents(Firebase db){
        this.db = db;
        mEventStack = new Stack<myEvent>();
        myEventArrayList = new ArrayList<myEvent>();
        mEventDescription = new Vector<String>();
        mEventNames = new Vector<String>();
        mEventTimes = new Vector<String>();
        mMasterList = new Vector<String>();
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
    public boolean postEvent(int category, String eventName, String eventTime, String eventDesc, int day,
                             int month, int year, int hour, int min){

        // Generate random number
        Random randomGenerator = new Random();
        int eventNumber = randomGenerator.nextInt();

        // Create new myEvent object with the info
        myEvent event = new myEvent(category, eventName, eventTime, eventDesc, day, month, year, hour, min, "null");

        // Push event to db, the id of the event is the random number
        Firebase eventRef = MainActivity.databaseRef.child("events").child(String.valueOf(eventNumber));
        eventRef.setValue(event);

        return true;
    }
    public boolean postEvent(int category, String eventName, String eventTime, String eventDesc, int day,
                             int month, int year, int hour, int min, Bitmap image){

        // Encode image to string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        // Generate random number
        Random randomGenerator = new Random();
        int eventNumber = randomGenerator.nextInt();

        // Create new myEvent object with the info
        myEvent event = new myEvent(category, eventName, eventTime, eventDesc, day, month, year, hour, min, encodedImage);

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
        mEventStack = new Stack<myEvent>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("CreateEvent", "There are " + dataSnapshot.getChildrenCount() + " events");
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                    myEvent singleEvent = eventSnapshot.getValue(myEvent.class);
                    Log.i("CreateEvent", "The title is "  + singleEvent.getEventName());
                    //Log.i("CreateEvent", "The description is" + singleEvent.getEventName());
                    mEventStack.push(singleEvent);
                    myEventArrayList.add(singleEvent);
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
    public static String getTimeString(int hour, int minute){
        String time;
        int nH;

        if (hour == 12){
            time = "pm";
            nH = 12;
        }
        else if(hour > 12){
            time = "pm";
            nH = hour - 12;
        }
        else{
            time = "am";
            nH = hour;
        }
        return new StringBuilder().append(nH).append(":").append(minute)
                .append(" ").append(time).toString();

    }
    public ArrayList<myEvent> getMyEventArrayList(){return myEventArrayList;}
}
