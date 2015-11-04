package com.apress.gerber.ucsdbulletinboard;

/**
 * Created by danielmartin on 11/3/15.
 */
public class myEvent {

    private String mEventName;
    private String mEventTime;
    private String mEventDesc;
    private int day;
    private int month;
    private int year;
    private int integerDate;

    public myEvent(){}

    public myEvent(String mEN, String mET, String mED, int day, int month, int year){
        mEventName = mEN;
        mEventTime = mET;
        mEventDesc = mED;
        this.day = day;
        this.month = month;
        this.year = year;

        String tt = new StringBuilder().append(year).append(month).append(day).toString();
        integerDate = Integer.parseInt(tt);
    }

    public String getEventName() {
        return mEventName;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public String getEventDesc() {
        return mEventDesc;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getIntegerDate() {
        return integerDate;
    }






}
