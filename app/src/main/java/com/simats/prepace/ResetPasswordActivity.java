package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private ImageView ivShowNewPassword;
    private ImageView ivShowConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ivShowNewPassword = findViewById(R.id.ivShowNewPassword);
        ivShowConfirmPassword = findViewById(R.id.ivShowConfirmPassword);

        // Password Visibility Toggles
        setupPasswordToggle(etNewPassword, ivShowNewPassword);
        setupPasswordToggle(etConfirmPassword, ivShowConfirmPassword);

        // Reset Button
        findViewById(R.id.btnResetPassword).setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString();
            String confirmPass = etConfirmPassword.getText().toString();

            if (newPass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Success -> Go to Login
            Toast.makeText(this, "Password Reset Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity(); // Clear stack
        });
    }

    private void setupPasswordToggle(EditText editText, ImageView imageView) {
        imageView.setOnClickListener(v -> {
            if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                // Show Password
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageView.setImageResource(R.drawable.ic_eye_off);
            } else {
                // Hide Password
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageView.setImageResource(R.drawable.ic_eye);
            }
            // Move cursor to end
            editText.setSelection(editText.getText().length());
        });
    }
}
