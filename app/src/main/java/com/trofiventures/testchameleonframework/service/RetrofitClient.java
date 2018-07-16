package com.trofiventures.testchameleonframework.service;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClient {

    private Retrofit retrofit;

    public RetrofitClient() {
        init();
    }

    private void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //builder.addInterceptor(new StethoInterceptor());
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://34.211.194.70:8080/ca-1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(builder.build())
                .build();
    }

    public ChameleonApi get() {
        if (retrofit != null)
            return retrofit.create(ChameleonApi.class);
        return null;
    }
}
