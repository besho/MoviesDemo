package com.demo.movies.data.local

import com.demo.movies.data.model.Movie
import java.util.concurrent.Executors
import javax.inject.Inject


class MoviesLocalRepository @Inject constructor (moviesRoomDatabase: MoviesRoomDatabase) {

    private val moviesDao = moviesRoomDatabase.moviesDao()

    fun getAllMovies() = moviesDao.getAlphabetizedMovies()

}