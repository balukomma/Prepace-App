package com.simats.prepace;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LegalActivity extends AppCompatActivity {

    private LinearLayout tabPrivacy, tabTerms;
    private View contentPrivacy, contentTerms;
    private ImageView ivPrivacyIcon, ivTermsIcon;
    private TextView tvPrivacyLabel, tvTermsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Views
        tabPrivacy = findViewById(R.id.tabPrivacy);
        tabTerms = findViewById(R.id.tabTerms);
        contentPrivacy = findViewById(R.id.contentPrivacy);
        contentTerms = findViewById(R.id.contentTerms);
        
        ivPrivacyIcon = findViewById(R.id.ivPrivacyIcon);
        ivTermsIcon = findViewById(R.id.ivTermsIcon);
        tvPrivacyLabel = findViewById(R.id.tvPrivacyLabel);
        tvTermsLabel = findViewById(R.id.tvTermsLabel);
        
        TextView tvPrivacyBody = findViewById(R.id.tvPrivacyBody);
        TextView tvTermsBody = findViewById(R.id.tvTermsBody);

        // Load Content
        tvPrivacyBody.setText(Html.fromHtml(getPrivacyContent(), Html.FROM_HTML_MODE_LEGACY));
        tvTermsBody.setText(Html.fromHtml(getTermsContent(), Html.FROM_HTML_MODE_LEGACY));

        // Click Listeners
        tabPrivacy.setOnClickListener(v -> switchTab(true));
        tabTerms.setOnClickListener(v -> switchTab(false));
    }

    private void switchTab(boolean isPrivacy) {
        if (isPrivacy) {
            // Active Privacy
            tabPrivacy.setBackgroundResource(R.drawable.bg_tab_active);
            ivPrivacyIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
            tvPrivacyLabel.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            contentPrivacy.setVisibility(View.VISIBLE);

            // Inactive Terms
            tabTerms.setBackgroundResource(R.drawable.bg_tab_inactive);
            ivTermsIcon.setColorFilter(android.graphics.Color.parseColor("#757575"));
            tvTermsLabel.setTextColor(android.graphics.Color.parseColor("#757575"));
            contentTerms.setVisibility(View.GONE);
        } else {
            // Inactive Privacy
            tabPrivacy.setBackgroundResource(R.drawable.bg_tab_inactive);
            ivPrivacyIcon.setColorFilter(android.graphics.Color.parseColor("#757575"));
            tvPrivacyLabel.setTextColor(android.graphics.Color.parseColor("#757575"));
            contentPrivacy.setVisibility(View.GONE);

            // Active Terms
            tabTerms.setBackgroundResource(R.drawable.bg_tab_active);
            ivTermsIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
            tvTermsLabel.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            contentTerms.setVisibility(View.VISIBLE);
        }
    }

    private String getPrivacyContent() {
        return "<b>1. Information We Collect</b><br>" +
                "We collect information you provide directly to us, including your name, email address, and quiz performance data. This helps us provide you with personalized learning experiences.<br><br>" +
                "<b>2. How We Use Your Information</b><br>" +
                "Your information is used to:<br>" +
                "• Track your learning progress<br>" +
                "• Provide personalized quiz recommendations<br>" +
                "• Improve our app and services<br>" +
                "• Send you important updates<br><br>" +
                "<b>3. Data Security</b><br>" +
                "We implement industry-standard security measures to protect your personal information. Your data is encrypted and stored securely.<br><br>" +
                "<b>4. Your Rights</b><br>" +
                "You have the right to access, update, or delete your personal information at any time. Contact our support team for assistance.<br><br>" +
                "<b>5. Contact Us</b><br>" +
                "If you have questions about our privacy practices, please contact us at privacy@prepace.com";
    }

    private String getTermsContent() {
        return "<b>1. Acceptance of Terms</b><br>" +
                "By accessing and using Prepace, you agree to be bound by these Terms of Service. If you disagree with any part of these terms, you may not use our service.<br><br>" +
                "<b>2. User Accounts</b><br>" +
                "You are responsible for maintaining the confidentiality of your account credentials. You must notify us immediately of any unauthorized use of your account.<br><br>" +
                "<b>3. User Conduct</b><br>" +
                "You agree not to:<br>" +
                "• Share quiz content without permission<br>" +
                "• Use the app for any illegal purposes<br>" +
                "• Attempt to hack or compromise the app<br><br>" +
                "<b>4. Intellectual Property</b><br>" +
                "All content, including quizzes, questions, and explanations, are the property of Prepace. Unauthorized reproduction is prohibited.<br><br>" +
                "<b>5. Limitation of Liability</b><br>" +
                "Prepace is provided \"as is\" without warranties. We are not liable for any damages arising from your use of the app.";
    }
}
