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
import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.model.MovieResponse;
import pl.digitaldream.android.moviebrowser.model.ReviewResponse;
import pl.digitaldream.android.moviebrowser.model.Video;
import pl.digitaldream.android.moviebrowser.model.VideoResponse;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class MoviesDownloader {
    private static final String TAG = MoviesDownloader.class.getSimpleName();

    private static final String API_KEY = "";
    private static final String API_URL = "https://api.themoviedb.org/3/movie";
    private static final String TRAILER_PATH = "videos";
    private static final String REVIEW_PATH = "reviews";

    private static final String MOVIE_THUMBNAIL_URL = "http://image.tmdb.org/t/p/w185";

    private static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi";
    private static final String YOUTUBE_THUMBNAIL_MEDIUM_QUALITY_PATH = "mqdefault.jpg";

    private static final String API_KEY_PARAM_NAME = "api_key";
    private static final String PAGE_PARAM_NAME = "page";

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

    public MovieResponse fetchMovies(MovieOrder order, String page){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addPathSegment(order.getPathName());
        urlBuilder.addQueryParameter(API_KEY_PARAM_NAME, API_KEY);
        urlBuilder.addQueryParameter(PAGE_PARAM_NAME, page);
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
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch movies", e);
            return null;
        }

        return gson.fromJson(response.body().charStream(), MovieResponse.class);
    }
    public List<Video> fetchVideos(int movieId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addPathSegment(String.valueOf(movieId));
        urlBuilder.addPathSegment(TRAILER_PATH);
        urlBuilder.addQueryParameter(API_KEY_PARAM_NAME, API_KEY);
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
            Log.e(TAG, "Failed to fetch videos", e);
            return Collections.emptyList();
        }
        VideoResponse movies = gson.fromJson(response.body().charStream(), VideoResponse.class);
        return movies.getResults();

    }
    public ReviewResponse fetchReviews(int movieId, int page){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addPathSegment(String.valueOf(movieId));
        urlBuilder.addPathSegment(REVIEW_PATH);
        urlBuilder.addQueryParameter(API_KEY_PARAM_NAME, API_KEY);
        urlBuilder.addQueryParameter(PAGE_PARAM_NAME, String.valueOf(page));
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
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch movies", e);
            return null;
        }
        return gson.fromJson(response.body().charStream(), ReviewResponse.class);
    }

    public void fetchImageFromMovieDb(Context context, String imageRelativePath, ImageView target) {
        Uri moviePosterUri  = Uri.parse(MOVIE_THUMBNAIL_URL+imageRelativePath);
        Log.d(TAG, "Downloading movie poster "+moviePosterUri);
        Picasso.with(context)
                .load(moviePosterUri)
                .into(target);
    }
    public void fetchImageFromYoutube(Context context, String ytVideoId, ImageView target) {
        Uri ytThumbnailUrl = Uri.parse(YOUTUBE_THUMBNAIL_URL).buildUpon()
                .appendPath(ytVideoId)
                .appendPath(YOUTUBE_THUMBNAIL_MEDIUM_QUALITY_PATH)
                .build();
        Log.d(TAG, "Downloading video thumbnail "+ytThumbnailUrl);
        Picasso.with(context)
                .load(ytThumbnailUrl)
                .into(target);
    }
}
