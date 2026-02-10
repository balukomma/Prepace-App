package com.simats.prepace;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TechnicalQuizListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_quiz_list);

        android.widget.TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Available Quizzes");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Set up click listener for quiz card
        findViewById(R.id.btnStartQuiz1).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, QuizInstructionsActivity.class);
            intent.putExtra("quiz_title", "JavaScript Basics");
            intent.putExtra("quiz_description", "Test your JavaScript fundamentals");
            intent.putExtra("question_count", 10);
            intent.putExtra("time_limit", 15);
            intent.putExtra("category_title", "Technical");
            startActivity(intent);
        });

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
