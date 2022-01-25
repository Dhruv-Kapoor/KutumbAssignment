package com.example.kutumbassignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kutumbassignment.dataClasses.Repository

@Database(entities = [Repository::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class TrendingApiDatabase: RoomDatabase() {

    abstract fun dao():TrendingApiDao

    companion object{
        @Volatile
        private var INSTANCE: TrendingApiDatabase? = null

        fun getDatabase(context: Context): TrendingApiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrendingApiDatabase::class.java,
                    "TrendingApiDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}