package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // Update Quiz Counts Dynamically
        updateCategoryCounts();

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_quizzes);

        setupClickListeners();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            return itemId == R.id.nav_quizzes;
        });
    }

    private void updateCategoryCounts() {
        android.widget.TextView tvAptitude = findViewById(R.id.tvAptitudeCount);
        android.widget.TextView tvReasoning = findViewById(R.id.tvReasoningCount);
        android.widget.TextView tvTechnical = findViewById(R.id.tvTechnicalCount);

        int aptitudeCount = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, "Aptitude").size();
        int reasoningCount = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, "Reasoning").size();
        int technicalCount = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, "Technical").size();

        if (tvAptitude != null) tvAptitude.setText(aptitudeCount + " Quizzes");
        if (tvReasoning != null) tvReasoning.setText(reasoningCount + " Quizzes");
        if (tvTechnical != null) tvTechnical.setText(technicalCount + " Quizzes");
    }

    private void setupClickListeners() {
        int aptitudeCount = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, "Aptitude").size();
        int reasoningCount = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, "Reasoning").size();
        int technicalCount = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, "Technical").size();

        findViewById(R.id.llAptitude).setOnClickListener(v -> navigateToDetail(AptitudeCategoryActivity.class, "Aptitude", "Test your numerical and logical reasoning abilities", String.valueOf(aptitudeCount), R.drawable.bg_category_aptitude));
        findViewById(R.id.llReasoning).setOnClickListener(v -> navigateToDetail(ReasoningCategoryActivity.class, "Reasoning", "Enhance your analytical and problem-solving skills", String.valueOf(reasoningCount), R.drawable.bg_category_reasoning));
        findViewById(R.id.llTechnical).setOnClickListener(v -> navigateToDetail(TechnicalCategoryActivity.class, "Technical", "Master programming and technical concepts", String.valueOf(technicalCount), R.drawable.bg_category_technical_detail));
    }

    private void navigateToDetail(Class<?> cls, String title, String desc, String quizCount, int headerBg) {
        android.content.Intent intent = new android.content.Intent(this, cls);
        intent.putExtra("category_title", title);
        intent.putExtra("category_desc", desc);
        intent.putExtra("quiz_count", quizCount);
        intent.putExtra("header_bg", headerBg);
        startActivity(intent);
    }
}
