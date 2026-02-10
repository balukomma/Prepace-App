package com.simats.prepace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HelpSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        // Back Button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // FAQ Setup
        setupFaq();

        // Contact Support Actions
        setupContactActions();
    }

    private void setupFaq() {
        LinearLayout container = findViewById(R.id.llFaqContainer);
        String[][] faqData = {
            {"How do I start a quiz?", 
             "Browse categories from the home screen, select a category, choose a quiz, and tap 'Start Quiz'. You can review the instructions before beginning."},
            {"Can I review my answers after completing a quiz?", 
             "Yes, immediately after finishing a quiz, you can tap 'Review Answers' on the result screen to see correct and incorrect answers."},
            {"How is my score calculated?", 
             "Your score is based on the number of correct answers. Each correct answer awards 1 point. There is no negative marking currently."},
            {"Can I retake a quiz?", 
             "Yes, you can retake any quiz as many times as you like. Your history will save each attempt separately."},
            {"How do I track my progress?", 
             "Go to the 'Progress' tab in the bottom navigation to see your statistics, including total quizzes taken, average score, and category performance."},
            {"What do the difficulty levels mean?", 
             "Quizzes are categorized into Easy, Medium, and Hard based on the complexity of questions. Beginners should start with Easy."}
        };

        LayoutInflater inflater = LayoutInflater.from(this);

        for (String[] faq : faqData) {
            View view = inflater.inflate(R.layout.item_faq_row, container, false);
            
            TextView tvQuestion = view.findViewById(R.id.tvQuestion);
            TextView tvAnswer = view.findViewById(R.id.tvAnswer);
            ImageView ivExpandIcon = view.findViewById(R.id.ivExpandIcon);
            LinearLayout llHeader = view.findViewById(R.id.llHeader);

            tvQuestion.setText(faq[0]);
            tvAnswer.setText(faq[1]);

            llHeader.setOnClickListener(v -> {
                if (tvAnswer.getVisibility() == View.VISIBLE) {
                    tvAnswer.setVisibility(View.GONE);
                    ivExpandIcon.animate().rotation(0).setDuration(200).start();
                } else {
                    tvAnswer.setVisibility(View.VISIBLE);
                    ivExpandIcon.animate().rotation(180).setDuration(200).start();
                }
            });

            container.addView(view);
        }
    }

    private void setupContactActions() {
        // Chat Support
        findViewById(R.id.cvChatSupport).setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        // Email Support
        findViewById(R.id.cvEmailSupport).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@prepace.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        // Call Support
        findViewById(R.id.cvCallSupport).setOnClickListener(v -> {
             Intent intent = new Intent(Intent.ACTION_DIAL);
             intent.setData(Uri.parse("tel:+917670951237"));
             try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to call", Toast.LENGTH_SHORT).show();
            }
        });

        // Write Feedback
        findViewById(R.id.btnFeedback).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:feedback@prepace.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
