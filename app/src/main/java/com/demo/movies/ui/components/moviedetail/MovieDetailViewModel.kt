package com.demo.movies.ui.components.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.demo.movies.data.model.PhotosResponse
import com.demo.movies.data.remote.RemoteRepository
import com.demo.movies.utils.NetworkHelper
import com.demo.movies.utils.Resource
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val remoteRepository: RemoteRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _photosResponse = MutableLiveData<Resource<PhotosResponse>>()
    val photosResponse: LiveData<Resource<PhotosResponse>>
        get() = _photosResponse


    fun fetchPhotos(title: String, page: Int) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                remoteRepository.getPhotos(title,page).let {
                    if (it.isSuccessful ) {
                        if (it.body()?.stat == "ok")
                            _photosResponse.postValue(Resource.success(it.body()))
                        else
                            _photosResponse.postValue(Resource.error(it.body()!!.message, null))
                    } else _photosResponse.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _photosResponse.postValue(Resource.error("No Internet Connection!", null))
        }
    }
}