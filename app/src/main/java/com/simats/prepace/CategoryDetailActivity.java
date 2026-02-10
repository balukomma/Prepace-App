package com.simats.prepace;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        String title = getIntent().getStringExtra("category_title");
        String desc = getIntent().getStringExtra("category_desc");
        int headerBgRes = getIntent().getIntExtra("header_bg", R.drawable.bg_category_aptitude);
        String quizCount = getIntent().getStringExtra("quiz_count");

        View headerBg = findViewById(R.id.header_bg);
        TextView tvTitle = findViewById(R.id.tvCategoryTitle);
        TextView tvDesc = findViewById(R.id.tvCategoryDesc);
        TextView tvQuizCount = findViewById(R.id.tvQuizCountBadge);

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvQuizCount.setText(quizCount + " Quizzes");
        headerBg.setBackgroundResource(headerBgRes);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnViewAll).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, QuizListActivity.class);
            intent.putExtra("category_title", title);
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
