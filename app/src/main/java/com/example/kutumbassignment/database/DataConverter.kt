package com.example.kutumbassignment.database

import androidx.room.TypeConverter
import com.example.kutumbassignment.dataClasses.BuiltByItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

    @TypeConverter
    fun fromBuiltByList(list: List<BuiltByItem>): String{
        val gson = Gson()
        val type = object: TypeToken<List<BuiltByItem>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toBuiltByList(string: String): List<BuiltByItem>{
        val gson = Gson()
        val type = object: TypeToken<List<BuiltByItem>>() {}.type
        return gson.fromJson(string, type)
    }

}