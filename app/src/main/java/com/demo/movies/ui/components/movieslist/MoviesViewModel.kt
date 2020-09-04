package com.demo.movies.ui.components.movieslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.demo.movies.data.local.MoviesLocalRepository
import com.demo.movies.data.model.Movie
import com.demo.movies.utils.DUMMY_YEAR

class MoviesViewModel @ViewModelInject constructor(moviesLocalRepository: MoviesLocalRepository): ViewModel() {


    var mMoviesList = moviesLocalRepository.getAllMovies()
    private val mMoviesRepository = moviesLocalRepository

    fun startSearch(searchKeyword:String?) {
        mMoviesList = mMoviesRepository.getFilteredMovies(searchKeyword)
    }

    fun getMoviesCategorizedByYear(movies: List<Movie>): List<Movie>
    {
        val filterMoviesList = ArrayList<Movie>()

        var year = movies[0].year
        var count = 0
        filterMoviesList.add(Movie(title = year.toString(),genres = null,cast = null,rating = -1,year = DUMMY_YEAR))
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
                filterMoviesList.add(Movie(title = year.toString(),genres = null,cast = null,rating = -1,year = DUMMY_YEAR))
                filterMoviesList.add(movie)
                count = 1
            }
        }
        return filterMoviesList
    }
}
