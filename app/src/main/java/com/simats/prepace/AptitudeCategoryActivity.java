package com.simats.prepace;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AptitudeCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aptitude_category);

        // Get extras
        String title = getIntent().getStringExtra("category_title");
        String desc = getIntent().getStringExtra("category_desc");
        String quizCount = getIntent().getStringExtra("quiz_count");
        int headerBgRes = getIntent().getIntExtra("header_bg", R.drawable.bg_category_aptitude);

        // Setup Header
        android.view.View headerBg = findViewById(R.id.header_bg);
        android.widget.TextView tvTitle = findViewById(R.id.tvCategoryTitle);
        android.widget.TextView tvDesc = findViewById(R.id.tvCategoryDesc);
        android.widget.TextView tvQuizCount = findViewById(R.id.tvQuizCountBadge);

        if (title != null) tvTitle.setText(title);
        if (desc != null) tvDesc.setText(desc);
        
        // Dynamic Quiz Count
        int count = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, title != null ? title : "Aptitude").size();
        tvQuizCount.setText(count + " Quizzes");
        
        headerBg.setBackgroundResource(headerBgRes);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Pass title as category to list
        final String categoryTitle = title != null ? title : "Aptitude";

        findViewById(R.id.btnViewAll).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, QuizListActivity.class);
            intent.putExtra("category_title", categoryTitle);
            intent.putExtra("filter_difficulty", "All");
            startActivity(intent);
        });
        
        // Filter Buttons
        setupFilterButton(R.id.btnFilterAll, "All", categoryTitle);
        setupFilterButton(R.id.btnFilterEasy, "Easy", categoryTitle);
        setupFilterButton(R.id.btnFilterMedium, "Medium", categoryTitle);
        setupFilterButton(R.id.btnFilterHard, "Hard", categoryTitle);
        
        // Calculate and Set Dynamic Stats
        // 1. Total Questions
        int totalQuestions = com.simats.prepace.utils.QuizDataManager.getTotalQuestions(this, categoryTitle);
        android.widget.TextView tvTotalQs = findViewById(R.id.tvStatTotalQuestions);
        if (tvTotalQs != null) {
            tvTotalQs.setText(String.valueOf(totalQuestions));
        }

        // 2. Completion Rate
        // Logic: (Unique Quizzes Completed / Total Quizzes Available) * 100
        int totalAvailableQuizzes = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, categoryTitle).size();
        String userId = com.simats.prepace.UserProfileManager.getInstance(this).getUserId();
        int uniqueCompleted = com.simats.prepace.utils.QuizHistoryManager.getUniqueCompletedQuizCount(this, userId, categoryTitle);
        
        int completionRate = 0;
        if (totalAvailableQuizzes > 0) {
            completionRate = (int) (((float) uniqueCompleted / totalAvailableQuizzes) * 100);
        }
        
        android.widget.TextView tvCompletionRate = findViewById(R.id.tvStatCompletionRate);
        if (tvCompletionRate != null) {
            tvCompletionRate.setText(completionRate + "%");
        }
    
    
        // Bottom Navigation Setup
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_quizzes);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                finish();
                return true;
            }
            return item.getItemId() == R.id.nav_quizzes;
        });
    }
    
    private void setupFilterButton(int btnId, String difficulty, String category) {
        findViewById(btnId).setOnClickListener(v -> {
            // DEBUG: Confirm click
            // android.widget.Toast.makeText(this, "Filter: " + difficulty, android.widget.Toast.LENGTH_SHORT).show();
            
            android.content.Intent intent = new android.content.Intent(this, QuizListActivity.class);
            intent.putExtra("category_title", category);
            intent.putExtra("filter_difficulty", difficulty);
            startActivity(intent);
        });
    }
}
