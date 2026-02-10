package com.simats.prepace.network;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface ApiService {
    
    // Base URL: http://14.139.187.229:8081/april_2025_batch/spic_723/prepace/
    
    @GET("users")
    Call<JsonObject> getUsers();
    
    @POST("login")
    Call<JsonObject> login(@Body JsonObject loginRequest);
    
    @POST("register")
    Call<JsonObject> register(@Body JsonObject registerRequest);
    
    @GET("quiz/list")
    Call<JsonObject> getQuizList();
    
    @GET("quiz/detail")
    Call<JsonObject> getQuizDetail(@Query("id") String quizId);
    
    @POST("quiz/submit")
    Call<JsonObject> submitQuiz(@Body JsonObject quizSubmission);
    
    @GET("user/profile")
    Call<JsonObject> getUserProfile();
    
    @POST("user/profile/update")
    Call<JsonObject> updateUserProfile(@Body JsonObject profileData);
    
    @GET("achievements")
    Call<JsonObject> getAchievements();
    
    @GET("leaderboard")
    Call<JsonObject> getLeaderboard();
    
}
