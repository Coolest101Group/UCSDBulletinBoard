package com.apress.gerber.ucsdbulletinboard;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.widget.TextView;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private CreateEvent createEventActivity;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //createEventActivity = new CreateEvent();

        //TODO: Set up test cases by using findViewById to instantiate activity views, and run tests to see if they're functioning properly.
        //Example:
        //createEventActivity.findViewById(R.id.name_mainMenu);
        //Do something with the name textview to see if it works
    }

}