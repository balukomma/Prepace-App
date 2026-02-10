package com.simats.prepace.utils;

import com.simats.prepace.model.Quiz;
import java.util.ArrayList;
import java.util.List;

public class QuizDataManager {

    public static List<Quiz> getQuizzesForCategory(android.content.Context context, String category) {
        List<Quiz> quizList = new ArrayList<>();
        
        try {
            // Read JSON from Assets
            java.io.InputStream is = context.getAssets().open("quiz_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            // Parse JSON
            org.json.JSONObject obj = new org.json.JSONObject(json);
            org.json.JSONArray categories = obj.getJSONArray("categories");

            for (int i = 0; i < categories.length(); i++) {
                org.json.JSONObject catObj = categories.getJSONObject(i);
                if (catObj.getString("name").equalsIgnoreCase(category)) {
                    org.json.JSONArray quizzes = catObj.getJSONArray("quizzes");
                    for (int j = 0; j < quizzes.length(); j++) {
                        org.json.JSONObject quizObj = quizzes.getJSONObject(j);
                        Quiz quiz = new Quiz(
                            quizObj.getString("title"),
                            quizObj.getString("difficulty"),
                            quizObj.getString("description"),
                            quizObj.getJSONArray("questions").length(), // Count questions dynamically
                            quizObj.getInt("time_limit"),
                            category
                        );
                        quizList.add(quiz);
                    }
                    break; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return quizList;
    }

    public static int getTotalQuestions(android.content.Context context, String category) {
        List<Quiz> quizzes = getQuizzesForCategory(context, category);
        int total = 0;
        for (Quiz q : quizzes) {
            total += q.getQuestions();
        }
        return total;
    }
}
