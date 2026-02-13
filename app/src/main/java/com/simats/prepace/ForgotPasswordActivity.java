package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText etEmail = findViewById(R.id.etEmail);

        // Verify Email Button -> Go to Reset Password
        findViewById(R.id.btnVerifyEmail).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                return;
            }
            // For now, assume email is valid and navigate
            startActivity(new Intent(this, ResetPasswordActivity.class));
            finish();
        });

        // Back to Login -> Finish current activity
        findViewById(R.id.llBackToLogin).setOnClickListener(v -> {
            finish();
        });
    }
}
