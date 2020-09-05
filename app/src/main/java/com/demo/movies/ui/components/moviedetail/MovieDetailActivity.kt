package com.demo.movies.ui.components.moviedetail

import android.os.Bundle
import android.view.MenuItem
import com.demo.movies.R
import com.demo.movies.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_movie_detail


    override fun initializeView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        MovieDetailFragment.ARG_MOVIE,
                            intent.getStringExtra(MovieDetailFragment.ARG_MOVIE))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}