package com.example.kutumbassignment

import android.app.Application
import com.example.kutumbassignment.repository.TrendingApiRepository

class TrendingRepoApplication: Application() {

    val repository by lazy {
        TrendingApiRepository()
    }

}