package com.demo.movies.data.remote

import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getPhotos(title : String, page: Int) =  apiHelper.getPhotos(title,page)

}