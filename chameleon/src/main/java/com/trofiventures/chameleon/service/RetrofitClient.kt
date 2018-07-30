package com.trofiventures.chameleon.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient {

    private var retrofit: Retrofit? = null

    init {
        init()
    }

    private fun init() {
        val builder = OkHttpClient.Builder()

        //builder.addInterceptor(new StethoInterceptor());
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // todo change the url to production
        // baseUrl method
        retrofit = Retrofit.Builder()
                .baseUrl("http://mcpdev.mozido.com/corpay/ci/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(builder.build())
                .build()
    }

    fun get() = retrofit?.create(ChameleonApi::class.java)
}
