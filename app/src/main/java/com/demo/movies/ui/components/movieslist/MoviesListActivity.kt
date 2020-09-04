package com.demo.movies.ui.components.movieslist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.movies.R
import com.demo.movies.data.model.Movie
import com.demo.movies.ui.base.BaseActivity
import com.demo.movies.ui.components.movieslist.adapters.MoviesAdapter
import com.google.android.material.snackbar.Snackbar
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
        mMoviesViewModel.getMoviesList.observe(this, Observer {
            if (it.isNotEmpty()) {
                progress_bar.visibility = View.GONE
                moviesList.addAll(it)
                moviesAdapter.notifyDataSetChanged()

            }
        })
    }

    private fun startSearch(searchKeyword: String?)
    {
        mMoviesViewModel.startSearch(searchKeyword)

        resetMoviesList()

        mMoviesViewModel.getMoviesList.observe(this, Observer {
            if (it.isNotEmpty()) {
                progress_bar.visibility = View.GONE
                if (searchKeyword.equals(""))
                    moviesList.addAll(it)
                else
                    moviesList.addAll(mMoviesViewModel.getMoviesCategorizedByYear(it))
                moviesAdapter.notifyDataSetChanged()

            } else {
                progress_bar.visibility = View.GONE
                Snackbar.make(moviesCL, R.string.no_search_result, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun resetMoviesList() {
        progress_bar.visibility = View.VISIBLE
        moviesList.clear()
        moviesAdapter.notifyDataSetChanged()
    }

    private fun setupMoviesRecyclerView() {
        moviesAdapter = MoviesAdapter(this, moviesList, twoPane)

        moviesRV.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(this@MoviesListActivity)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.movies_search, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(searchKeyword: String?): Boolean {
                startSearch(searchKeyword)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}