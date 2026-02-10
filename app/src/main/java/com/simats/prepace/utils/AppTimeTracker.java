package com.simats.prepace.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppTimeTracker {

    private static final String PREF_NAME = "app_stats";
    private static final String KEY_TOTAL_TIME = "total_time_ms";

    private static long startTime = 0;

    public static void startTracking() {
        startTime = System.currentTimeMillis();
    }

    public static void stopTracking(Context context) {
        if (startTime == 0) return;

        long endTime = System.currentTimeMillis();
        long sessionTime = endTime - startTime;

        if (sessionTime > 0) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            long totalTime = prefs.getLong(KEY_TOTAL_TIME, 0);
            prefs.edit().putLong(KEY_TOTAL_TIME, totalTime + sessionTime).apply();
        }
        startTime = 0;
    }

    public static long getTotalTimeMs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long storedTime = prefs.getLong(KEY_TOTAL_TIME, 0);
        
        // Add current session time if tracking is active
        if (startTime > 0) {
            storedTime += (System.currentTimeMillis() - startTime);
        }
        
        return storedTime;
    }

    public static String getFormattedTotalTime(Context context) {
        long totalTimeMs = getTotalTimeMs(context);
        long totalMinutes = totalTimeMs / (1000 * 60);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }
}
