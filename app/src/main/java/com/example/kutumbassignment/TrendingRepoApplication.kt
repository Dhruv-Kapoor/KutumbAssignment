package com.example.kutumbassignment

import android.app.Application
import com.example.kutumbassignment.database.TrendingApiDatabase
import com.example.kutumbassignment.repository.TrendingApiRepository

class TrendingRepoApplication: Application() {

    private val dao by lazy {
        TrendingApiDatabase.getDatabase(this).dao()
    }

    val repository by lazy {
        TrendingApiRepository(dao)
    }

}