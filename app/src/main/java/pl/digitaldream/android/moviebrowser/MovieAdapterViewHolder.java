package pl.digitaldream.android.moviebrowser;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wbaranowski on 09.04.2017.
 */
class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String TAG = MovieAdapterViewHolder.class.getSimpleName();

    private MovieProvider movieAdapter;
    private final MovieAdapterOnClickHandler mClickHandler;
    private final ImageView mMovieImageView;

    MovieAdapterViewHolder(MovieProvider movieAdapter,
                           View itemView,
                           MovieAdapterOnClickHandler mClickHandler) {
        super(itemView);
        this.movieAdapter = movieAdapter;
        this.mClickHandler = mClickHandler;
        this.mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Click on: " + movieAdapter.provideMovie(getAdapterPosition()).getTitle());
        mClickHandler.onClick(movieAdapter.provideMovie(getAdapterPosition()));
    }

    public ImageView getmMovieImageView() {
        return mMovieImageView;
    }
}
