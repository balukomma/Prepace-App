package com.simats.prepace.network;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.JsonObject;

/**
 * Example Usage of Retrofit API Client
 * 
 * This class demonstrates how to use the RetrofitClient and ApiService
 * to make API calls to your backend server.
 * 
 * Your API Base URL: http://14.139.187.229:8081/april_2025_batch/spic_723/prepace/
 */
public class ApiUsageExample {
    
    /**
     * Example: Login API Call
     */
    public static void loginUser(AppCompatActivity activity, String email, String password) {
        ApiService apiService = RetrofitClient.getApiService();
        
        // Create login request body
        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("email", email);
        loginRequest.addProperty("password", password);
        
        // Make the API call
        Call<JsonObject> call = apiService.login(loginRequest);
        
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    JsonObject result = response.body();
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Process the result and navigate to next screen
                } else {
                    // Handle error response
                    Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle network error
                Toast.makeText(activity, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Example: Register API Call
     */
    public static void registerUser(AppCompatActivity activity, String name, String email, String password) {
        ApiService apiService = RetrofitClient.getApiService();

        // Create register request body
        JsonObject registerRequest = new JsonObject();
        registerRequest.addProperty("username", name);
        registerRequest.addProperty("email", email);
        registerRequest.addProperty("password", password);

        Call<JsonObject> call = apiService.register(registerRequest);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(activity, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Example: Get Quiz List API Call
     */
    public static void fetchQuizList(AppCompatActivity activity) {
        ApiService apiService = RetrofitClient.getApiService();
        
        Call<JsonObject> call = apiService.getQuizList();
        
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject quizList = response.body();
                    // Parse and display quiz list
                    Toast.makeText(activity, "Quiz List Loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Failed to load quiz list", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(activity, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Example: Get User Profile API Call
     */
    public static void fetchUserProfile(AppCompatActivity activity, String userId) {
        ApiService apiService = RetrofitClient.getApiService();
        
        Call<JsonObject> call = apiService.getUserProfile(userId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject userProfile = response.body();
                    // Display user profile
                    Toast.makeText(activity, "Profile Loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(activity, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Example: Submit Quiz API Call
     */
    public static void submitQuiz(AppCompatActivity activity, String quizId, JsonObject answers) {
        ApiService apiService = RetrofitClient.getApiService();
        
        JsonObject submission = new JsonObject();
        submission.addProperty("quizId", quizId);
        // Add your answers here - assuming answers is already a JsonObject or needs to be added
        if (answers != null) {
             submission.add("answers", answers); 
        }
        
        Call<JsonObject> call = apiService.submitQuiz(submission);
        
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject result = response.body();
                    Toast.makeText(activity, "Quiz Submitted Successfully", Toast.LENGTH_SHORT).show();
                    // Show results
                } else {
                    Toast.makeText(activity, "Failed to submit quiz", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(activity, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Example: Get Leaderboard API Call
     */
    public static void fetchLeaderboard(AppCompatActivity activity) {
        ApiService apiService = RetrofitClient.getApiService();
        
        Call<JsonObject> call = apiService.getLeaderboard();
        
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject leaderboard = response.body();
                    // Display leaderboard
                    Toast.makeText(activity, "Leaderboard Loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(activity, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
