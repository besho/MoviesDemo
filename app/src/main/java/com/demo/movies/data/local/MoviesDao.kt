package com.demo.movies.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.movies.data.model.Movie


@Dao
interface MoviesDao{

    @Query("SELECT * from movies_table ORDER BY title ASC")
    fun getAlphabetizedMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

}