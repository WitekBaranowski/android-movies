package pl.digitaldream.android.moviebrowser;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Date;

import pl.digitaldream.android.moviebrowser.data.FavMovieColumns;
import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.network.ImagesDownloader;

/**
 * Created by sam_chordas on 8/12/15.
 * Credit to skyfishjy gist:
 * https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */
public class MovieCursorAdapter extends CursorRecyclerViewAdapter<MovieAdapterViewHolder>
        implements MovieProvider {
    private Context mContext;
    private MovieAdapterOnClickHandler mClickHandler;

    public MovieCursorAdapter(Context context, Cursor cursor, MovieAdapterOnClickHandler mClickHandler) {
        super(context, cursor);
        mContext = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(this, view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder viewHolder, Cursor cursor) {
        String imagePath = cursor.getString(cursor.getColumnIndex(FavMovieColumns.POSTER_PATH));
        ImageView target = viewHolder.getmMovieImageView();
        ImagesDownloader.getInstance().fetchImageFromMovieDb(mContext, imagePath, target);
    }


    @Override
    public Movie provideMovie(int adapterPosition) {
        getCursor().moveToPosition(adapterPosition);
        int id = getCursor().getInt(getCursor().getColumnIndex(FavMovieColumns._ID));
        String title = getCursor().getString(getCursor().getColumnIndex(FavMovieColumns.TITLE));
        String posterPath = getCursor().getString(getCursor().getColumnIndex(FavMovieColumns.POSTER_PATH));
        String overView = getCursor().getString(getCursor().getColumnIndex(FavMovieColumns.OVERVIEW));
        int releaseDate = getCursor().getInt(getCursor().getColumnIndex(FavMovieColumns.RELEASE_DATE));
        String dateInMillis = getCursor().getString(getCursor().getColumnIndex(FavMovieColumns.VOTE_AVERAGE));
        return new Movie(id, title, posterPath, overView, new Date(releaseDate), Double.valueOf(dateInMillis));
    }
}
