package com.simats.prepace.network;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ================= AUTH =================

    @POST("login.php")
    Call<JsonObject> login(@Body JsonObject loginRequest);

    @POST("signup.php")
    Call<JsonObject> register(@Body JsonObject registerRequest);

    // ================= USER =================

    @GET("profile.php")
    Call<JsonObject> getUserProfile(
            @Query("user_id") String userId
    );

    @POST("update_profile.php")
    Call<JsonObject> updateUserProfile(@Body JsonObject profileData);

    // ================= QUIZ =================

    @GET("quiz_list.php")
    Call<JsonObject> getQuizList();

    @GET("quiz_detail.php")
    Call<JsonObject> getQuizDetail(
            @Query("quiz_id") String quizId
    );

    @POST("submit_quiz.php")
    Call<JsonObject> submitQuiz(@Body JsonObject quizSubmission);

    // ================= EXTRA =================

    @GET("leaderboard.php")
    Call<JsonObject> getLeaderboard();

    @GET("achievements.php")
    Call<JsonObject> getAchievements();
}
