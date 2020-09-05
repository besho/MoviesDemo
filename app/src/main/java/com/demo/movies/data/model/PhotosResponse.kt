package com.demo.movies.data.model

import com.google.gson.annotations.SerializedName


data class PhotosResponse (@SerializedName("photos") val photosList : PhotosList,val stat:String,val message:String)