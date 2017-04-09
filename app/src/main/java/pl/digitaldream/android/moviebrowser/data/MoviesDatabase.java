package pl.digitaldream.android.moviebrowser.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by sam_chordas on 8/11/15.
 */

@Database(version = MoviesDatabase.VERSION)
public final class MoviesDatabase {
    private MoviesDatabase(){}

    public static final int VERSION = 3;

        @Table(FavMovieColumns.class)
        public static final String FAV_MOVIES = "FAV_MOVIE";





}
