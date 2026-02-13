package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.simats.prepace.model.QuizResult;
import com.simats.prepace.utils.QuizHistoryManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceActivity extends AppCompatActivity {

    private LinearLayout containerCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        containerCategories = findViewById(R.id.containerCategories);

        loadPerformanceData();
    }

    private void loadPerformanceData() {
        String userId = com.simats.prepace.UserProfileManager.getInstance(this).getUserId();
        List<QuizResult> history = QuizHistoryManager.getQuizResults(this, userId);
        List<String> categories = new ArrayList<>();
        categories.add("Aptitude");
        categories.add("Reasoning");
        categories.add("Technical");


        // Prepare Data Calculation
        // Map: Category -> List of Scores
        Map<String, List<Integer>> categoryScores = new HashMap<>();
        Map<String, Integer> categoryAttempts = new HashMap<>();

        for (String cat : categories) {
            categoryScores.put(cat, new ArrayList<>());
            categoryAttempts.put(cat, 0);
        }

        // Aggregate from history
        for (QuizResult res : history) {
            String cat = res.getCategory();
            // Normalize category usage if needed (handle possible typos or variations if any)
            // Just basic matching for now
            if (categoryScores.containsKey(cat)) {
                int qTotal = res.getTotalQuestions() > 0 ? res.getTotalQuestions() : 1;
                int scorePercent = (int) (((float) res.getScore() / qTotal) * 100);
                categoryScores.get(cat).add(scorePercent);
                categoryAttempts.put(cat, categoryAttempts.get(cat) + 1);
            }
        }

        // Render Cards
        for (String cat : categories) {
            addCategoryCard(cat, categoryScores.get(cat), categoryAttempts.get(cat));
        }
    }

    private void addCategoryCard(String categoryName, List<Integer> scores, int attempts) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_performance_card, containerCategories, false);

        TextView tvCategoryName = cardView.findViewById(R.id.tvCategoryName);
        TextView tvAvgScorePercent = cardView.findViewById(R.id.tvAvgScorePercent);
        TextView tvQuizCount = cardView.findViewById(R.id.tvQuizCount);
        TextView tvScoreProgressVal = cardView.findViewById(R.id.tvScoreProgressVal);
        ProgressBar progressScore = cardView.findViewById(R.id.progressScore);
        TextView tvCompletionVal = cardView.findViewById(R.id.tvCompletionVal);
        ProgressBar progressCompletion = cardView.findViewById(R.id.progressCompletion);
        Button btnPractice = cardView.findViewById(R.id.btnPractice);

        // Calc Stats
        int avgScore = 0;
        if (!scores.isEmpty()) {
            long sum = 0;
            for (int s : scores) sum += s;
            avgScore = (int) (sum / scores.size());
        }

        // Hardcoded total quizzes per category for demo "Completion" stat
        // In a real app this would come from a database of total available quizzes
        int totalAvailable = 20; 
        if (categoryName.equals("Aptitude")) totalAvailable = 12;
        if (categoryName.equals("Reasoning")) totalAvailable = 15;
        
        int completionPercent = (int) (((float) attempts / totalAvailable) * 100);
        if (completionPercent > 100) completionPercent = 100;

        // Set Text
        tvCategoryName.setText(categoryName);
        tvAvgScorePercent.setText(avgScore + "%");
        tvScoreProgressVal.setText(avgScore + "%");
        tvQuizCount.setText(attempts + " of " + totalAvailable + " quizzes attempted");
        
        // Set Progress
        progressScore.setProgress(avgScore);
        // Change progress color based on avg score? (Optional, kept default/green for now)
        if (avgScore >= 80) {
            progressScore.setProgressTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#00C853"))); // Green
        } else if (avgScore >= 50) {
             progressScore.setProgressTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2979FF"))); // Blue
        } else {
             progressScore.setProgressTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFAB00"))); // Orange
        }

        tvCompletionVal.setText(completionPercent + "%");
        progressCompletion.setProgress(completionPercent);

        // Button Action
        btnPractice.setOnClickListener(v -> {
            Intent intent = null;
            if (categoryName.equals("Aptitude")) intent = new Intent(this, com.simats.prepace.CategoryDetailActivity.class); // Assuming this is Aptitude
            else if (categoryName.equals("Reasoning")) intent = new Intent(this, com.simats.prepace.ReasoningCategoryActivity.class);
            else if (categoryName.equals("Technical")) intent = new Intent(this, com.simats.prepace.TechnicalCategoryActivity.class);
            else intent = new Intent(this, com.simats.prepace.CategoriesActivity.class); // General fallback
            
            if (intent != null) startActivity(intent);
        });

        containerCategories.addView(cardView);
    }
}
