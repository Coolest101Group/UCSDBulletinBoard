package com.apress.gerber.ucsdbulletinboard;

import android.content.Context;
import android.content.Intent;
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
    ListView lvNav;

    List<NavItem> mNavItemList;
    List<Fragment> mFragmentList;

    ActionBarDrawerToggle mActionBarDrawerToggle;

    //our database object
    public static Firebase databaseRef;
    public static manageEvents mDB;

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

        // Load Login fragemnt as default
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

                // replace fragment with one seleced by user
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content, mFragmentList.get(position))
                        .commit();
                setTitle(mNavItemList.get(position).getTitle());
                lvNav.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerPane);

            }
        });

        //create listener for drawer layout
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
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

        if(mActionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch(item.getItemId()) {
            case R.id.create_event:
                Intent intent = new Intent(this, CreateEvent.class);
                this.startActivity(intent);
                break;
            case R.id.action_settings:
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
}
