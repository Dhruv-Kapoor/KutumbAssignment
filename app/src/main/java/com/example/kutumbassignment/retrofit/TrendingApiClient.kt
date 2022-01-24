package com.example.kutumbassignment.retrofit

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrendingApiClient {
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gh-trending-api.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api = retrofit.create(TrendingApiService::class.java)
}