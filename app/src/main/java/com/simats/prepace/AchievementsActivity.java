package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AchievementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        android.widget.TextView tvProgress = findViewById(R.id.progressBar).getRootView().findViewById(R.id.tvProgressCount); // Wait, need ID for textview 2/6
        // Let's just find by ID if I added it? Check xml.
        // Actually, I need to check activity_achievements.xml again or just find views by structure or ID.
        // Let me add IDs to xml first to be safe in next step if needed, but for now I'll write the logic assuming IDs exist or I'll add them.
        
        updateAchievements();
    }
    
    private void updateAchievements() {
        android.widget.LinearLayout container = findViewById(R.id.llAchievementsList);
        container.removeAllViews(); // Clear static includes
        
        String userId = com.simats.prepace.UserProfileManager.getInstance(this).getUserId();
        java.util.List<AchievementManager.Achievement> list = AchievementManager.checkAchievements(this, userId);
        int unlockedCount = 0;
        
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
        
        for (AchievementManager.Achievement a : list) {
            if (a.isUnlocked) {
                unlockedCount++;
                android.view.View view = inflater.inflate(R.layout.item_achievement_card_unlocked, container, false);
                ((android.widget.TextView) view.findViewById(R.id.tvTitle)).setText(a.title);
                ((android.widget.TextView) view.findViewById(R.id.tvDesc)).setText(a.description);
                ((android.widget.ImageView) view.findViewById(R.id.ivBadge)).setImageResource(a.iconResId);
                container.addView(view);
            } else {
                android.view.View view = inflater.inflate(R.layout.item_achievement_card_locked, container, false);
                ((android.widget.TextView) view.findViewById(R.id.tvTitle)).setText(a.title);
                ((android.widget.TextView) view.findViewById(R.id.tvDesc)).setText(a.description);
                // For locked, we might want to keep the generic lock or use the icon in grayscale? 
                // Let's stick to generic lock for mysterious feel, or user request implied "badges changed based on analytics" -> unlocked ones.
                container.addView(view);
            }
        }
        
        // Update Progress
        android.widget.ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(list.size());
        progressBar.setProgress(unlockedCount);
        
        android.widget.TextView tvCount = findViewById(R.id.tvProgressCount);
        if (tvCount != null) {
            tvCount.setText(unlockedCount + "/" + list.size());
        }
    }
}
