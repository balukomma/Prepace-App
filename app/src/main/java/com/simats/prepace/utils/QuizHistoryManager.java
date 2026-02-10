package com.simats.prepace.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.simats.prepace.model.QuizResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuizHistoryManager {
    private static final String PREF_NAME = "QuizHistoryPrefs";
    private static final String KEY_HISTORY = "quiz_history_json";

    public static void saveQuizResult(Context context, QuizResult result) {
        List<QuizResult> history = getQuizResults(context);
        history.add(0, result); // Add to top

        // Convert back to JSON and save
        saveList(context, history);
    }

    public static List<QuizResult> getQuizResults(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(KEY_HISTORY, "[]");
        List<QuizResult> results = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                results.add(new QuizResult(
                        obj.getString("category"),
                        obj.getString("quizTitle"),
                        obj.getInt("score"),
                        obj.getInt("totalQuestions"),
                        obj.getLong("timestamp")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getUniqueCompletedQuizCount(Context context, String category) {
        List<QuizResult> results = getQuizResults(context);
        java.util.Set<String> uniqueTitles = new java.util.HashSet<>();
        for (QuizResult res : results) {
            if (res.getCategory().equalsIgnoreCase(category)) {
                uniqueTitles.add(res.getQuizTitle());
            }
        }
        return uniqueTitles.size();
    }

    private static void saveList(Context context, List<QuizResult> list) {
        JSONArray jsonArray = new JSONArray();
        for (QuizResult res : list) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("category", res.getCategory());
                obj.put("quizTitle", res.getQuizTitle());
                obj.put("score", res.getScore());
                obj.put("totalQuestions", res.getTotalQuestions());
                obj.put("timestamp", res.getTimestamp());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_HISTORY, jsonArray.toString()).apply();
    }
}
