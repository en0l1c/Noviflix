package com.enolic.noviflix.api;

import com.enolic.noviflix.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // static instance
    private static Retrofit retrofit = null; // null at first, it will be filled when is needed

    // a private constructor (singleton) - to prevent from creating an object of ApiClient somewhere else
    private ApiClient() {}

    // singleton provider
    public static Retrofit getClient() {
        // if retrofit == null it will create a new instance
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // to capture requests/responses
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // capturing the whole content of the request (headers, body)

            // we creating an OkHttpClient for using it in RetroFit
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // okhttpclient has default timeout to 10sec
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(logging)
//                    .connectTimeout(30, TimeUnit.SECONDS) // how much time to connect with server
//                    .readTimeout(30, TimeUnit.SECONDS) // how much time to retrieve data from server
//                    .writeTimeout(30, TimeUnit.SECONDS) // how much time to write/post data to server
//                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // for converting json to java object
                    .client(client)
                    .build();
        }
        // if retrofit instance is not created, it will create and return a new instance
        // if retrofit is already created, it returns the current instance
        return retrofit;
    }
}
