package com.simats.prepace.utils;

import com.simats.prepace.model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuizDataProvider {

    public static List<Question> getQuestions(android.content.Context context, String category, String quizTitle) {
        List<Question> questions = new ArrayList<>();

        if (category == null || quizTitle == null) return questions;

        try {
            // Read JSON
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
                        if (quizObj.getString("title").equalsIgnoreCase(quizTitle)) {
                            org.json.JSONArray qArray = quizObj.getJSONArray("questions");
                            for (int k = 0; k < qArray.length(); k++) {
                                org.json.JSONObject qObj = qArray.getJSONObject(k);
                                Question q = new Question(
                                    qObj.getString("questionText"),
                                    qObj.getString("optionA"),
                                    qObj.getString("optionB"),
                                    qObj.getString("optionC"),
                                    qObj.getString("optionD"),
                                    qObj.getInt("correctAnswerIndex"),
                                    qObj.optString("explanation", "Explanation coming soon.")
                                );
                                questions.add(q);
                            }
                            return questions; // Found the quiz
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If no questions found (or error), fallback or return empty
        if (questions.isEmpty()) {
             // Optional: Add dummy if needed, but better to show empty.
             // questions.add(new Question("Sample Question?", "A", "B", "C", "D", 0, "Sample Explanation."));
        }

        return questions;
    }
}
