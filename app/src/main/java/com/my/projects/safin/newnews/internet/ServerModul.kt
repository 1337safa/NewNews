package com.my.projects.safin.newnews.internet

import com.my.projects.safin.newnews.constance.ServerData
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ServerModul() {
    fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(ServerData.base_url)
            .addConverterFactory(moshiConverterFactory)
            .client(client)
            .build()
        return retrofit
    }
    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    private val moshi = Moshi.Builder()
        .build()
    private val moshiConverterFactory = MoshiConverterFactory.create(moshi)
}