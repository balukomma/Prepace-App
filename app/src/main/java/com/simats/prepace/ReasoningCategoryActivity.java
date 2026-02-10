package com.simats.prepace;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReasoningCategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasoning_category);

        String title = getIntent().getStringExtra("category_title");
        String desc = getIntent().getStringExtra("category_desc");
        String quizCount = getIntent().getStringExtra("quiz_count");
        int headerBgRes = getIntent().getIntExtra("header_bg", R.drawable.bg_category_reasoning);

        android.view.View headerBg = findViewById(R.id.header_bg);
        android.widget.TextView tvTitle = findViewById(R.id.tvCategoryTitle);
        android.widget.TextView tvDesc = findViewById(R.id.tvCategoryDesc);
        android.widget.TextView tvQuizCount = findViewById(R.id.tvQuizCountBadge);

        if (title != null) tvTitle.setText(title);
        if (desc != null) tvDesc.setText(desc);
        
        // Dynamic Quiz Count
        int count = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, title != null ? title : "Reasoning").size();
        tvQuizCount.setText(count + " Quizzes");
        
        headerBg.setBackgroundResource(headerBgRes);

        final String categoryTitle = title != null ? title : "Reasoning";

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
        int totalAvailableQuizzes = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, categoryTitle).size();
        int uniqueCompleted = com.simats.prepace.utils.QuizHistoryManager.getUniqueCompletedQuizCount(this, categoryTitle);
        
        int completionRate = 0;
        if (totalAvailableQuizzes > 0) {
            completionRate = (int) (((float) uniqueCompleted / totalAvailableQuizzes) * 100);
        }
        
        android.widget.TextView tvCompletionRate = findViewById(R.id.tvStatCompletionRate);
        if (tvCompletionRate != null) {
            tvCompletionRate.setText(completionRate + "%");
        }
    }

    private void setupFilterButton(int btnId, String difficulty, String category) {
        findViewById(btnId).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, QuizListActivity.class);
            intent.putExtra("category_title", category);
            intent.putExtra("filter_difficulty", difficulty);
            startActivity(intent);
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

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
}
