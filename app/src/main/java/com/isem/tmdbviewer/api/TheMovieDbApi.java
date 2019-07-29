package com.isem.tmdbviewer.api;

import com.isem.tmdbviewer.model.MovieItem;
import com.isem.tmdbviewer.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbApi {

    //Movie search by query
    @GET("/3/search/movie")
    Call<MovieResponse> search(@Query("api_key") String apiKey, @Query("query") String query);

    //Movie item details by id
    @GET("/3/movie/{id}")
    Call<MovieItem> movieDetails(@Path("id") int movieID, @Query("api_key") String apiKey);
}
