package com.demo.movies.data.model

import androidx.room.*
import kotlin.collections.ArrayList


@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey val title: String,
    val year: Int,
    val cast: ArrayList<String>?,
    val genres: ArrayList<String>?,
    val rating: Int,
)