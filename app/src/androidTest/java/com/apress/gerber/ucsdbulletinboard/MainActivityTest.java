package com.apress.gerber.ucsdbulletinboard;
import  org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by danielmartin on 12/4/15.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest{

    StringBuilder n = new StringBuilder();
    Random ran = new Random();
    int rando = ran.nextInt();
    String mail = n.append("test").append(rando).append("@email.com").toString();

    @Rule public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);



    /*
     * Test
     */
    @Test
    public void testEverything(){


        // Create account
        onView(withId(R.id.create_new_account_button))
                .perform(click());
        onView(withId(R.id.firstName))
                .perform(typeText("FirstNameTest"), closeSoftKeyboard());
        onView(withId(R.id.lastName))
                .perform(typeText("LastNameTest"), closeSoftKeyboard());
        onView(withId(R.id.email))
                .perform(typeText(mail), closeSoftKeyboard());
        onView(withId(R.id.password1))
                .perform(typeText("Qwerty12345!"), closeSoftKeyboard());
        onView(withId(R.id.password2))
                .perform(typeText("Qwerty12345!"), closeSoftKeyboard());
        onView(withId(R.id.email_signup_button)).perform(click());


        for(int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; ){
            i++;
        }

        /*onView(withId(R.id.email))
                .perform(typeText("testtest@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("Qwerty12345!"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        */
        onView(withText("OK")).perform(click());

        // Login
        onView(withId(R.id.email))
                .perform(typeText("testtest@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("Qwerty12345!"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button))
                .perform(click());

        for(int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; ){
            i++;
        }
        onView(withText("OK")).perform(click());

        // Add an event
        onView(withId(R.id.add_new_event_png)).perform(click());






    }


}
