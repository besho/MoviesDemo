package com.demo.movies.data.local
import androidx.room.*
import com.demo.movies.data.model.Movie
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Database(entities = [Movie::class], version = 1)
@TypeConverters(Converters::class)
abstract class MoviesRoomDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}

class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String {
        return Gson().toJson(list)
    }
}