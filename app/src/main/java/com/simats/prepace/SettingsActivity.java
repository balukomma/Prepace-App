package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        UserProfileManager userManager = UserProfileManager.getInstance(this);

        android.widget.Switch swNotifications = findViewById(R.id.swNotifications);
        android.widget.Switch swDarkMode = findViewById(R.id.swDarkMode);
        android.widget.Switch swSound = findViewById(R.id.swSound);
        
        // Initial States
        swNotifications.setChecked(userManager.isNotificationsEnabled());
        swDarkMode.setChecked(userManager.isDarkModeEnabled());
        swSound.setChecked(userManager.isSoundEnabled());
        
        // Listeners
        swNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userManager.setNotificationsEnabled(isChecked);
        });

        swDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
             userManager.setDarkModeEnabled(isChecked);
             if (isChecked) {
                 androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
             } else {
                 androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
             }
        });

        swSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userManager.setSoundEnabled(isChecked);
        });
        
        findViewById(R.id.llLanguageRow).setOnClickListener(v -> {
            final String[] languages = {"English", "Spanish", "French", "German"};
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    String selected = languages[which];
                    userManager.setLanguage(selected);
                    android.widget.Toast.makeText(this, "Language set to " + selected, android.widget.Toast.LENGTH_SHORT).show();
                    // In a real app, we'd reload resources/locale here
                })
                .show();
        });
        
        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        // Note: Settings isn't a main tab, but acts like it's under Profile in flow.
        // We can highlight Profile or nothing. Let's highlight Profile since it's a sub-screen of it often.
        // Or just leave it unselected. Let's highlight Profile.
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
            } else if (itemId == R.id.nav_profile) {
                // Already in a profile-related screen, maybe go back to main Profile?
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });
    }
}
