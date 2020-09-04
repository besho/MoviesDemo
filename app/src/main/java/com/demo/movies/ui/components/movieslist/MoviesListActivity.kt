package com.demo.movies.ui.components.movieslist

import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.widget.Toolbar
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.movies.R
import com.demo.movies.data.model.Movie

import com.demo.movies.ui.base.BaseActivity
import com.demo.movies.ui.components.movieslist.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.item_list.*

@AndroidEntryPoint
class MoviesListActivity : BaseActivity() {

    private val mMoviesViewModel: MoviesViewModel by viewModels()
    private var twoPane: Boolean = false
    private var moviesList : ArrayList<Movie> = ArrayList()
    private lateinit var moviesAdapter: MoviesAdapter

    override val layoutId: Int
        get() = R.layout.activity_movies_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }

    }

    override fun initializeView() {

        setupMoviesRecyclerView()

        mMoviesViewModel.getAllMoviesList.observe(this, Observer {
            if (it.isNotEmpty())
            {
                progress_bar.visibility = View.GONE
                moviesList.clear()
                moviesList.addAll(it)
                moviesAdapter.notifyDataSetChanged()
            }
        })

    }

    private fun setupMoviesRecyclerView() {
        moviesAdapter = MoviesAdapter(this, moviesList, twoPane)

        moviesRV.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(this@MoviesListActivity)
        }
    }


}