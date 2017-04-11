package pl.digitaldream.android.moviebrowser;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.network.ImagesDownloader;

/**
 * Created by wbaranowski on 26.03.2017.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapterViewHolder> implements MovieProvider {
    private static final String TAG = MovieAdapter.class.getSimpleName();


    private List<Movie> movies = new ArrayList<>();

    private final Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    MovieAdapter(MovieActivity movieActivity) {
        context = movieActivity.getApplicationContext();
        mClickHandler = movieActivity;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        Log.v(TAG, "Creating view holder");
        return new MovieAdapterViewHolder(this, view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String imagePath = movies.get(position).getPosterPath();
        ImageView target = movieAdapterViewHolder.getmMovieImageView();
        ImagesDownloader.getInstance().fetchImageFromMovieDb(context, imagePath, target);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    void addMovies(List<Movie> movieData) {
        this.movies.addAll(movieData);
        notifyDataSetChanged();
    }

    void reset() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public Movie provideMovie(int adapterPosition) {
        return movies.get(adapterPosition);
    }

    public ArrayList<? extends Parcelable> getMovies() {
        return new ArrayList<>(movies);
    }
}
