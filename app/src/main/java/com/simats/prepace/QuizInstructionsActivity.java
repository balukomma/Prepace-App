package com.simats.prepace;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class QuizInstructionsActivity extends AppCompatActivity {

    private TextView tvQuizTitle, tvQuizDescription, tvQuestionCount, tvTimeLimit, tvTimeLimitRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_instructions);

        // Initialize views
        tvQuizTitle = findViewById(R.id.tvQuizTitle);
        tvQuizDescription = findViewById(R.id.tvQuizDescription);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvTimeLimit = findViewById(R.id.tvTimeLimit);
        tvTimeLimitRule = findViewById(R.id.tvTimeLimitRule);

        // Get quiz data from intent
        String quizTitle = getIntent().getStringExtra("quiz_title");
        String quizDescription = getIntent().getStringExtra("quiz_description");
        int questionCount = getIntent().getIntExtra("question_count", 10);
        int timeLimit = getIntent().getIntExtra("time_limit", 15);
        String categoryTitle = getIntent().getStringExtra("category_title");

        // Set quiz data
        if (quizTitle != null) tvQuizTitle.setText(quizTitle);
        if (quizDescription != null) tvQuizDescription.setText(quizDescription);
        tvQuestionCount.setText(String.valueOf(questionCount));
        tvTimeLimit.setText(String.valueOf(timeLimit));
        tvTimeLimitRule.setText("• Total time limit: " + timeLimit + " minutes");

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Go Back button
        findViewById(R.id.btnGoBack).setOnClickListener(v -> finish());

        // Start Quiz button
        findViewById(R.id.btnStartQuiz).setOnClickListener(v -> {
            // Toast.makeText(this, "Starting " + quizTitle + " quiz...", Toast.LENGTH_SHORT).show();
            android.content.Intent intent = new android.content.Intent(this, QuizActivity.class);
            intent.putExtra("quiz_title", quizTitle);
            intent.putExtra("quiz_description", quizDescription);
            intent.putExtra("question_count", questionCount);
            intent.putExtra("time_limit", timeLimit);
            intent.putExtra("category_title", categoryTitle);
            startActivity(intent);
        });
    }
}
