package com.simats.prepace.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.simats.prepace.model.Notification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static final String PREF_NAME = "NotificationPrefs";
    private static final String KEY_NOTIFICATIONS = "notifications_json";

    public static void addNotification(Context context, Notification notification) {
        List<Notification> notifications = getNotifications(context);
        notifications.add(0, notification); // Add to top
        saveNotifications(context, notifications);
    }

    public static List<Notification> getNotifications(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(KEY_NOTIFICATIONS, "[]");
        List<Notification> notifications = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Notification.Type type = Notification.Type.valueOf(obj.getString("type"));
                notifications.add(new Notification(
                        obj.getString("title"),
                        obj.getString("message"),
                        obj.getString("timestamp"),
                        type,
                        obj.getBoolean("isUnread")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    private static void saveNotifications(Context context, List<Notification> notifications) {
        JSONArray jsonArray = new JSONArray();
        for (Notification n : notifications) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("title", n.getTitle());
                obj.put("message", n.getMessage());
                obj.put("timestamp", n.getTimestamp());
                obj.put("type", n.getType().name());
                obj.put("isUnread", n.isUnread());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_NOTIFICATIONS, jsonArray.toString()).apply();
    }
    
    // Initial dummy data if empty (Optional, but good for demo)
    public static void checkAndAddDummyData(Context context) {
        if (getNotifications(context).isEmpty()) {
            addNotification(context, new Notification("New Quizzes Added", "5 new quizzes have been added to the Science category", "5 hours ago", Notification.Type.NEW_QUIZZES, false));
            addNotification(context, new Notification("Daily Challenge Available", "A new daily challenge is waiting for you. Complete it to earn bonus points!", "3 hours ago", Notification.Type.DAILY_CHALLENGE, false));
        }
    }
}
