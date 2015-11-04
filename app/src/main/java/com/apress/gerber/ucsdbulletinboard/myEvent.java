package com.apress.gerber.ucsdbulletinboard;

/**
 * Created by danielmartin on 11/3/15.
 */
public class myEvent {

    public String getEventName() {
        return mEventName;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public String getEventDesc() {
        return mEventDesc;
    }

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
