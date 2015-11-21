package com.apress.gerber.ucsdbulletinboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by danielmartin on 11/3/15.
 */
public class myEvent {

    private String eventName;
    private String eventTime;
    private String eventDesc;
    private int day;
    private int month;
    private int year;
    private int minute;
    private int hour;
    private int integerDate;
    private String image;
    private int cat;

    public myEvent(){}

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public myEvent(int category, String mEN, String mET, String mED, int day, int month, int year, int hr, int mn, String image){
        eventName = mEN;
        eventTime = mET;
        eventDesc = mED;
        this.day = day;
        this.month = month;
        this.year = year;
        this.image = image;
        this.minute = mn;
        this.hour = hr;
        cat = category;
        String tt;


        if (day < 10){
            tt = new StringBuilder().append(year).append(month).append("0").append(day).toString();


        }else{
            tt = new StringBuilder().append(year).append(month).append(day).toString();
        }
        integerDate = Integer.parseInt(tt);
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventDesc() {
        return eventDesc;
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

    public String getImage() { return image;}

    public static Bitmap getImageBitmap(String input){
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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

    public int getCat(){
        return cat;
    }





}
