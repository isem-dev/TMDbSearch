package com.isem.tmdbviewer.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isem.tmdbviewer.BuildConfig;
import com.isem.tmdbviewer.R;
import com.isem.tmdbviewer.model.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<MovieItem> mMovieItemList;
    private int mItemLayout;
    private Context mContext;

    public MovieAdapter(List<MovieItem> movieItemList,
                        int itemLayout,
                        Context context) {
        this.mMovieItemList = movieItemList;
        this.mItemLayout = itemLayout;
        this.mContext = context;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout movieItemLayout;
        TextView movieTitle;
        ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieItemLayout = itemView.findViewById(R.id.movie_layout);
            movieImage = itemView.findViewById(R.id.movie_image);
            movieTitle = itemView.findViewById(R.id.movie_title);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String imageUri = BuildConfig.THE_MOVIE_DB_API_IMAGE_BASE_URL
                + BuildConfig.THE_MOVIE_DB_API_IMAGE_SIZE
                + mMovieItemList.get(position).getPosterPath();
        Log.d(TAG, imageUri);
        Picasso.get().load(imageUri).into(holder.movieImage);
        holder.movieTitle.setText(mMovieItemList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovieItemList.size();
    }

}
