package com.example.kutumbassignment

import androidx.lifecycle.*
import com.example.kutumbassignment.dataClasses.BuiltByItem
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.repository.TrendingApiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TrendingApiRepository): ViewModel(){

    private val trendingRepos = MutableLiveData<List<Repository>?>()
    private val loadingState = MutableLiveData(false)
    private val errorState = MutableLiveData(false)

    init {
        refresh()
    }

    fun refresh(){
        loadingState.value = true
        errorState.value = false
        repository.getTrendingRepos { trendingReposResponse, isSuccess ->
            loadingState.value = false
            if(isSuccess){
                trendingRepos.value = trendingReposResponse
            }else{
                errorState.value = true
            }
        }
    }

    fun getTrendingReposLD() = trendingRepos as LiveData<List<Repository>?>

    fun getLoadingStateLD() = loadingState as LiveData<Boolean>

    fun getErrorStateLD() = errorState as LiveData<Boolean>


    fun loadDummyData() {
        loadingState.value = true
        errorState.value = false

        viewModelScope.launch {
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
                        builtBy = listOf(BuiltByItem(
                            avatar = "https://avatars.githubusercontent.com/u/1666888?s=40&v=4",
                            url = "https://github.com/hoadlck",
                            username = "hoadlck"
                        )),
                        starsSince = 462,
                        since = "daily"
                    )
                )
            }
            delay(2000)
            loadingState.postValue(false)
            trendingRepos.postValue(list)
        }
    }

}

class MainActivityViewModelFactory(private val repository: TrendingApiRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}