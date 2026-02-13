package com.simats.prepace.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://14.139.187.229:8081/april_2025_batch/spic_723/prepace/";

    /**
     * Get the Retrofit instance
     * If instance doesn't exist, create a new one
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
                    .client(createOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    /**
     * Get the API Service instance
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    /**
     * Create OkHttpClient with logging and timeouts
     */
    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Create Gson instance for serialization/deserialization
     */
    private static Gson createGson() {
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Reset the Retrofit instance (useful for testing or changing base URL)
     */
    public static void resetRetrofit() {
        retrofit = null;
    }
}
