package com.sprd.simple.model;

import android.graphics.drawable.Drawable;


/**
 * Created by SPRD on 2016/7/19.
 */
public class IconInfo {
    private String mIconName;
    private int mIconImage;
    private int mBackgroud;
    private Drawable mIconDrawable;

    public String getIconName() {
        return mIconName;
    }

    public void setIconName(String iconName) {
        mIconName = iconName;
    }

    public int getIconImage() {
        return mIconImage;
    }

    public void setIconImage(int iconImage) {
        mIconImage = iconImage;
    }

    public int getBackgroud() {
        return mBackgroud;
    }

    public void setBackgroud(int backgroud) {
        mBackgroud = backgroud;
    }

    public Drawable getmIconDrawable() {
        return mIconDrawable;
    }

    public void setmIconDrawable(Drawable mIconDrawable) {
        this.mIconDrawable = mIconDrawable;
    }
}
