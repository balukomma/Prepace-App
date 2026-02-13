package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.simats.prepace.model.QuizResult;
import com.simats.prepace.ui.QuizHistoryAdapter;
import com.simats.prepace.utils.QuizHistoryManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class QuizHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_history);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        
        String userId = com.simats.prepace.UserProfileManager.getInstance(this).getUserId();
        List<QuizResult> history = QuizHistoryManager.getQuizResults(this, userId);
        
        // --- DUMMY DATA FOR DEMO (If empty) ---
        if (history.isEmpty()) {
            // JavaScript Basics: 0% (0/2)
            history.add(new QuizResult("Technical", "JavaScript Basics", 0, 2, System.currentTimeMillis() - 86400000L * 4)); 
            
            // Basic Arithmetic: 40% (4/10)
            history.add(new QuizResult("Aptitude", "Basic Arithmetic", 4, 10, System.currentTimeMillis() - 86400000L * 4));
        }
        
        QuizHistoryAdapter adapter = new QuizHistoryAdapter(this, history);
        rvHistory.setAdapter(adapter);

        // Setup Bottom Nav (just for look/nav consistency)
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_progress);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (itemId == R.id.nav_quizzes) {
                 Intent intent = new Intent(this, CategoriesActivity.class);
                 startActivity(intent);
                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                 return true;
            }
            return itemId == R.id.nav_progress;
        });
    }
}
