package pl.digitaldream.android.moviebrowser.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {

    public static final String CONTENT_AUTHORITY =
            "pl.digitaldream.android.moviebrowser.data.MoviesProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "fav_movies";

    public static final class FavMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "FAV_MOVIE";

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER_PATH = "posterPath";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "releaseDate";

        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

        public static Uri buildMoviesUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
        }

    }
}