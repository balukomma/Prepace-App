package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.simats.prepace.model.QuizResult;
import com.simats.prepace.ui.SimpleBarChartView;
import com.simats.prepace.ui.SimpleLineChartView;
import com.simats.prepace.utils.QuizHistoryManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {

    private SimpleLineChartView chartScoreTrends;
    private SimpleBarChartView chartQuizActivity;
    private TextView tvTotalQuizzes;
    private TextView tvAvgScore;
    
    private List<QuizResult> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        initViews();
        loadData();
    }

    private void initViews() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        chartScoreTrends = findViewById(R.id.chartScoreTrends);
        chartQuizActivity = findViewById(R.id.chartQuizActivity);
        tvTotalQuizzes = findViewById(R.id.tvTotalQuizzes);
        tvAvgScore = findViewById(R.id.tvAvgScore);

        // Buttons for Performance and History (Placeholders for now)
        // Buttons for Performance and History
        findViewById(R.id.cardPerformance).setOnClickListener(v -> {
             startActivity(new Intent(this, PerformanceActivity.class));
        });
        
        findViewById(R.id.cardHistory).setOnClickListener(v -> {
             startActivity(new Intent(this, QuizHistoryActivity.class));
        });
        
        // Toggle Logic (Visual only for now, default Weekly)
        TextView btnWeekly = findViewById(R.id.btnWeekly);
        TextView btnMonthly = findViewById(R.id.btnMonthly);
        
        btnWeekly.setOnClickListener(v -> {
            btnWeekly.setBackgroundResource(R.drawable.bg_toggle_active);
            btnWeekly.setTextColor(getColor(R.color.white));
            btnMonthly.setBackgroundResource(R.drawable.bg_toggle_inactive);
            btnMonthly.setTextColor(getColor(android.R.color.darker_gray));
            // Reload weekly data
            bindCharts(true);
        });
        
        btnMonthly.setOnClickListener(v -> {
            btnMonthly.setBackgroundResource(R.drawable.bg_toggle_active);
            btnMonthly.setTextColor(getColor(R.color.white));
            btnWeekly.setBackgroundResource(R.drawable.bg_toggle_inactive);
            btnWeekly.setTextColor(getColor(android.R.color.darker_gray));
             // Reload monthly data (logic same for now as we don't have enough data)
             bindCharts(false);
        });

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
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
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return itemId == R.id.nav_progress;
        });
    }

    private void loadData() {
        String userId = com.simats.prepace.UserProfileManager.getInstance(this).getUserId();
        history = QuizHistoryManager.getQuizResults(this, userId);
        
        // --- Top Stats ---
        int totalQuizzes = history.size();
        tvTotalQuizzes.setText(String.valueOf(totalQuizzes));

        long sumAccuracy = 0;
        for (QuizResult res : history) {
            int qTotal = res.getTotalQuestions() > 0 ? res.getTotalQuestions() : 1;
            int accuracy = (int) (((float) res.getScore() / qTotal) * 100);
            sumAccuracy += accuracy;
        }
        int avg = totalQuizzes > 0 ? (int) (sumAccuracy / totalQuizzes) : 0;
        tvAvgScore.setText(avg + "%");

        // --- Charts ---
        bindCharts(true);
    }
    
    private void bindCharts(boolean isWeekly) {
         // Data Aggregation for last 7 days (Weekly)
         // Map<DateString, Data>
         
         // 1. Prepare Last 7 Days Labels
         List<String> labels = new ArrayList<>();
         List<String> dateKeys = new ArrayList<>(); // format yyyy-MM-dd
         
         SimpleDateFormat sdfLabel = new SimpleDateFormat("EEE", Locale.getDefault());
         SimpleDateFormat sdfKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
         
         Calendar cal = Calendar.getInstance();
         // Go back 6 days + today
         cal.add(Calendar.DAY_OF_YEAR, -6);
         
         for(int i=0; i<7; i++) {
             Date d = cal.getTime();
             labels.add(sdfLabel.format(d));
             dateKeys.add(sdfKey.format(d));
             cal.add(Calendar.DAY_OF_YEAR, 1);
         }
         
         // 2. Aggregate Data
         Map<String, List<Integer>> scoresPerDay = new HashMap<>(); // Key: Date, Value: List of accuracies
         Map<String, Integer> quizzesPerDay = new HashMap<>();       // Key: Date, Value: Count
         
         for(String key : dateKeys) {
             scoresPerDay.put(key, new ArrayList<>());
             quizzesPerDay.put(key, 0);
         }
         
         for(QuizResult res : history) {
             String resDate = sdfKey.format(new Date(res.getTimestamp()));
             if(scoresPerDay.containsKey(resDate)) {
                  int qTotal = res.getTotalQuestions() > 0 ? res.getTotalQuestions() : 1;
                  int accuracy = (int) (((float) res.getScore() / qTotal) * 100);
                  
                  scoresPerDay.get(resDate).add(accuracy);
                  quizzesPerDay.put(resDate, quizzesPerDay.get(resDate) + 1);
             }
         }
         
         // 3. Prepare Chart Data Objects
         List<Integer> avgScores = new ArrayList<>();
         List<Integer> activityCounts = new ArrayList<>();
         
         for(String key : dateKeys) {
             List<Integer> dayScores = scoresPerDay.get(key);
             int dayAvg = 0;
             if(!dayScores.isEmpty()) {
                 int sum = 0;
                 for(int s : dayScores) sum += s;
                 dayAvg = sum / dayScores.size();
             }
             avgScores.add(dayAvg);
             activityCounts.add(quizzesPerDay.get(key));
         }
         
         // 4. Set Data
         chartScoreTrends.setData(avgScores, labels);
         chartQuizActivity.setData(activityCounts, labels);
    }
}
