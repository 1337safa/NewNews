package com.my.projects.safin.newnews.internet

import com.my.projects.safin.newnews.constance.ServerData
import com.my.projects.safin.newnews.constance.ServerData.base_parameter_for_requests
import com.my.projects.safin.newnews.models.AboutNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(ServerData.get_url)
    fun getNews(): Call<AboutNews>

    @GET(base_parameter_for_requests)
    fun getNewsByParametrs(
        @Query("q") news_topic: String,
        @Query("from") data: String,
        @Query("sortBy") sort_by: String,
        @Query("apiKey") api_key: String
    ): Call<AboutNews>
}