package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Header Back Button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Setup Menu Items programmatically (since we used <include>)
        setupMenuItem(findViewById(R.id.menuEditProfile), "Edit Profile", R.drawable.ic_edit, R.drawable.bg_icon_blue_light, "#1976D2");
        setupMenuItem(findViewById(R.id.menuAchievements), "Achievements", R.drawable.ic_badge, R.drawable.bg_icon_purple_light, "#7B1FA2");
        setupMenuItem(findViewById(R.id.menuSettings), "Settings", R.drawable.ic_settings, R.drawable.bg_icon_gray_light, "#757575");
        setupMenuItem(findViewById(R.id.menuHelp), "Help & Support", R.drawable.ic_help_circle, R.drawable.bg_icon_green_light, "#388E3C");
        setupMenuItem(findViewById(R.id.menuPrivacy), "Privacy & Terms", R.drawable.ic_shield, R.drawable.bg_icon_blue_light, "#5E35B1");
        setupMenuItem(findViewById(R.id.menuLiveChat), "Live Chat", R.drawable.ic_chat, R.drawable.bg_icon_blue_light, "#1976D2");

        // Click Listeners for Menus (Toasts for now)
        findViewById(R.id.menuEditProfile).setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        findViewById(R.id.menuAchievements).setOnClickListener(v -> startActivity(new Intent(this, AchievementsActivity.class)));
        findViewById(R.id.menuSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        findViewById(R.id.menuHelp).setOnClickListener(v -> startActivity(new Intent(this, HelpSupportActivity.class)));
        findViewById(R.id.menuPrivacy).setOnClickListener(v -> startActivity(new Intent(this, LegalActivity.class)));
        findViewById(R.id.menuLiveChat).setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
        
        findViewById(R.id.menuLogout).setOnClickListener(v -> showLogoutDialog());

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
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
            } else if (itemId == R.id.nav_progress) {
                Intent intent = new Intent(this, ProgressActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return itemId == R.id.nav_profile;
        });
        
        // Initial Update
        updateProfileUI();
    }

    private void showLogoutDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnLogoutConfirm).setOnClickListener(v -> {
            dialog.dismiss();
            performLogout();
        });

        dialog.show();
    }

    private void performLogout() {
        // Clear user data
        UserProfileManager.getInstance(this).clear();
        
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        
        // Navigate to Login/Intro
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileUI();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
        }
    }

    private void updateProfileUI() {
        UserProfileManager userManager = UserProfileManager.getInstance(this);
        
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        ImageView ivAvatar = findViewById(R.id.ivProfileAvatar);
        
        TextView tvStatQuizzes = findViewById(R.id.tvStatQuizzes);
        TextView tvStatAvgScore = findViewById(R.id.tvStatAvgScore);
        TextView tvStatBadges = findViewById(R.id.tvStatBadges);
        
        if (tvName != null) tvName.setText(userManager.getName());
        if (tvEmail != null) tvEmail.setText(userManager.getEmail());
        
        if (ivAvatar != null) {
            String savedUri = userManager.getAvatarUri();
            if (savedUri != null) {
                ivAvatar.setImageURI(null); // Clear previous to refresh
                ivAvatar.setImageURI(android.net.Uri.parse(savedUri));
            }
        }
        
        // Calculate Stats
        java.util.List<com.simats.prepace.model.QuizResult> history = com.simats.prepace.utils.QuizHistoryManager.getQuizResults(this);
        int totalQuizzes = history.size();
        int totalScore = 0;
        for (com.simats.prepace.model.QuizResult res : history) {
            totalScore += res.getScore();
        }
        int avgScore = totalQuizzes > 0 ? totalScore / totalQuizzes : 0;
        int unlockedBadges = AchievementManager.getUnlockedCount(this);
        
        if (tvStatQuizzes != null) tvStatQuizzes.setText(String.valueOf(totalQuizzes));
        if (tvStatAvgScore != null) tvStatAvgScore.setText(avgScore + "%");
        if (tvStatBadges != null) tvStatBadges.setText(String.valueOf(unlockedBadges));
    }

    private void setupMenuItem(View view, String title, int iconRes, int bgRes, String tintColor) {
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        
        tvLabel.setText(title);
        ivIcon.setImageResource(iconRes);
        ivIcon.setBackgroundResource(bgRes);
        ivIcon.setColorFilter(android.graphics.Color.parseColor(tintColor));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
