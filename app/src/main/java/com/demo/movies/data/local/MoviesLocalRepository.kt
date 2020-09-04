package com.demo.movies.data.local

import javax.inject.Inject


class MoviesLocalRepository @Inject constructor (moviesRoomDatabase: MoviesRoomDatabase) {

    private val moviesDao = moviesRoomDatabase.moviesDao()

    fun getAllMovies() = moviesDao.getAllMovies()

    fun getFilteredMovies(searchKeyword:String?) = moviesDao.getFilteredMovies(searchKeyword)
}