//DO NOT DELETE

package com.apress.gerber.ucsdbulletinboard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.ucsdbulletinboard.models.NavItem;
import com.apress.gerber.ucsdbulletinboard.*;

import java.util.List;

/**
 * Created by danielmartin on 10/23/15.
 */
public class NavListAdapter extends ArrayAdapter<NavItem>{

    Context mContext;
    int mResLayout;
    List<NavItem> mNavItemList;

    public NavListAdapter(Context context, int resource, List<NavItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResLayout = resource;
        mNavItemList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mV = View.inflate(mContext, mResLayout, null);
        TextView tvTitle = (TextView) mV.findViewById(R.id.title);
        TextView tvSubTitle = (TextView) mV.findViewById(R.id.subtitle);
        ImageView navIcon = (ImageView) mV.findViewById(R.id.nav_icon);
        NavItem navItem = mNavItemList.get(position);
        tvTitle.setText(navItem.getTitle());
        tvSubTitle.setText(navItem.getSubtitle());
        navIcon.setImageResource(navItem.getResIcon());

        return mV;
    }
}
