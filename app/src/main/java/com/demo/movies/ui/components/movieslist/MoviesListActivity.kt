package com.demo.movies.ui.components.movieslist

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_movies_list.progressBar
import kotlinx.android.synthetic.main.item_list.*


@AndroidEntryPoint
class MoviesListActivity : BaseActivity() {

    private val mMoviesViewModel: MoviesViewModel by viewModels()
    private var mTwoPane: Boolean = false
    private var mMoviesList : ArrayList<Movie> = ArrayList()
    private lateinit var mMoviesAdapter: MoviesAdapter

    override val layoutId: Int
        get() = R.layout.activity_movies_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            mTwoPane = true
        }

    }

    override fun initializeView() {
        setupMoviesRecyclerView()
        setupObserver()
    }

    private fun setupObserver() {
        mMoviesViewModel.mMoviesList.observe(this, Observer {
            if (it.isNotEmpty()) {
                progressBar.visibility = View.GONE
                mMoviesList.addAll(it)
                mMoviesAdapter.notifyDataSetChanged()

            }
        })
    }

    private fun startSearch(searchKeyword: String?)
    {
        mMoviesViewModel.startSearch(searchKeyword)

        resetMoviesList()

        mMoviesViewModel.mMoviesList.observe(this, Observer {
            if (it.isNotEmpty()) {
                progressBar.visibility = View.GONE
                if (searchKeyword.equals(""))
                    mMoviesList.addAll(it)
                else
                    mMoviesList.addAll(mMoviesViewModel.getMoviesCategorizedByYear(it))
                mMoviesAdapter.notifyDataSetChanged()

            } else {
                progressBar.visibility = View.GONE
                Snackbar.make(moviesCL, R.string.no_search_result, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetMoviesList() {
        progressBar.visibility = View.VISIBLE
        mMoviesList.clear()
        mMoviesAdapter.notifyDataSetChanged()
    }

    private fun setupMoviesRecyclerView() {
        mMoviesAdapter = MoviesAdapter(this, mMoviesList, mTwoPane)

        moviesRV.apply {
            adapter = mMoviesAdapter
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
                startSearch(query)
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                if (query?.isEmpty()!!)
                    startSearch(query)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}