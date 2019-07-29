package com.isem.tmdbviewer.api;

import com.isem.tmdbviewer.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheMovieDbApiClient {
    private static <T> T builder(Class<T> endpoint) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.THE_MOVIE_DB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(endpoint);
    }

    public static TheMovieDbApi theMovieDbApi() {
        return builder(TheMovieDbApi.class);
    }
}
