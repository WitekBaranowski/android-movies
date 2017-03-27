package pl.digitaldream.android.moviebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mUserRating;
    private TextView mReleaseDate;

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
    }
}
