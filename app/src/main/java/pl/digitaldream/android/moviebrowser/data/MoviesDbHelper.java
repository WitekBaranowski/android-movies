package pl.digitaldream.android.moviebrowser.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.digitaldream.android.moviebrowser.data.MoviesContract.FavMoviesEntry;


public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + FavMoviesEntry.TABLE_NAME + " (" +
                        FavMoviesEntry._ID               + " INTEGER PRIMARY KEY, " +
                        FavMoviesEntry.COLUMN_TITLE       + " STRING NOT NULL, "     +
                        FavMoviesEntry.COLUMN_OVERVIEW + " STRING, "                  +
                        FavMoviesEntry.COLUMN_POSTER_PATH   + " STRING, "       +
                        FavMoviesEntry.COLUMN_RELEASE_DATE   + " INTEGER, "      +
                        FavMoviesEntry.COLUMN_VOTE_AVERAGE   + " STRING  );";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}