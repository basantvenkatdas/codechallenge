package com.foo.umbrella.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gollaba on 9/17/17.
 */

public class AppData {

    private static AppData instance = null;
    private SharedPreferences prefs;
    private String metricsKey = "METRICS_KEY";
    private String zipCodeKey = "ZIP_KEY";


    private AppData(Context context) {
        prefs = context.getSharedPreferences(
                "umbrella", Context.MODE_PRIVATE);
    }

    public static AppData instance(Context context) {
        if(instance == null) {
            instance = new AppData(context);
        }
        return instance;
    }

    public int getMetricsSetting() {
        return prefs.getInt(metricsKey, 0);
    }

    public void updateMetricsSetting(int setting) {
        prefs.edit().putInt(metricsKey, setting).apply();
    }

    public String getZipCode() {
        return prefs.getString(zipCodeKey, "");
    }

    public void updateZipCode(String zipCode) {
        prefs.edit().putString(zipCodeKey, zipCode).apply();
    }

}
