package pl.digitaldream.android.moviebrowser.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by sam_chordas on 8/11/15.
 */
public interface FavMovieColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    String POSTER_PATH = "posterPath";

    @DataType(DataType.Type.TEXT)
    String OVERVIEW = "overview";

    @DataType(DataType.Type.INTEGER)
    String RELEASE_DATE = "releaseDate";

    @DataType(DataType.Type.TEXT)
    String VOTE_AVERAGE = "voteAverage";


}

