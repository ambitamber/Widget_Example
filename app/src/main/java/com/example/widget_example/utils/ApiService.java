package com.example.widget_example.utils;

import com.example.widget_example.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v2/top-headlines")
    Call<News> getheadlines(
            @Query("country") String country,
            @Query("apikey") String key
    );
}
