package com.example.kutumbassignment.retrofit

import com.example.kutumbassignment.dataClasses.Repository
import retrofit2.Call
import retrofit2.http.GET

interface TrendingApiService {

    @GET("repositories")
    fun getTrendingRepos(): Call<List<Repository?>>

}