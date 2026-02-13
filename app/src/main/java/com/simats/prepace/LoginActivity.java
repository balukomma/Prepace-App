package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.tvRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateAccountActivity.class));
            finish();
        });

        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        android.widget.EditText etEmail = findViewById(R.id.etEmail);
        android.widget.EditText etPassword = findViewById(R.id.etPassword);
        android.widget.ImageView ivShowPassword = findViewById(R.id.ivShowPassword);
        android.widget.Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        ivShowPassword.setOnClickListener(v -> {
            if (etPassword.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                // Show Password
                etPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                ivShowPassword.setImageResource(R.drawable.ic_eye_off);
            } else {
                // Hide Password
                etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                ivShowPassword.setImageResource(R.drawable.ic_eye);
            }
            // Move cursor to the end
            etPassword.setSelection(etPassword.getText().length());
        });


    }



    private void loginUser(String email, String password) {
        com.google.gson.JsonObject loginRequest = new com.google.gson.JsonObject();
        loginRequest.addProperty("email", email);
        loginRequest.addProperty("password", password);

        com.simats.prepace.network.RetrofitClient.getApiService().login(loginRequest).enqueue(new retrofit2.Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<com.google.gson.JsonObject> call, retrofit2.Response<com.google.gson.JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.google.gson.JsonObject body = response.body();

                    if (body.has("status") && body.get("status").getAsString().equals("success")) {
                        // Login successful
                        String userId = body.has("user_id") ? body.get("user_id").getAsString() : "";
                        String name = body.has("username") ? body.get("username").getAsString() : "User";
                        String phone = body.has("phone") ? body.get("phone").getAsString() : "";
                        // Capture API Key if provided, though not used in ProfileManager yet
                        
                        if (!userId.isEmpty()) {
                            UserProfileManager.getInstance(LoginActivity.this).saveUserId(userId);
                            
                            // Fetch full profile to get correct name/avatar
                            fetchUserProfileAndNavigate(userId, email);
                        } else {
                            // Fallback if no user ID (shouldn't happen on success)
                            UserProfileManager.getInstance(LoginActivity.this).saveProfile(name, email, phone);
                            navigateToHome();
                        }
                    } else {
                        String message = body.has("message") ? body.get("message").getAsString() : "Login Failed";
                        android.widget.Toast.makeText(LoginActivity.this, message, android.widget.Toast.LENGTH_SHORT).show();
                    }
                } else {
                    android.widget.Toast.makeText(LoginActivity.this, "Server Error", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.google.gson.JsonObject> call, Throwable t) {
                android.widget.Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserProfileAndNavigate(String userId, String email) {
        com.simats.prepace.network.RetrofitClient.getApiService().getUserProfile(userId).enqueue(new retrofit2.Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<com.google.gson.JsonObject> call, retrofit2.Response<com.google.gson.JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.google.gson.JsonObject body = response.body();
                    // Check if the response is wrapped in a "data" object or flat, handling common PHP API patterns
                    // Assuming flat or standard structure based on previous interactions
                    
                    String name = "User";
                    String phone = "";
                    String avatar = "";

                    // Adjust parsing based on actual response structure. 
                    // If the API returns { "status": "success", "data": { ... } } vs direct fields
                    com.google.gson.JsonObject data = body.has("data") ? body.getAsJsonObject("data") : body;

                    if (data.has("username")) name = data.get("username").getAsString();
                    if (data.has("full_name")) name = data.get("full_name").getAsString(); // Fallback
                    if (data.has("phone")) phone = data.get("phone").getAsString();
                    if (data.has("avatar")) avatar = data.get("avatar").getAsString();

                    // Save full profile
                    UserProfileManager userManager = UserProfileManager.getInstance(LoginActivity.this);
                    userManager.saveProfile(name, email, phone);
                    if (!avatar.isEmpty()) {
                        userManager.saveAvatarUri(avatar); // Saving URL/URI
                    }
                }
                // Navigate regardless of profile fetch success to avoid locking user out
                navigateToHome();
            }

            @Override
            public void onFailure(retrofit2.Call<com.google.gson.JsonObject> call, Throwable t) {
                // Navigate anyway
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        android.widget.Toast.makeText(LoginActivity.this, "Login Successful", android.widget.Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finishAffinity();
    }

}
