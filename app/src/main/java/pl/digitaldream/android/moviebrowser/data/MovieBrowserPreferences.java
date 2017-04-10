package pl.digitaldream.android.moviebrowser.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pl.digitaldream.android.moviebrowser.MovieOrder;


public final class MovieBrowserPreferences {


    public static final String PREF_ORDER = "order";

    public static final MovieOrder DEFAULT_MOVIE_ORDER = MovieOrder.POPULAR;

    public static void setPrefOrder(Context context, MovieOrder order) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_ORDER, order.name());
        editor.apply();
    }

    public static MovieOrder getPrefOrder(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return MovieOrder.valueOf(sp.getString(PREF_ORDER, DEFAULT_MOVIE_ORDER.name()));
    }
}