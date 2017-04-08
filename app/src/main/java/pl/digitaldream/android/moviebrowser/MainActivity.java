package pl.digitaldream.android.moviebrowser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.model.MovieResponse;
import pl.digitaldream.android.moviebrowser.tasks.FetchMoviesTask;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private EndlessRecyclerViewScrollListener reviewsScrollListener;

    private MovieOrder movieOrder = MovieOrder.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        reviewsScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchMovies(movieOrder, page+1);
            }
        };
        mRecyclerView.addOnScrollListener(reviewsScrollListener);

       fetchMovies(movieOrder, 1);
    }

    private void fetchMovies(MovieOrder order, int page) {
        new FetchMoviesTask(this).execute(String.valueOf(order), String.valueOf(page));

    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetailsActivity.class;
        Intent movieDetailsIntent = new Intent(context, destinationClass);
        movieDetailsIntent.putExtra(Movie.MOVIE_DETAILS_DATA, movie);
        startActivity(movieDetailsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_order_popular) {
            mMovieAdapter.reset();
            reviewsScrollListener.resetState();
            fetchMovies(MovieOrder.POPULAR, 1);
            return true;
        }

        if (id == R.id.action_order_top_rated) {
            mMovieAdapter.reset();
            reviewsScrollListener.resetState();
            fetchMovies(MovieOrder.TOP_RATED, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showLoader() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void handleResponse(MovieResponse response) {
        mMovieAdapter.addMovies(response.getResults());
    }
}
