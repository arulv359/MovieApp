package com.codingmoviedb.app.network;

import com.codingmoviedb.app.model.MovieDetail;
import com.codingmoviedb.app.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //GET popular movie list
    @GET("movie/popular")
    Call<MovieList> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") int page
    );

    //GET movie detail
    @GET("movie/{movieId}")
    Call<MovieDetail> getMovieDetail(
            @Path(value = "movieId", encoded = true) String movieId,
            @Query("api_key") String key
    );

}
