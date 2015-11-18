package com.apress.gerber.ucsdbulletinboard;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        NavListAdapter navListAdapter = new NavListAdapter(
                getApplicationContext(), R.layout.item_nav_list, mNavItemList);

        lvNav.setAdapter(navListAdapter);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new Login());
        mFragmentList.add(new FeaturedEvents());
        mFragmentList.add(new ArtsCultureAndMore());
        mFragmentList.add(new FitnessAndWellBeing());
        mFragmentList.add(new SeminarInfoSession());
        mFragmentList.add(new CommunityInvolve());
        mFragmentList.add(new WeekendEvents());

        // Load Event fragemnt as default
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, mFragmentList.get(1))
                .commit();
        setTitle(mNavItemList.get(1).getTitle());
        lvNav.setItemChecked(1, true);
        mDrawerLayout.closeDrawer(mDrawerPane);

        // set listener for navigation items
        lvNav.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //this is a little hacky but lets see if it works
               if(loggedIn && position == 1){ //position 1 should be the manage account button
                  activitySwitcher(1);
                }

                // replace fragment with one selected by user
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.main_content, mFragmentList.get(position))
                        .commit();
                setTitle(mNavItemList.get(position).getTitle());
                lvNav.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerPane);

            }
        });

        //this gets called when you pop out of a fragment back to this main activity
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {


                        setTitle("Featured Events");

                        //first time login work. adds logout and manage account buttons
                        if (loggedIn && firstTimeLogin) {
                            System.out.println("This logged in IF statement is running");
                            mNavItemList.get(0).setTitle("Logout");
                            mNavItemList.get(0).setResIcon(R.drawable.logout);

                            mNavItemList.add(1, new NavItem("Manage Account", " ", R.drawable.mng_account));


                            NavListAdapter navListAdapter = new NavListAdapter(
                                    getApplicationContext(), R.layout.item_nav_list, mNavItemList);

                            lvNav.setAdapter(navListAdapter);
                            firstTimeLogin = false; //now its no longer the first time
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

    void activitySwitcher(int itemSelected){
        Intent newIntent;
        if(itemSelected == 1) {
            newIntent = new Intent(this, Profile.class);
            this.startActivity(newIntent);
        }

    }


}
