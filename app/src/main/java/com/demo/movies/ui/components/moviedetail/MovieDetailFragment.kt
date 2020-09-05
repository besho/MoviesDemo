package com.demo.movies.ui.components.moviedetail

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.demo.movies.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.movies.data.model.Movie
import com.demo.movies.data.model.Photo
import com.demo.movies.ui.components.moviedetail.adapters.RecyclerViewLoadMoreScroll
import com.demo.movies.utils.OnLoadMoreListener
import com.demo.movies.utils.Status
import com.demo.movies.utils.VIEW_TYPE_ITEM
import com.demo.movies.utils.VIEW_TYPE_LOADING
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.demo.movies.ui.components.moviedetail.adapters.PhotosAdapter
import kotlinx.android.synthetic.main.fragment_movie_detail.*


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var mMovie: Movie? = null
    private val mMovieDetailViewModel : MovieDetailViewModel by viewModels()
    private lateinit var mPhotosAdapter: PhotosAdapter
    lateinit var mScrollListener: RecyclerViewLoadMoreScroll
    private var mPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_MOVIE)) {
                mMovie = Gson().fromJson(it.getString(ARG_MOVIE),Movie::class.java)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = mMovie?.title
        setupUI()
        setupObserver()
        callFetchPhotos()
    }

    private fun setupUI() {

        mMovie?.cast?.let {
            if (it.size ==0)
                castLV.visibility = View.GONE
            else
            {
                castLV.visibility = View.VISIBLE
                castTV.text = TextUtils.join(", ", it)
            }
        }

        mMovie?.genres?.let {
            if (it.size ==0)
                genresLV.visibility = View.GONE
            else
            {
                genresLV.visibility = View.VISIBLE
                genresTV.text = TextUtils.join(", ", it)
            }
        }

        yearTV.text = mMovie?.year.toString()
        rateTV.text = mMovie?.rating.toString()

        setupPhotosRecycler()
    }

    private fun setupPhotosRecycler() {
        photosRV.layoutManager = GridLayoutManager(activity, 2)
        photosRV.isNestedScrollingEnabled = false
        mPhotosAdapter = PhotosAdapter(arrayListOf())
        photosRV.adapter = mPhotosAdapter

        mScrollListener = RecyclerViewLoadMoreScroll(photosRV.layoutManager as GridLayoutManager)

        (photosRV.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (mPhotosAdapter.getItemViewType(position)) {
                        VIEW_TYPE_ITEM -> 1
                        VIEW_TYPE_LOADING -> 2 //number of columns of the grid
                        else -> -1
                    }
                }
            }
        mScrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loadMorePhotos()
            }
        })
        movieNSC.setOnScrollChangeListener(mScrollListener)
    }

    private fun loadMorePhotos() {
        mPage++
        mPhotosAdapter.addLoadingView()
        callFetchPhotos()
    }

    private fun setupObserver() {
        mMovieDetailViewModel.photosResponse.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { 
                        if (it.photosList.total == "0" )
                            noPhotosTV.visibility = View.VISIBLE
                        else {
                            renderList(it.photosList.photosList)
                            photosRV.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    it.message?.let { it1 -> showReloadSnackBar(it1) }
                }
            }
        })
    }

    private fun showReloadSnackBar(message:String)
    {
        Snackbar.make(movieDetailsCV, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.reload) {
            if (mPage ==1)
                progressBar.visibility = View.VISIBLE
            callFetchPhotos()
        }.show()
    }

    private fun callFetchPhotos() {
        mMovie?.title?.let { mMovieDetailViewModel.fetchPhotos(it, mPage) }
    }

    private fun renderList(photosList: List<Photo>) {
       mPhotosAdapter.removeLoadingView()
       //Change the boolean isLoading to false
       mScrollListener.setLoaded()

       mPhotosAdapter.addPhotos(photosList)
       mPhotosAdapter.notifyDataSetChanged()
    }

    companion object {
        const val ARG_MOVIE = "movie"
    }
}