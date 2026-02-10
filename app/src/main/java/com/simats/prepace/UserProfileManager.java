package com.simats.prepace;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProfileManager {
    private static final String PREF_NAME = "PrepaceUserProfile";
    private static final String KEY_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_AVATAR_ID = "avatar_id";
    private static final String KEY_AVATAR_URI = "avatar_uri";
    
    // Settings Keys
    private static final String KEY_NOTIFICATIONS = "pref_notifications";
    private static final String KEY_DARK_MODE = "pref_dark_mode";
    private static final String KEY_SOUND = "pref_sound";
    private static final String KEY_LANGUAGE = "pref_language";
    private static final String KEY_TOTAL_QUIZZES = "total_quizzes"; // Cached for ease

    private SharedPreferences prefs;
    private static UserProfileManager instance;

    private UserProfileManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserProfileManager(context);
        }
        return instance;
    }

    // Profile Getters
    public String getName() { return prefs.getString(KEY_NAME, "John Doe"); }
    public String getEmail() { return prefs.getString(KEY_EMAIL, "hbh@gmail.com"); }
    public String getPhone() { return prefs.getString(KEY_PHONE, ""); }
    public int getAvatarId() { return prefs.getInt(KEY_AVATAR_ID, R.drawable.user_avatar); }
    public String getAvatarUri() { return prefs.getString(KEY_AVATAR_URI, null); }
    
    // Profile Setters
    public void saveProfile(String name, String email, String phone) {
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_PHONE, phone)
            .apply();
    }
    
    public void saveAvatarUri(String uriString) {
        prefs.edit().putString(KEY_AVATAR_URI, uriString).apply();
    }

    // Settings Getters
    public boolean isNotificationsEnabled() { return prefs.getBoolean(KEY_NOTIFICATIONS, true); }
    public boolean isDarkModeEnabled() { return prefs.getBoolean(KEY_DARK_MODE, false); }
    public boolean isSoundEnabled() { return prefs.getBoolean(KEY_SOUND, true); }
    public String getLanguage() { return prefs.getString(KEY_LANGUAGE, "English"); }

    // Settings Setters
    public void setNotificationsEnabled(boolean enabled) { prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply(); }
    public void setDarkModeEnabled(boolean enabled) { prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply(); }
    public void setSoundEnabled(boolean enabled) { prefs.edit().putBoolean(KEY_SOUND, enabled).apply(); }
    public void setLanguage(String language) { prefs.edit().putString(KEY_LANGUAGE, language).apply(); }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
