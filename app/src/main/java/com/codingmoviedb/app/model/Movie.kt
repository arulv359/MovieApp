package com.codingmoviedb.app.model

import com.google.gson.annotations.SerializedName

class Movie {
    val id: String? = null

    val title: String? = null

    @SerializedName("poster_path")
    val posterPath: String? = null

    @SerializedName("backdrop_path")
    val thumbnail: String? = null

    @SerializedName("release_date")
    val releaseDate: String? = null

    val overview: String? = null

    @SerializedName("vote_average")
    val rating: String? = null

}