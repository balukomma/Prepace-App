package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.simats.prepace.adapter.ReviewAnswersAdapter;
import com.simats.prepace.model.Question;
import java.util.List;

public class ReviewAnswersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_answers);

        // Get Data
        List<Question> questionList = (List<Question>) getIntent().getSerializableExtra("question_list");
        
        if (questionList == null || questionList.isEmpty()) {
             android.widget.Toast.makeText(this, "No review data available.", android.widget.Toast.LENGTH_SHORT).show();
             finish();
             return;
        }

        // Setup RecyclerView
        RecyclerView rvReview = findViewById(R.id.rvReviewAnswers);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        
        ReviewAnswersAdapter adapter = new ReviewAnswersAdapter(this, questionList);
        rvReview.setAdapter(adapter);

        // Buttons
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
