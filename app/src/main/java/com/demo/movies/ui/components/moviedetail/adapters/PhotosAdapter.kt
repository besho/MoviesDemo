package com.demo.movies.ui.components.moviedetail.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.movies.R
import com.demo.movies.data.model.Photo
import com.demo.movies.utils.PHOTO_URL
import com.demo.movies.utils.VIEW_TYPE_ITEM
import com.demo.movies.utils.VIEW_TYPE_LOADING
import kotlinx.android.synthetic.main.item_photo.view.*

const val PHOTO_LOADING_DUMMY_ID = "-999"
class PhotosAdapter(
    private val photosList: ArrayList<Photo>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_photo, parent,
                    false
                )
            )
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loadmore, parent, false)
            LoadingViewHolder(view)
        }
    }


    override fun getItemCount(): Int = photosList.size

    override fun getItemViewType(position: Int): Int {
        return if (photosList[position].id == PHOTO_LOADING_DUMMY_ID) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM -> {
                (holder as PhotoViewHolder).bind(photosList[position])
            }
        }
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo : Photo) {
            Glide.with(itemView.photoIV.context)
                .load(String.format(PHOTO_URL, photo.farm,photo.server,photo.id,photo.secret) )
                .into(itemView.photoIV)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addPhotos(photos: List<Photo>) {
        photosList.addAll(photos)
    }

    fun addLoadingView() {
        //Add loading item
        Handler().post {
            photosList.add(Photo(id = PHOTO_LOADING_DUMMY_ID,owner = "",secret = "",server = "",farm = -1,title = "",isPublic = 0,isFamily = 0,isFriend = 0))
            notifyItemInserted(photosList.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (photosList.size != 0 && photosList.get(photosList.size-1).id == PHOTO_LOADING_DUMMY_ID) {
            photosList.removeAt(photosList.size - 1)
            notifyItemRemoved(photosList.size)
        }
    }
}