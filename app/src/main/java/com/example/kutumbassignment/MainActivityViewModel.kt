package com.example.kutumbassignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.repository.TrendingApiRepository

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