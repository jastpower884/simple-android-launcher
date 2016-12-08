package org.jast.simpleandroidlauncher;

import android.graphics.drawable.Drawable;

/**
 * Created by ptc_02008 on 2016/11/4.
 */

public class AppDetail {


    public static final int TYPE_SETTING_APP = 0x000001;
    public static final int TYPE_OTHER_APP = 0x000002;


    CharSequence label;
    CharSequence name;
    Drawable icon;

    int appType;


    public CharSequence getLabel() {
        return label;
    }

    public CharSequence getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }


    public int getAppType() {
        return appType;
    }
}
