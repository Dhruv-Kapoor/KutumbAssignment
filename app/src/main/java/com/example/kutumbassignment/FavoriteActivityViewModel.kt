package com.example.kutumbassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kutumbassignment.dataClasses.Favorites
import com.example.kutumbassignment.repository.TrendingApiRepository
import kotlinx.coroutines.launch

class FavoriteActivityViewModel(private val repository: TrendingApiRepository): ViewModel() {

    val favorites = repository.favorites.asLiveData()

    fun removeFavorite(favorites: Favorites) {
        viewModelScope.launch {
            repository.removeFavorite(favorites)
        }
    }
}

class FavoriteActivityViewModelFactory(private val repository: TrendingApiRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}