package com.example.kutumbassignment.database

import androidx.room.*
import com.example.kutumbassignment.dataClasses.Favorites
import com.example.kutumbassignment.dataClasses.Repository
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingApiDao {

    @Query("select * from Repository")
    fun getTrendingRepositories(): Flow<List<Repository>>

    @Query("select * from Favorites")
    fun getFavorites(): Flow<List<Favorites>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorites)

    @Delete
    suspend fun deleteFavorite(favorite: Favorites)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrendingRepository(items: List<Repository>)

    @Query("delete from Repository")
    fun deleteAllData()

}