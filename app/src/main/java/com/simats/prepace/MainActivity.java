package com.simats.prepace;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Dot Animation
        final android.view.View dot1 = findViewById(R.id.dot1);
        final android.view.View dot2 = findViewById(R.id.dot2);
        final android.view.View dot3 = findViewById(R.id.dot3);
        
        final android.os.Handler animHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        final Runnable animRunnable = new Runnable() {
            int step = 0;
            @Override
            public void run() {
                if (isFinishing()) return;

                dot1.setBackgroundResource(step == 0 ? R.drawable.dot_active : R.drawable.dot_inactive);
                dot2.setBackgroundResource(step == 1 ? R.drawable.dot_active : R.drawable.dot_inactive);
                dot3.setBackgroundResource(step == 2 ? R.drawable.dot_active : R.drawable.dot_inactive);
                
                step = (step + 1) % 3;
                animHandler.postDelayed(this, 300); // Speed of running light
            }
        };
        animHandler.post(animRunnable);

        // Navigation Delay
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            animHandler.removeCallbacks(animRunnable); // Cleanup animation
            startActivity(new android.content.Intent(MainActivity.this, OnboardingActivity.class));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            finish();
        }, 2500); // Slightly longer to visually see animation
    }
}