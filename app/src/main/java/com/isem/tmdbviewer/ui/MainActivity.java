package com.isem.tmdbviewer.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isem.tmdbviewer.BuildConfig;
import com.isem.tmdbviewer.R;
import com.isem.tmdbviewer.api.TheMovieDbApiClient;
import com.isem.tmdbviewer.model.MovieItem;
import com.isem.tmdbviewer.model.MovieResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String mSearchString = null;

    private Context mContext;
    private RecyclerView mRecyclerViewMovies;
    private List<MovieItem> mMovieItems;
    private MovieAdapter mMovieAdapter;

    public static final String MOVIE_ID = "movieId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        mRecyclerViewMovies = findViewById(R.id.rv_movies);
        mRecyclerViewMovies.setHasFixedSize(true);
        mRecyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerViewMovies.addOnItemTouchListener(
                new RecyclerTouchListener(mContext, mRecyclerViewMovies, new ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                new Handler().postDelayed(() -> {
                    Intent intent1 = new Intent(getApplicationContext(), DetailsActivity.class);
                    intent1.putExtra(MOVIE_ID, mMovieItems.get(position).getId());
                    startActivity(intent1);
                }, 400);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        fetchMovies(mSearchString);
    }

    private void fetchMovies(String searchString) {
        if (searchString != null) {
            TheMovieDbApiClient.theMovieDbApi().search(BuildConfig.THE_MOVIE_DB_API_KEY, searchString)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            Log.i(TAG, call.request().toString());
                            mMovieItems = response.body().getResults();
                            mMovieAdapter = new MovieAdapter(mMovieItems,
                                    R.layout.item_main_movie_card,
                                    mContext);
                            mRecyclerViewMovies.setAdapter(mMovieAdapter);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
        }
    }

    private void search(final SearchView searchView) {
        RxSearchObservable.fromView(searchView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(text -> {
                    if (text.isEmpty()) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> fetchMovies(result));
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent motionEvent) {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        search(searchView);

        return true;
    }

}
