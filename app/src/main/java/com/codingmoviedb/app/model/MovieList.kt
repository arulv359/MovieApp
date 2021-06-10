package com.codingmoviedb.app.model

import com.google.gson.annotations.SerializedName

data class MovieList(
        @SerializedName("results")
        var movieResult: List<Movie>,
        var page: Int,
        @SerializedName("total_pages")
        var totalPages: Int
)