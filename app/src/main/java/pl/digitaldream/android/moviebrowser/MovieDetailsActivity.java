package pl.digitaldream.android.moviebrowser;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import pl.digitaldream.android.moviebrowser.data.FavMovieColumns;
import pl.digitaldream.android.moviebrowser.data.MoviesProvider;
import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.model.ReviewResponse;
import pl.digitaldream.android.moviebrowser.model.Video;
import pl.digitaldream.android.moviebrowser.model.VideoResponse;
import pl.digitaldream.android.moviebrowser.network.ImagesDownloader;
import pl.digitaldream.android.moviebrowser.network.MovieDbAPI;
import pl.digitaldream.android.moviebrowser.network.MovieDbClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final int ID_FAV_MOVIE_LOADER = 70;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private ToggleButton mFavorite;
    private CarouselView mVideos;
    private List<Video> ytVideos;
    private ReviewAdapter reviewAdapter;
    private RecyclerView reviewsRecycleView;
    private EndlessRecyclerViewScrollListener reviewsScrollListener;

    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mPoster = (ImageView) findViewById(R.id.iv_movie_thumbnail) ;
        mTitle = (TextView) findViewById(R.id.tv_title);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mUserRating = (TextView) findViewById(R.id.tv_user_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        mFavorite = (ToggleButton) findViewById(R.id.tb_favorite);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Movie.MOVIE_DETAILS_DATA)) {
                bindData(intentThatStartedThisActivity);
            }
        }
        createVideoElement();

        reviewsRecycleView = (RecyclerView) findViewById(R.id.recyclerview_revievs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewsRecycleView.setLayoutManager(linearLayoutManager);
        reviewsScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadReviews(page-1);
            }
        };
        reviewAdapter = new ReviewAdapter();
        reviewsRecycleView.setAdapter(reviewAdapter);
        reviewsRecycleView.addOnScrollListener(reviewsScrollListener);
        loadReviews(1);
        getSupportLoaderManager().initLoader(ID_FAV_MOVIE_LOADER, null, this);
    }

    private void bindData(Intent movieDataIntent) {
        movie = movieDataIntent.getParcelableExtra(Movie.MOVIE_DETAILS_DATA);
        mTitle.setText(movie.getTitle());
        mOverview.setText(movie.getOverview());
        String userRating = getResources().getString(R.string.format_rating, movie.getVoteAverage());
        mUserRating.setText(userRating);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        mReleaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        ImagesDownloader.getInstance().fetchImageFromMovieDb(this, movie.getPosterPath(), mPoster);
    }

    private void createVideoElement() {
        MovieDbAPI movieDbAPI = MovieDbClient.getClient().create(MovieDbAPI.class);
        Call<VideoResponse> call = movieDbAPI.loadVideos(movie.getId(), getString(R.string.movie_db_api_key));
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if(response.isSuccessful()) {
                    List<Video> videos = response.body().getResults();
                    setupVideoCarousel();
                    initVideos(videos);
                }else{
                    Log.e(TAG, "Error response: "+response);
                }
            }
            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }
    private void initVideos(List<Video> videos){
        ytVideos = new ArrayList<>(videos.size());
        for (Video video : videos) {
            if(getString(R.string.youtube_video_site).equals(video.getSite())){
                ytVideos.add(video);
            }
        }
        mVideos.setPageCount(ytVideos.size());
    }

    private void setupVideoCarousel() {
        mVideos = (CarouselView) findViewById(R.id.cv_videos);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                ImagesDownloader.getInstance().fetchImageFromYoutube(MovieDetailsActivity.this,
                        ytVideos.get(position).getKey(), imageView);
            }
        };
        mVideos.setImageListener(imageListener);
        mVideos.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                watchYoutubeVideo(ytVideos.get(position).getKey());
            }
        });
    }

    public void loadReviews(int page) {
        MovieDbAPI movieDbAPI = MovieDbClient.getClient().create(MovieDbAPI.class);
        Call<ReviewResponse> call = movieDbAPI.loadReviews(movie.getId(), page,
                getString(R.string.movie_db_api_key));
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if(response.isSuccessful()) {
                    ReviewResponse reviewsResponse = response.body();
                    int curSize = reviewAdapter.getItemCount();
                    reviewAdapter.appendReviewsData(reviewsResponse.getResults());
                    reviewAdapter.notifyItemRangeInserted(curSize, reviewAdapter.getItemCount()- 1);

                }else{
                    Log.e(TAG, "Error response: "+response);
                }
            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(ID_FAV_MOVIE_LOADER == id){
            return new CursorLoader(this, MoviesProvider.FavMovies.withId(movie.getId()),
                    null, null, null, null);
        }
        throw new RuntimeException("Loader Not Implemented: " + id);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() == 1){
            mFavorite.setChecked(true);
        }else{
            mFavorite.setChecked(false);
        }
        mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavMovieColumns._ID, movie.getId());
                    contentValues.put(FavMovieColumns.TITLE, movie.getTitle());
                    contentValues.put(FavMovieColumns.OVERVIEW, movie.getOverview());
                    contentValues.put(FavMovieColumns.POSTER_PATH, movie.getPosterPath());
                    contentValues.put(FavMovieColumns.RELEASE_DATE, movie.getReleaseDate().getTime());
                    contentValues.put(FavMovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
                    getContentResolver().insert(MoviesProvider.FavMovies.CONTENT_URI, contentValues);
                } else {
                    getContentResolver().delete(MoviesProvider.FavMovies.withId(movie.getId()),
                            null, null);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
