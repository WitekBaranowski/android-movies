package pl.digitaldream.android.moviebrowser.network;

import pl.digitaldream.android.moviebrowser.model.MovieResponse;
import pl.digitaldream.android.moviebrowser.model.ReviewResponse;
import pl.digitaldream.android.moviebrowser.model.VideoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbAPI {

        @GET("movie/{movieId}/videos")
        Call<VideoResponse> loadVideos(@Path("movieId") int movieId, @Query("api_key") String apiKey);

        @GET("movie/{movieId}/reviews")
        Call<ReviewResponse> loadReviews(@Path("movieId") int movieId,
                                         @Query("page") int page,
                                         @Query("api_key") String apiKey);

        @GET("movie/{sort}")
        Call<MovieResponse> loadMovies(@Path("sort") String order,
                                       @Query("page") int page,
                                       @Query("api_key") String apiKey);
}