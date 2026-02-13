package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (UserProfileManager.getInstance(this).isNotificationsEnabled()) {
            android.widget.Toast.makeText(this, "🔔 You have new quizzes available!", android.widget.Toast.LENGTH_LONG).show();
            
            // Play Notification Sound
            if (UserProfileManager.getInstance(this).isSoundEnabled()) {
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
        }


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_quizzes) {
                Intent intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (itemId == R.id.nav_progress) {
                Intent intent = new Intent(this, ProgressActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return itemId == R.id.nav_home;
        });

        findViewById(R.id.cvBrowseQuizzes).setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriesActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        findViewById(R.id.cvViewProgress).setOnClickListener(v -> {
             startActivity(new Intent(this, ProgressActivity.class));
        });
        
        // Profile Header Click
        android.widget.ImageView ivAvatar = findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Notification Click
        findViewById(R.id.ivNotification).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Chat FAB Click
        findViewById(R.id.fab_chat).setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        
        updateHomeUI();

        // Search Functionality
        android.widget.EditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                (event != null && event.getAction() == android.view.KeyEvent.ACTION_DOWN && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                
                performSearch(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        String lowerQuery = query.toLowerCase().trim();
        android.content.Intent intent = null;

        // Categories
        if (lowerQuery.contains("aptitude")) {
            intent = new Intent(this, AptitudeCategoryActivity.class);
            intent.putExtra("category_title", "Aptitude");
        } else if (lowerQuery.contains("reasoning")) {
            intent = new Intent(this, ReasoningCategoryActivity.class);
            intent.putExtra("category_title", "Reasoning");
        } else if (lowerQuery.contains("technical")) {
            intent = new Intent(this, TechnicalCategoryActivity.class);
            intent.putExtra("category_title", "Technical");
        } else if (lowerQuery.contains("quiz") || lowerQuery.contains("test") || lowerQuery.contains("exam")) {
            intent = new Intent(this, CategoriesActivity.class);
        
        // Features
        } else if (lowerQuery.contains("achievement") || lowerQuery.contains("badge") || lowerQuery.contains("reward")) {
            intent = new Intent(this, AchievementsActivity.class);
        } else if (lowerQuery.contains("history") || lowerQuery.contains("log") || lowerQuery.contains("record")) {
            intent = new Intent(this, QuizHistoryActivity.class);
        } else if (lowerQuery.contains("rank") || lowerQuery.contains("leaderboard") || lowerQuery.contains("performance")) {
            intent = new Intent(this, PerformanceActivity.class);
        } else if (lowerQuery.contains("progress") || lowerQuery.contains("stat") || lowerQuery.contains("score")) {
            intent = new Intent(this, ProgressActivity.class);
        
        // Settings & Profile
        } else if (lowerQuery.contains("edit profile") || lowerQuery.contains("change name") || lowerQuery.contains("photo") || lowerQuery.contains("avatar")) {
            intent = new Intent(this, EditProfileActivity.class);
        } else if (lowerQuery.contains("profile") || lowerQuery.contains("account") || lowerQuery.contains("user")) {
            intent = new Intent(this, ProfileActivity.class);
        } else if (lowerQuery.contains("notification") || lowerQuery.contains("alert")) {
            intent = new Intent(this, NotificationActivity.class);
        } else if (lowerQuery.contains("setting") || lowerQuery.contains("config")) {
            intent = new Intent(this, SettingsActivity.class);
        
        // Support & Legal
        } else if (lowerQuery.contains("help") || lowerQuery.contains("support") || lowerQuery.contains("faq") || lowerQuery.contains("contact")) {
            intent = new Intent(this, HelpSupportActivity.class);
        } else if (lowerQuery.contains("legal") || lowerQuery.contains("privacy") || lowerQuery.contains("term") || lowerQuery.contains("policy")) {
            intent = new Intent(this, LegalActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            android.widget.Toast.makeText(this, "No matching screen found for '" + query + "'", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHomeUI() {
        UserProfileManager userManager = UserProfileManager.getInstance(this);
        android.widget.TextView tvUserName = findViewById(R.id.tvUserName);
        android.widget.ImageView ivAvatar = findViewById(R.id.ivAvatar);
        
        if (tvUserName != null) tvUserName.setText(userManager.getName());
        
        if (ivAvatar != null) {
            String savedUri = userManager.getAvatarUri();
            if (savedUri != null) {
                ivAvatar.setImageURI(null); 
                ivAvatar.setImageURI(android.net.Uri.parse(savedUri));
            }
        }
        
        // Update Stats
        String userId = userManager.getUserId();
        java.util.List<com.simats.prepace.model.QuizResult> history = com.simats.prepace.utils.QuizHistoryManager.getQuizResults(this, userId);
        int totalQuizzes = history.size();
        int totalScore = 0;
        int totalQuestions = 0;
        
        for (com.simats.prepace.model.QuizResult res : history) {
            totalScore += res.getScore();
            totalQuestions += res.getTotalQuestions();
        }
        
        int avgScore = totalQuizzes > 0 ? totalScore / totalQuizzes : 0;
        
        android.widget.TextView tvStatQuizzes = findViewById(R.id.tvHomeStatQuizzes);
        android.widget.TextView tvStatAvgScore = findViewById(R.id.tvHomeStatAvgScore);

        if (tvStatQuizzes != null) tvStatQuizzes.setText(String.valueOf(totalQuizzes));
        if (tvStatAvgScore != null) tvStatAvgScore.setText(avgScore + "%");
        
        updateTimeSpent();
    }

    private final Handler timeUpdateHandler = new Handler();
    private final Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeSpent();
            timeUpdateHandler.postDelayed(this, 60000); // Update every minute
        }
    };

    private void updateTimeSpent() {
        android.widget.TextView tvStatTime = findViewById(R.id.tvHomeStatTime);
        if (tvStatTime != null) {
            String userId = UserProfileManager.getInstance(this).getUserId();
            String timeSpent = com.simats.prepace.utils.AppTimeTracker.getFormattedTotalTime(this, userId);
            tvStatTime.setText(timeSpent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHomeUI();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
        timeUpdateHandler.post(timeUpdateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
    }
}
