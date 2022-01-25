package com.example.kutumbassignment

import androidx.lifecycle.*
import com.example.kutumbassignment.dataClasses.Favorites
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.repository.TrendingApiRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TrendingApiRepository): ViewModel(){

    private val trendingRepos = repository.trendingRepos.asLiveData()
    private val loadingState = MutableLiveData(false)
    private val repoErrorState = repository.error as LiveData<Boolean>

    init {
        refresh()
    }

    fun refresh(){
        loadingState.postValue(true)
        viewModelScope.launch {
            repository.refresh()
            loadingState.postValue(false)
        }
    }

    fun getTrendingReposLD() = trendingRepos as LiveData<List<Repository>?>

    fun getLoadingStateLD() = loadingState as LiveData<Boolean>

    fun getErrorStateLD() = repoErrorState as LiveData<Boolean>


    fun loadDummyData() {
        loadingState.value = true
        viewModelScope.launch {
            repository.loadDummyData()
            loadingState.postValue(false)
        }
    }

    fun addFavorite(repo: Repository) {
        viewModelScope.launch {
            repository.addFavorite(repo)
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