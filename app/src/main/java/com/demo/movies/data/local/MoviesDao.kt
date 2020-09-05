package com.demo.movies.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.movies.data.model.Movie


@Dao
interface MoviesDao{

    @Query("SELECT * from movies_table ORDER BY year DESC, rating DESC")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * from movies_table WHERE title LIKE '%' || :searchKeyword || '%' OR genres LIKE '%' || :searchKeyword || '%' OR `cast` LIKE '%' || :searchKeyword || '%' OR year LIKE '%' || :searchKeyword || '%' ORDER BY year DESC, rating DESC")
    fun getFilteredMovies(searchKeyword:String?): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

}