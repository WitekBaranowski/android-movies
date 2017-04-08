package pl.digitaldream.android.moviebrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pl.digitaldream.android.moviebrowser.model.Movie;

/**
 * Created by wbaranowski on 26.03.2017.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> movies = new ArrayList<>();

    private final Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    MovieAdapter(MainActivity mainActivity) {
        context = mainActivity.getApplicationContext();
        mClickHandler = mainActivity;
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        Log.v(TAG, "Creating view holder");
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String imagePath = movies.get(position).getPosterPath();
        ImageView target = movieAdapterViewHolder.mMovieImageView;
        MoviesDownloader.getInstance()
                .fetchImageFromMovieDb(context, imagePath, target);
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


    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mMovieImageView;

        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Click on: "+movies.get(getAdapterPosition()).getTitle());
            mClickHandler.onClick(movies.get(getAdapterPosition()));
        }
    }
}
