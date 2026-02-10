package com.simats.prepace;

import android.content.Context;
import com.simats.prepace.model.QuizResult;
import com.simats.prepace.utils.QuizHistoryManager;
import java.util.ArrayList;
import java.util.List;

public class AchievementManager {

    public static class Achievement {
        public String id;
        public String title;
        public String description;
        public boolean isUnlocked;
        public int iconResId;

        public Achievement(String id, String title, String description, boolean isUnlocked, int iconResId) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.isUnlocked = isUnlocked;
            this.iconResId = iconResId;
        }
    }

    public static List<Achievement> checkAchievements(Context context) {
        List<QuizResult> history = QuizHistoryManager.getQuizResults(context);
        List<Achievement> achievements = new ArrayList<>();
        int totalQuizzes = history.size();
        boolean perfectScore = false;
        
        for (QuizResult res : history) {
            if (res.getScore() == 100) { // Assuming score is % or verify logic
                perfectScore = true;
            }
        }

        // Achievement 1: First Steps
        achievements.add(new Achievement(
            "first_steps",
            "First Steps",
            "Complete your first quiz",
            totalQuizzes >= 1,
            R.drawable.ic_play // Using play icon as placeholder for steps
        ));

        // Achievement 2: Perfect Score
        achievements.add(new Achievement(
            "perfect_score",
            "Perfect Score",
            "Score 100% in any quiz",
            perfectScore,
            R.drawable.ic_target // Target for accuracy/perfect score
        ));

        // Achievement 3: Dedicated Learner
        achievements.add(new Achievement(
            "dedicated_learner",
            "Dedicated Learner",
            "Complete 10 quizzes",
            totalQuizzes >= 10,
            R.drawable.ic_book // Book for learning
        ));
        
        // Achievement 4: Speed Demon
        achievements.add(new Achievement(
            "speed_demon",
            "Speed Demon",
            "Complete a quiz in under 5 minutes",
             false,
             R.drawable.ic_time // Time icon
        ));
        
         // Achievement 5: Streak Master
        achievements.add(new Achievement(
            "streak_master",
            "Streak Master",
            "Practice for 7 consecutive days",
            false,
            R.drawable.ic_trend_up // Trend up for streak/growth
        ));

        return achievements;
    }
    
    public static int getUnlockedCount(Context context) {
        int count = 0;
        for (Achievement a : checkAchievements(context)) {
            if (a.isUnlocked) count++;
        }
        return count;
    }

    public static void checkAndNotifyNewAchievements(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences("AchievementPrefs", Context.MODE_PRIVATE);
        java.util.Set<String> unlockedIds = prefs.getStringSet("unlocked_ids", new java.util.HashSet<>());
        
        List<Achievement> currentStatus = checkAchievements(context);
        boolean saveNeeded = false;
        
        for (Achievement a : currentStatus) {
            if (a.isUnlocked && !unlockedIds.contains(a.id)) {
                // NEW UNLOCK!
                com.simats.prepace.utils.NotificationManager.addNotification(context, new com.simats.prepace.model.Notification(
                    "Achievement Unlocked",
                    "You've earned the \"" + a.title + "\" badge!",
                    "Just now",
                    com.simats.prepace.model.Notification.Type.ACHIEVEMENT_UNLOCKED,
                    true
                ));
                unlockedIds.add(a.id);
                saveNeeded = true;
            }
        }
        
        if (saveNeeded) {
            prefs.edit().putStringSet("unlocked_ids", unlockedIds).apply();
        }
    }
}
