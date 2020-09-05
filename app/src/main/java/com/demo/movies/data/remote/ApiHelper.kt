package com.demo.movies.data.remote

import com.demo.movies.data.model.PhotosResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getPhotos(title : String, page: Int): Response<PhotosResponse>
}