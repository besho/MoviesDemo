package com.demo.movies.ui.components.movieslist.adapters

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.R
import com.demo.movies.data.model.Movie
import com.demo.movies.ui.components.ItemDetailActivity
import com.demo.movies.ui.components.ItemDetailFragment
import com.demo.movies.ui.components.movieslist.MoviesListActivity

class MoviesAdapter(private val parentActivity: MoviesListActivity,
                    private val moviesList: List<Movie>,
                    private val twoPane: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    private val MOVIE_ITEM  = 0
    private val Category_ITEM  = 1

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Movie
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_ID, item.title)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.title)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (moviesList[position].year == 0)
            Category_ITEM
        else
            MOVIE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Category_ITEM ) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
            CategoryViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            MovieViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)){
            MOVIE_ITEM -> {
                val movie = moviesList[position]

                val movieHolder = holder as MovieViewHolder
                movieHolder.titleView.text = movie.title
                movieHolder.castView.text = movie.cast?.let {
                    TextUtils.join(", ",
                        it
                    )
                }
                movieHolder.genreView.text = movie.genres?.let {
                    TextUtils.join(", ",
                        it
                    )
                }
                movieHolder.yearView.text = movie.year.toString()
                movieHolder.rate.text = movie.rating.toString()

                with(holder.itemView) {
                    tag = movie
                    setOnClickListener(onClickListener)
                }
            }
            else -> {
                val category = moviesList[position]

                val categoryHolder = holder as CategoryViewHolder
                categoryHolder.categoryView.text = category.title
            }
        }


    }

    override fun getItemCount() = moviesList.size

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.titleTV)
        val yearView: TextView = view.findViewById(R.id.yearTV)
        val castView: TextView = view.findViewById(R.id.castTV)
        val genreView: TextView = view.findViewById(R.id.genreTV)
        val rate: TextView = view.findViewById(R.id.rateTV)
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryView: TextView = view.findViewById(R.id.categoryTV)
    }

}