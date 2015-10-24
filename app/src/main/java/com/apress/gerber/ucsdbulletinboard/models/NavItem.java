package com.apress.gerber.ucsdbulletinboard.models;

/**
 * Created by danielmartin on 10/23/15.
 */
public class NavItem {
    private String mTitle;
    private String mSubtitle;
    private int mResIcon;

    public NavItem(String title, String subtitle, int resIcon) {
        mTitle = title;
        mSubtitle = subtitle;
        mResIcon = resIcon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        mSubtitle = subtitle;
    }

    public int getResIcon() {
        return mResIcon;
    }

    public void setResIcon(int resIcon) {
        mResIcon = resIcon;
    }
}
