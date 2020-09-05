package com.demo.movies.data.remote

import com.demo.movies.data.model.PhotosResponse
import com.demo.movies.utils.FLICKER_API_KEY
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getPhotos(title:String,page: Int): Response<PhotosResponse> = apiService.getPhotos(title,page,FLICKER_API_KEY)

}