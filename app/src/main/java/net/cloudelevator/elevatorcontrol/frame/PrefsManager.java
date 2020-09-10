package net.cloudelevator.elevatorcontrol.frame;

import android.content.Context;

public class PrefsManager {
    private Context mContext;
    private static final String PREFERENCES_NAME = "elevator_control";

    public PrefsManager(final Context context) {
        this.mContext = context;
    }

    public void clear() {
        mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public boolean contains() {
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).contains(PrefsManager.PREFERENCES_NAME);
    }

    public boolean getBoolean(final String key) {
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public boolean getBooleanDefaultTrue(final String key) {
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(key, true);
    }

    public void putBoolean(String key, boolean val) {
        mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, val).commit();
    }

    public void putInt(String key, int val) {
        mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(key, val).commit();
    }

    public int getInt(final String key) {
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(key, 0);
    }

    public void putFloat(String key, float val) {
        mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putFloat(key, val).commit();
    }

    public float getFloat(String key) {
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getFloat(key, 0.0f);
    }

    public void putLong(String key, long val) {
        mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putLong(key, val).commit();
    }

    public long getLong(String key) {
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME, Context.MODE_PRIVATE).getLong(key, 0);
    }

    public void putString(String key,String val){
        mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME,Context.MODE_PRIVATE).edit().putString(key,val).commit();
    }

    public String getString(String key){
        return mContext.getSharedPreferences(PrefsManager.PREFERENCES_NAME,Context.MODE_PRIVATE).getString(key,null);
    }

}
