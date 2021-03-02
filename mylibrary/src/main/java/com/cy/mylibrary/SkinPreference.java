package com.cy.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chuyang on 2021/3/2.
 */
public class SkinPreference {
    private static final String SKIN_SHARED = "skins";
    private static final String KEY_SKIN_PATH = "skin-path";
    private volatile static SkinPreference instance;
    private SharedPreferences mPref;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinPreference.class) {
                if (instance == null) {
                    instance = new SkinPreference(context);
                }
            }
        }
    }

    public static SkinPreference getInstance() {
        return instance;
    }

    private SkinPreference(Context context) {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    public void setSkin(String skinPath) {
        mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    public void reset() {
        mPref.edit().remove(KEY_SKIN_PATH).apply();
    }

    public String getSkin() {
        return mPref.getString(KEY_SKIN_PATH, null);
    }
}
