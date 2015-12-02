package com.apress.gerber.ucsdbulletinboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.apress.gerber.ucsdbulletinboard.adapter.NavListAdapter;
import com.apress.gerber.ucsdbulletinboard.models.*;

import com.apress.gerber.ucsdbulletinboard.fragments.*;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends ActionBarActivity{

    ViewPager mViewPager;
    TabHost mTabHost;

    DrawerLayout mDrawerLayout;
    RelativeLayout mDrawerPane;
    public static ListView lvNav;

    public static List<NavItem> mNavItemList;
    public static List<Fragment> mFragmentList;

    ActionBarDrawerToggle mActionBarDrawerToggle;

    //our database object
    public static Firebase databaseRef;
    public static manageEvents mDB;
    public static Stack<myEvent> eventStack;
    public static ArrayList<myEvent> eventArrayList;

    public static boolean loggedIn = false; //logged in state
    public static boolean firstTimeLogin = true; //this is so we don't draw the new login shit twice

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup firebase database for our accounts and message storage
        Firebase.setAndroidContext(this);
        databaseRef = new Firebase("https://glaring-heat-815.firebaseio.com/");
        mDB = new manageEvents(databaseRef);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get DB events
        mDB.parseEvents();
        eventStack = mDB.getEventStack();
        eventArrayList = mDB.getMyEventArrayList();
        FeaturedEvents nn = new FeaturedEvents();




        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvNav = (ListView) findViewById(R.id.nav_list);

        mNavItemList = new ArrayList<NavItem>();
        mNavItemList.add(new NavItem("Login", " ", R.drawable.login17));
        mNavItemList.add(new NavItem("Featured Events", " ", R.drawable.event8));
        mNavItemList.add(new NavItem("Arts, Culture & More", " ", R.drawable.theater8));
        mNavItemList.add(new NavItem("Fitness and Wellbeing", " ", R.drawable.gym16));
        mNavItemList.add(new NavItem("Seminar/Info Sessions", " ", R.drawable.sciences));
        mNavItemList.add(new NavItem("Community Involvement", " ", R.drawable.network11));
        mNavItemList.add(new NavItem("Weekend Events", " ", R.drawable.fun1));


        Drawable drawerPanelBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_feather);
        drawerPanelBackground.setAlpha(150);

        NavListAdapter navListAdapter = new NavListAdapter(
                getApplicationContext(), R.layout.item_nav_list, mNavItemList);

        lvNav.setAdapter(navListAdapter);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new Login());
        mFragmentList.add(nn);
        //mFragmentList.add(new ArtsCultureAndMore());
        //mFragmentList.add(new FitnessAndWellBeing());
        //mFragmentList.add(new SeminarInfoSession());
        //mFragmentList.add(new CommunityInvolve());
        //mFragmentList.add(new WeekendEvents());

        /////// TEST CODE //////
        GenericFragment arts = new GenericFragment();
        Bundle bundle_art = new Bundle();
        bundle_art.putInt("cat", CreateEvent.ART);
        arts.setArguments(bundle_art);
        mFragmentList.add(arts);

        GenericFragment fitness = new GenericFragment();
        Bundle bundle_fit = new Bundle();
        bundle_fit.putInt("cat", CreateEvent.FITNESS);
        fitness.setArguments(bundle_fit);
        mFragmentList.add(fitness);

        GenericFragment sem = new GenericFragment();
        Bundle bundle_sem = new Bundle();
        bundle_sem.putInt("cat", CreateEvent.INFO);
        sem.setArguments(bundle_sem);
        mFragmentList.add(sem);

        GenericFragment com = new GenericFragment();
        Bundle bundle_com = new Bundle();
        bundle_com.putInt("cat", CreateEvent.COMINV);
        com.setArguments(bundle_com);
        mFragmentList.add(com);

        GenericFragment wk = new GenericFragment();
        Bundle bundle_wk = new Bundle();
        bundle_wk.putInt("cat", CreateEvent.WEEKEND);
        wk.setArguments(bundle_wk);
        mFragmentList.add(wk);
        ///////////////////////

        // Load Event fragment as default
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, mFragmentList.get(loggedIn ? 1 : 0)).addToBackStack("Login").commit();
        setTitle(mNavItemList.get(loggedIn ? 1 : 0).getTitle());
        lvNav.setItemChecked(loggedIn ? 2 : 0, true);
        mDrawerLayout.closeDrawer(mDrawerPane);

        // set listener for navigation items
        lvNav.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //logging in adds profile button so position gets adjusted
               if(loggedIn && position >= 2) position--;
               else if(loggedIn && position==1){
                   showProfile();
               }

                // replace fragment with one selected by user
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.main_content, mFragmentList.get(position))
                        .commit();
                setTitle(mNavItemList.get(loggedIn ? position+1 : position).getTitle());
                //theres one special case for setting the title when the user logs out
                if(loggedIn && position==0) setTitle("Login");
                lvNav.setItemChecked(loggedIn ? position+1 : position, true);
                mDrawerLayout.closeDrawer(mDrawerPane);

            }
        });

        //this gets called when you pop out of a fragment back to this main activity
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {

                        //after popping out of login for the first time, we're defaulting to featured events
                        if(loggedIn && firstTimeLogin) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.main_content, mFragmentList.get(1))
                                    .addToBackStack("").commit();
                            setTitle(mNavItemList.get(2).getTitle());
                            lvNav.setItemChecked(2, true);
                            mDrawerLayout.closeDrawer(mDrawerPane);

                            firstTimeLogin = false;
                        }
                    }
                });

        //create listener for drawer layout
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub

                //where i deleted that logout logic

                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

    }

    //get method for our databse to use in different fragments
    public Firebase getdb() {
        return databaseRef;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent newIntent;

        if(mActionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch(item.getItemId()) {
            case R.id.create_event:
                newIntent = new Intent(this, CreateEvent.class);
                this.startActivity(newIntent);
                break;
            case R.id.action_settings:
                break;
            case R.id.activity_profile:
                newIntent = new Intent(this, Profile.class);
                this.startActivity(newIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //TODO: read data sent from child fragment (firstname and lastname)
                String firstname = getIntent().getStringExtra("firstname");
                String lastname = getIntent().getStringExtra("lastname");
                TextView name = (TextView) findViewById(R.id.name_mainMenu);
                name.setText(firstname + " " + lastname);
                Log.d("name", firstname+lastname);
            }
        }
    }

    void showProfile() {
        Intent newIntent;
        newIntent = new Intent(this, Profile.class);
        this.startActivity(newIntent);
    }


}
