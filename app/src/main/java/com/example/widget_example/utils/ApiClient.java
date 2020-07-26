package com.example.widget_example.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){

        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl("https://newsapi.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
