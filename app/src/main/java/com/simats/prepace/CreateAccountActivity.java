package com.simats.prepace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.simats.prepace.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword;
    Button btnCreateAccount;
    TextView tvLogin;
    ImageView ivShowPassword;
    private boolean isRegistering = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvLogin = findViewById(R.id.tvLogin);
        ivShowPassword = findViewById(R.id.ivShowPassword);



        // Show/Hide Password
        ivShowPassword.setOnClickListener(v -> {
            if (etPassword.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                etPassword.setTransformationMethod(
                        android.text.method.HideReturnsTransformationMethod.getInstance());
            } else {
                etPassword.setTransformationMethod(
                        android.text.method.PasswordTransformationMethod.getInstance());
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnCreateAccount.setOnClickListener(v -> handleCreateAccount());
    }



    private void handleCreateAccount() {

        if (isRegistering) return;

        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        String error = validateFields(name, email, password);

        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return;
        }

        isRegistering = true;
        btnCreateAccount.setEnabled(false);

        registerUser(name, email, password);
    }

    private String validateFields(String name, String email, String password) {

        if (name.isEmpty()) {
            etFullName.requestFocus();
            return "Full name is required";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.requestFocus();
            return "Enter valid email";
        }

        if (password.length() < 6) {
            etPassword.requestFocus();
            return "Password must be at least 6 characters";
        }

        return null;
    }

    private void registerUser(String name, String email, String password) {

        JsonObject registerRequest = new JsonObject();
        registerRequest.addProperty("username", name);
        registerRequest.addProperty("email", email);
        registerRequest.addProperty("password", password);

        RetrofitClient.getApiService()
                .register(registerRequest)
                .enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        isRegistering = false;
                        btnCreateAccount.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {

                            JsonObject body = response.body();

                            if (body.has("status") &&
                                    body.get("status").getAsString().equals("success")) {

                                // Save user session
                                String userId = body.has("user_id") ? body.get("user_id").getAsString() : "";
                                UserProfileManager userManager = UserProfileManager.getInstance(CreateAccountActivity.this);
                                userManager.saveProfile(name, email, ""); // Phone is empty for now
                                if (!userId.isEmpty()) {
                                    userManager.saveUserId(userId);
                                }

                                Toast.makeText(
                                        CreateAccountActivity.this,
                                        body.get("message").getAsString(),
                                        Toast.LENGTH_SHORT
                                ).show();

                                // Registration successful — go straight to Home and clear the back stack
                                android.content.Intent intent = new android.content.Intent(CreateAccountActivity.this, HomeActivity.class);
                                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                // finish() not strictly required after the flags, but keep it to be explicit
                                finish();

                            } else {

                                String message = body.has("message")
                                        ? body.get("message").getAsString()
                                        : "Registration failed";

                                Toast.makeText(
                                        CreateAccountActivity.this,
                                        message,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {
                            Toast.makeText(
                                    CreateAccountActivity.this,
                                    "Server error",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        isRegistering = false;
                        btnCreateAccount.setEnabled(true);

                        Toast.makeText(
                                CreateAccountActivity.this,
                                "Network Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
