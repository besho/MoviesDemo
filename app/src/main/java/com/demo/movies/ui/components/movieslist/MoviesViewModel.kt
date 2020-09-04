package com.demo.movies.ui.components.movieslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.demo.movies.data.local.MoviesLocalRepository
import com.demo.movies.data.model.Movie

class MoviesViewModel @ViewModelInject constructor(moviesLocalRepository: MoviesLocalRepository): ViewModel() {


    var getMoviesList = moviesLocalRepository.getAllMovies()
    val moviesRepository = moviesLocalRepository

    fun startSearch(searchKeyword:String?) {
        getMoviesList = moviesRepository.getFilteredMovies(searchKeyword)
    }

    fun getMoviesCategorizedByYear(movies: List<Movie>): List<Movie>
    {
        val filterMoviesList = ArrayList<Movie>()

        var year = movies[0].year
        var count = 0
        filterMoviesList.add(Movie(title = year.toString(),genres = null,cast = null,rating = -1,year = 0))
        for (movie in movies)
        {
            if (movie.year == year)
            {
                if (count < 5)
                {
                    filterMoviesList.add(movie)
                    count++
                }
            }
            else
            {
                year = movie.year
                filterMoviesList.add(Movie(title = year.toString(),genres = null,cast = null,rating = -1,year = 0))
                filterMoviesList.add(movie)
                count = 1
            }
        }
        return filterMoviesList
    }

}
