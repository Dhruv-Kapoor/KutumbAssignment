package com.example.kutumbassignment.database

import androidx.room.*
import com.example.kutumbassignment.dataClasses.Repository
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingApiDao {

    @Query("select * from Repository")
    fun getTrendingRepositories(): Flow<List<Repository>>

    @Update
    suspend fun updateFavorite(repository: Repository)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrendingRepository(items: List<Repository>)

    @Query("delete from Repository")
    fun deleteAllData()

}