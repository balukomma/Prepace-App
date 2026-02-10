package com.simats.prepace;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.simats.prepace.adapter.QuizAdapter;
import com.simats.prepace.model.Quiz;
import java.util.ArrayList;
import java.util.List;

public class QuizListActivity extends AppCompatActivity {

    private RecyclerView rvQuizzes;
    private QuizAdapter adapter;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        String category = getIntent().getStringExtra("category_title");
        if (category == null) category = "Aptitude";
        
        android.widget.TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Available Quizzes");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rvQuizzes = findViewById(R.id.rvQuizzes);
        rvQuizzes.setLayoutManager(new LinearLayoutManager(this));

        // Load quizzes first
        loadQuizzes(category);

        // Initialize adapter AFTER loading quizzes
        if (quizList != null && !quizList.isEmpty()) {
            adapter = new QuizAdapter(this, quizList);
            rvQuizzes.setAdapter(adapter);
        }

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

    private void loadQuizzes(String category) {
        // Use the centralized data manager
        List<Quiz> allQuizzes = com.simats.prepace.utils.QuizDataManager.getQuizzesForCategory(this, category);
        
        // Check for difficulty filter
        String filterDifficulty = getIntent().getStringExtra("filter_difficulty");
        
        if (filterDifficulty == null || filterDifficulty.equalsIgnoreCase("All")) {
            quizList = allQuizzes;
        } else {
            quizList = new ArrayList<>();
            for (Quiz q : allQuizzes) {
                // Robust comparison: trim and ignore case
                if (q.getDifficulty().trim().equalsIgnoreCase(filterDifficulty.trim())) {
                    quizList.add(q);
                }
            }
        }
        
        // Handle Empty State
        android.widget.TextView tvNoData = findViewById(R.id.tvNoData);
        if (quizList.isEmpty()) {
            if (tvNoData != null) {
                tvNoData.setVisibility(android.view.View.VISIBLE);
                tvNoData.setText("No quizzes found for " + (filterDifficulty != null ? filterDifficulty : "this category"));
            }
        } else {
            if (tvNoData != null) {
                tvNoData.setVisibility(android.view.View.GONE);
            }
        }
    }
}
