package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // Get Data
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("total_questions", 10);
        
        int wrongAnswers = totalQuestions - score;
        int accuracy = (int) (((float) score / totalQuestions) * 100);
        
        // Save Result
        String category = getIntent().getStringExtra("category_title");
        String quizTitle = getIntent().getStringExtra("quiz_title");
        java.util.List<com.simats.prepace.model.Question> qList = (java.util.List<com.simats.prepace.model.Question>) getIntent().getSerializableExtra("question_list");
        
        // Simple null check fallback
        if (category == null) category = "General";
        if (quizTitle == null) quizTitle = "Quiz";
        if (qList == null) qList = new java.util.ArrayList<>();
        
        com.simats.prepace.model.QuizResult result = new com.simats.prepace.model.QuizResult(
            category, quizTitle, score, totalQuestions, System.currentTimeMillis(), qList
        );
        String userId = com.simats.prepace.UserProfileManager.getInstance(this).getUserId();
        com.simats.prepace.utils.QuizHistoryManager.saveQuizResult(this, userId, result);

        // UI References
        TextView tvScorePercentage = findViewById(R.id.tvScorePercentage);
        ProgressBar progressScore = findViewById(R.id.progressScore);
        TextView tvCorrectCount = findViewById(R.id.tvCorrectCount);
        TextView tvWrongCount = findViewById(R.id.tvWrongCount);
        TextView tvAccuracy = findViewById(R.id.tvAccuracy);

        // Sound Effect
        if (com.simats.prepace.UserProfileManager.getInstance(this).isSoundEnabled()) {
             try {
                android.media.MediaPlayer mediaPlayer = android.media.MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> mp.release());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Update UI
        tvScorePercentage.setText(accuracy + "%");
        progressScore.setProgress(accuracy); // Assuming max is 100
        tvCorrectCount.setText(String.valueOf(score));
        tvWrongCount.setText(String.valueOf(wrongAnswers));
        tvAccuracy.setText(accuracy + "%");
        
        // --- NOTIFICATION LOGIC ---
        
        // 1. Quiz Completed Notification
        String timeAgo = "Just now"; 
        com.simats.prepace.utils.NotificationManager.addNotification(this, new com.simats.prepace.model.Notification(
            "Quiz Completed!",
            "Great job! You scored " + accuracy + "% on \"" + quizTitle + "\"",
            timeAgo,
            com.simats.prepace.model.Notification.Type.QUIZ_COMPLETED,
            true
        ));
        
        // 2. High Score / Personal Best
        // Check previous best
        java.util.List<com.simats.prepace.model.QuizResult> history = com.simats.prepace.utils.QuizHistoryManager.getQuizResults(this, userId);
        int currentBest = 0;
        // The current result is already saved at index 0, so we check from index 1 downwards or filter
        // Actually we just saved the result in onCreate before this block.
        // Let's check if there was a previous attempt with this quiz title and category that had a lower score.
        // OR simply: if this is the highest score for this quiz.
        
        int highestScore = 0;
        int previousHighest = 0;
        
        for (com.simats.prepace.model.QuizResult r : history) {
            if (r.getQuizTitle().equals(quizTitle) && r.getCategory().equals(category)) {
                 if (r.getScore() > highestScore) {
                     // This technically shouldn't happen if we just added the new high score, 
                     // unless we handle the "this result logic" carefully.
                     // Let's assume history contains the current result.
                 }
            }
        }
        
        // Better logic: Filter history excluding the MOST RECENT one (current).
        boolean isNewHighScore = true;
        for (int i = 1; i < history.size(); i++) { // Skip index 0 (current)
             com.simats.prepace.model.QuizResult r = history.get(i);
             if (r.getQuizTitle().equals(quizTitle) && r.getCategory().equals(category)) {
                 if (r.getScore() >= score) {
                     isNewHighScore = false;
                     break;
                 }
             }
        }
        
        // If history has only 1 item (current), it effectively is a PB, but maybe we only want to notify if they beat a PREVIOUS score?
        // Let's say if it's the first time, it's also a PB? Or maybe "First Quiz Completed" handles that via achievement.
        // Let's stick to: If they played before and this score is higher.
        boolean playedBefore = false;
        for (int i = 1; i < history.size(); i++) {
             com.simats.prepace.model.QuizResult r = history.get(i);
             if (r.getQuizTitle().equals(quizTitle) && r.getCategory().equals(category)) {
                 playedBefore = true;
                 break;
             }
        }
        
        if (playedBefore && isNewHighScore) {
             com.simats.prepace.utils.NotificationManager.addNotification(this, new com.simats.prepace.model.Notification(
                "Personal Best!",
                "You beat your previous high score in \"" + quizTitle + "\"",
                timeAgo,
                com.simats.prepace.model.Notification.Type.PERSONAL_BEST,
                true
            ));
        }

        // 3. Achievements
        // We need to check if any achievement was JUST unlocked.
        // AchievementManager.checkAchievements returns status based on ALL history.
        // We lack a "previously unlocked" state storage to know if it's NEW.
        // To fix this simple: We can store unlocked achievement IDs in Prefs in AchievementManager.
        // If an achievement is Unlocked NOW but wasn't in Prefs, trigger notification.
        
        com.simats.prepace.AchievementManager.checkAndNotifyNewAchievements(this);
        

        // Buttons
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnReview).setOnClickListener(v -> {
            java.util.List<com.simats.prepace.model.Question> reviewList = (java.util.List<com.simats.prepace.model.Question>) getIntent().getSerializableExtra("question_list");
            if (reviewList != null) {
                Intent intent = new Intent(this, ReviewAnswersActivity.class);
                intent.putExtra("question_list", (java.io.Serializable) reviewList);
                startActivity(intent);
            } else {
                 android.widget.Toast.makeText(this, "Error loading review data", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
        
         findViewById(R.id.btnViewOptions).setOnClickListener(v -> {
            // Retry Logic: Restart QuestionActivity with same parameters
            String retryCategory = getIntent().getStringExtra("category_title");
            String retryTitle = getIntent().getStringExtra("quiz_title");
            
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("category_title", retryCategory);
            intent.putExtra("quiz_title", retryTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
