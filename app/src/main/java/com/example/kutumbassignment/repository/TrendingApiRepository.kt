package com.example.kutumbassignment.repository

import androidx.lifecycle.MutableLiveData
import com.example.kutumbassignment.dataClasses.BuiltByItem
import com.example.kutumbassignment.dataClasses.Favorites
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.database.TrendingApiDao
import com.example.kutumbassignment.retrofit.TrendingApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingApiRepository(private val dao: TrendingApiDao) {

    val trendingRepos = dao.getTrendingRepositories()
    val favorites = dao.getFavorites()
    val error = MutableLiveData<Boolean>()

    suspend fun refresh(){
        withContext(Dispatchers.IO){
            error.postValue(false)
            runCatching {
                val response = TrendingApiClient.api.getTrendingRepos().execute()
                if(!response.isSuccessful){
                    error.postValue(true)
                    return@withContext
                }
                error.postValue(false)
                response.body()?.let { dao.addTrendingRepository(it) }
            }.onFailure {
                error.postValue(true)
            }
        }
    }

    suspend fun loadDummyData(){
        error.postValue(false)
        val languages = listOf("Python", "C++", "Java")
        val languageColors = listOf("#ff8f00", "#0288d1", "#64dd17")
        val list = ArrayList<Repository>()
        for(i in 1..15){
            list.add(
                Repository(
                    rank = i,
                    username = "sherlock-project",
                    repositoryName = "sherlock",
                    url = "https://github.com/sherlock-project/sherlock",
                    description = "Hunt down social media accounts by username across social networks",
                    language = languages[(i-1)/5],
                    languageColor = languageColors[(i-1)/5],
                    totalStars = 21977,
                    forks = 2214,
                    builtBy = listOf(
                        BuiltByItem(
                        avatar = "https://avatars.githubusercontent.com/u/1666888?s=40&v=4",
                        url = "https://github.com/hoadlck",
                        username = "hoadlck"
                    )
                    ),
                    starsSince = 462,
                    since = "daily"
                )
            )
        }
        delay(1000)
        dao.addTrendingRepository(list)
    }

    suspend fun addFavorite(repository: Repository) {
        dao.addFavorite(Favorites(repository))
    }

    suspend fun removeFavorite(favorites: Favorites) {
        dao.deleteFavorite(favorites)
    }

}