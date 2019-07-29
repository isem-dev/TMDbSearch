package com.isem.tmdbviewer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.isem.tmdbviewer.BuildConfig;
import com.isem.tmdbviewer.R;
import com.isem.tmdbviewer.api.TheMovieDbApiClient;
import com.isem.tmdbviewer.model.MovieItem;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mReleaseView;
    private TextView mVoteView;
    private TextView mOverviewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitleView = findViewById(R.id.original_title_textview);
        mPosterView = findViewById(R.id.movie_poster_thumbnail_imageview);
        mReleaseView = findViewById(R.id.release_date_textview);
        mVoteView = findViewById(R.id.user_rating_vote_textview);
        mOverviewView = findViewById(R.id.plot_synopsis_overview_textview);

        Intent intent = getIntent();
        fetchMovie(intent.getIntExtra(MainActivity.MOVIE_ID, 0));
    }

    private void fetchMovie(int movieId) {
        TheMovieDbApiClient.theMovieDbApi().movieDetails(movieId, BuildConfig.THE_MOVIE_DB_API_KEY)
                .enqueue(new Callback<MovieItem>(){
                    @Override
                    public void onResponse(Call<MovieItem> call, Response<MovieItem> response) {
                        final MovieItem movie = response.body();
                        String imageUri = BuildConfig.THE_MOVIE_DB_API_IMAGE_BASE_URL
                                + BuildConfig.THE_MOVIE_DB_API_IMAGE_SIZE
                                + movie.getPosterPath();
                        mTitleView.setText(movie.getTitle());
                        mReleaseView.setText(movie.getReleaseDate());
                        mVoteView.setText(movie.getVoteAverage().toString());
                        mOverviewView.setText(movie.getOverview());
                        Picasso.get().load(imageUri).into(mPosterView);
                    }

                    @Override
                    public void onFailure(Call<MovieItem> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
    }

}
