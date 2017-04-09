package pl.digitaldream.android.moviebrowser.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by sam_chordas on 8/11/15.
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MoviesDatabase.class)
public final class MoviesProvider {

    public static final String AUTHORITY =
            "pl.digitaldream.android.moviebrowser.data.MoviesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String FAV_MOVIES = "fav_movies";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = MoviesDatabase.FAV_MOVIES)
    public static class FavMovies{

        @ContentUri(
                path = Path.FAV_MOVIES,
                type = "vnd.android.cursor.dir/fav_movies",
                defaultSort = FavMovieColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FAV_MOVIES);

        @InexactContentUri(
                name = "_ID",
                path = Path.FAV_MOVIES + "/#",
                type = "vnd.android.cursor.item/planet",
                whereColumn = FavMovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.FAV_MOVIES, String.valueOf(id));
        }
    }


}
