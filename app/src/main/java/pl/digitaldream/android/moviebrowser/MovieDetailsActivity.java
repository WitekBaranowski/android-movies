package pl.digitaldream.android.moviebrowser;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.model.ReviewResponse;
import pl.digitaldream.android.moviebrowser.model.Video;
import pl.digitaldream.android.moviebrowser.tasks.Callback;
import pl.digitaldream.android.moviebrowser.tasks.FetchReviewsTask;
import pl.digitaldream.android.moviebrowser.tasks.FetchVideosTask;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private CarouselView mVideos;

    private List<Video> ytVideos;

    private ReviewAdapter reviewAdapter;

    private EndlessRecyclerViewScrollListener reviewsScrollListener;

    private int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mPoster = (ImageView) findViewById(R.id.iv_movie_thumbnail) ;
        mTitle = (TextView) findViewById(R.id.tv_title);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mUserRating = (TextView) findViewById(R.id.tv_user_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Movie.MOVIE_DETAILS_DATA)) {
                bindData(intentThatStartedThisActivity);
            }
        }
        setupVideoCarousel();

        RecyclerView reviewsRecycleView = (RecyclerView) findViewById(R.id.recyclerview_revievs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewsRecycleView.setLayoutManager(linearLayoutManager);
        reviewsScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchNextReviewPage(page);
            }
        };
        reviewsRecycleView.addOnScrollListener(reviewsScrollListener);
        reviewAdapter = new ReviewAdapter();
        reviewsRecycleView.setAdapter(reviewAdapter);
    }
    public void fetchFirstReviewPage(final RecyclerView reviewsRecycleView) {
        new FetchReviewsTask(this).execute(movieId, 1);
    }
    public void fetchNextReviewPage(int offset) {
        new FetchReviewsTask(this).execute(movieId, offset);
    }
    public void loadNewReviews(ReviewResponse response) {
        Log.i(TAG, "Fetched "+response.getResults().size() + " results");
        Log.i(TAG, "From page: "+response.getPage() );
        Log.i(TAG, "Total results: "+response.getTotalResults());
        int curSize = reviewAdapter.getItemCount();
        reviewAdapter.appendReviewsData(response.getResults());
        reviewAdapter.notifyItemRangeInserted(curSize, reviewAdapter.getItemCount()- 1);
    }

    private void setupVideoCarousel() {
        new FetchVideosTask(this).execute(movieId);
        mVideos = (CarouselView) findViewById(R.id.cv_videos);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                MoviesDownloader.getInstance().fetchImageFromYoutube(MovieDetailsActivity.this,
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

    private void bindData(Intent movieDataIntent) {
        Movie movie = movieDataIntent.getParcelableExtra(Movie.MOVIE_DETAILS_DATA);
        mTitle.setText(movie.getTitle());
        mOverview.setText(movie.getOverview());
        String userRating = getResources().getString(R.string.format_rating, movie.getVoteAverage());
        mUserRating.setText(userRating);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        mReleaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        MoviesDownloader.getInstance()
                .fetchImageFromMovieDb(this, movie.getPosterPath(), mPoster);
        movieId = movie.getId();
    }

    public void initVideos(List<Video> videos){
        ytVideos = new ArrayList<>(videos.size());
        for (Video video : videos) {
            if(getString(R.string.youtube_video_site).equals(video.getSite())){
                ytVideos.add(video);
            }
        }
        mVideos.setPageCount(ytVideos.size());

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

}
