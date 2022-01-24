package com.example.kutumbassignment.repository

import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.retrofit.TrendingApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingApiRepository {

    fun getTrendingRepos(onResponse: (List<Repository?>?, Boolean)->Unit){
        TrendingApiClient.api.getTrendingRepos().enqueue(object: Callback<List<Repository?>>{
            override fun onResponse(
                call: Call<List<Repository?>>,
                response: Response<List<Repository?>>
            ) {
                if(response.code()==200){
                    onResponse(response.body(), true)
                }else{
                    onResponse(null, false)
                }
            }

            override fun onFailure(call: Call<List<Repository?>>, t: Throwable) {
                onResponse(null, false)
            }

        })
    }

}