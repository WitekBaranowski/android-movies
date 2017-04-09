package pl.digitaldream.android.moviebrowser.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class MovieDbClient {
    private static final String TAG = MovieDbClient.class.getSimpleName();

    private static final String API_URL = "https://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
