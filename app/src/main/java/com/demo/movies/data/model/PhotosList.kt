package com.demo.movies.data.model

import com.google.gson.annotations.SerializedName

data class PhotosList(val page : Int,
                      val pages : Int,
                      @SerializedName("perpage") val perPage : Int,
                      val total : String,
                      @SerializedName("photo") val photosList : List<Photo>)