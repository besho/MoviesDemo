package com.demo.movies.data.remote

import com.demo.movies.data.model.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/services/rest/?method=flickr.photos.search&format=json&nojsoncallback=50&per_page=20")
    suspend fun getPhotos(@Query("text")  title:String,@Query("page")  currentPage:Int
                          ,@Query("api_key")  apiKey:String): Response<PhotosResponse>

}