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

    public static void saveQuizResult(Context context, String userId, QuizResult result) {
        List<QuizResult> history = getQuizResults(context, userId);
        history.add(0, result); // Add to top

        // Convert back to JSON and save
        saveList(context, userId, history);
    }

    public static List<QuizResult> getQuizResults(Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = userId != null ? KEY_HISTORY + "_" + userId : KEY_HISTORY;
        String jsonString = prefs.getString(key, "[]");
        List<QuizResult> results = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                
                List<com.simats.prepace.model.Question> questions = new ArrayList<>();
                if (obj.has("questions")) {
                    JSONArray questionsArray = obj.getJSONArray("questions");
                    for (int j = 0; j < questionsArray.length(); j++) {
                        JSONObject qObj = questionsArray.getJSONObject(j);
                        com.simats.prepace.model.Question q = new com.simats.prepace.model.Question(
                            qObj.getString("questionText"),
                            qObj.optString("optionA", ""),
                            qObj.optString("optionB", ""),
                            qObj.optString("optionC", ""),
                            qObj.optString("optionD", ""),
                            qObj.optInt("correctAnswerIndex", 0),
                            qObj.optString("explanation", "")
                        );
                        q.setUserAnswerIndex(qObj.optInt("userAnswerIndex", -1));
                        questions.add(q);
                    }
                }

                results.add(new QuizResult(
                        obj.getString("category"),
                        obj.getString("quizTitle"),
                        obj.getInt("score"),
                        obj.getInt("totalQuestions"),
                        obj.getLong("timestamp"),
                        questions
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static int getUniqueCompletedQuizCount(Context context, String userId, String category) {
        List<QuizResult> results = getQuizResults(context, userId);
        java.util.Set<String> uniqueTitles = new java.util.HashSet<>();
        for (QuizResult res : results) {
            if (res.getCategory().equalsIgnoreCase(category)) {
                uniqueTitles.add(res.getQuizTitle());
            }
        }
        return uniqueTitles.size();
    }

    private static void saveList(Context context, String userId, List<QuizResult> list) {
        JSONArray jsonArray = new JSONArray();
        for (QuizResult res : list) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("category", res.getCategory());
                obj.put("quizTitle", res.getQuizTitle());
                obj.put("score", res.getScore());
                obj.put("totalQuestions", res.getTotalQuestions());
                obj.put("timestamp", res.getTimestamp());
                
                if (res.getQuestions() != null) {
                    JSONArray questionsArray = new JSONArray();
                    for (com.simats.prepace.model.Question q : res.getQuestions()) {
                        JSONObject qObj = new JSONObject();
                        qObj.put("questionText", q.getQuestionText());
                        qObj.put("optionA", q.getOptionA());
                        qObj.put("optionB", q.getOptionB());
                        qObj.put("optionC", q.getOptionC());
                        qObj.put("optionD", q.getOptionD());
                        qObj.put("correctAnswerIndex", q.getCorrectAnswerIndex());
                        qObj.put("userAnswerIndex", q.getUserAnswerIndex());
                        qObj.put("explanation", q.getExplanation());
                        questionsArray.put(qObj);
                    }
                    obj.put("questions", questionsArray);
                }
                
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = userId != null ? KEY_HISTORY + "_" + userId : KEY_HISTORY;
        prefs.edit().putString(key, jsonArray.toString()).apply();
    }
}
