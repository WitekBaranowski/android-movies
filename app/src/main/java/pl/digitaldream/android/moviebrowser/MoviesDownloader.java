package pl.digitaldream.android.moviebrowser;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class MoviesDownloader {

    private static final String TAG = MoviesDownloader.class.getSimpleName();

    private static final String API_KEY = "#####";

    private static final String API_URL = "https://api.themoviedb.org/3/movie";

    private static final String MOVIE_THUMBNAIL_URL = "http://image.tmdb.org/t/p/w185";

    private final OkHttpClient client;
    private final Gson gson;

    private static MoviesDownloader instance;

    private MoviesDownloader(){
        client = new OkHttpClient();
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static MoviesDownloader getInstance(){
        if(instance == null){
            instance = new MoviesDownloader();
        }
        return instance;
    }

    public List<Movie> fetchMovies(MovieOrder order){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addPathSegment(order.getPathName());
        urlBuilder.addQueryParameter("api_key", API_KEY);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                Log.e(TAG, "Error response: "+response);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch movies", e);
            return Collections.emptyList();
        }

        MovieDbResponse movies = gson.fromJson(response.body().charStream(), MovieDbResponse.class);
        return movies.getResults();
    }

    public void fetchImageFromMovieDb(Context context, String imageRelativePath, ImageView target) {
        Uri moviePosterUri  = Uri.parse(MOVIE_THUMBNAIL_URL+imageRelativePath);
        Log.d(TAG, "Downloading "+moviePosterUri);
        Picasso.with(context)
                .load(moviePosterUri)
                .into(target);
    }
}
