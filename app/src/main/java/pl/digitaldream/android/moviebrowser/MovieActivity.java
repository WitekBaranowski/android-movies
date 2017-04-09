package pl.digitaldream.android.moviebrowser;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import pl.digitaldream.android.moviebrowser.data.MoviesProvider;
import pl.digitaldream.android.moviebrowser.model.Movie;
import pl.digitaldream.android.moviebrowser.model.MovieResponse;
import pl.digitaldream.android.moviebrowser.network.MovieDbAPI;
import pl.digitaldream.android.moviebrowser.network.MovieDbClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity implements MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MovieActivity.class.getSimpleName();
    private static final int ID_FAV_MOVIES_LOADER = 69;

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private MovieCursorAdapter mFavoriteMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private EndlessRecyclerViewScrollListener reviewsScrollListener;

    private MovieOrder movieOrder = MovieOrder.POPULAR;

    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        GridLayoutManager layoutManager;
        if (Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mFavoriteMovieAdapter = new MovieCursorAdapter(this, null, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        reviewsScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchMovies(movieOrder, page + 1);
            }
        };
        mRecyclerView.addOnScrollListener(reviewsScrollListener);

        fetchMovies(movieOrder, 1);
    }

    private void fetchMovies(MovieOrder order, int page) {
        showLoader();
        MovieDbAPI movieDbAPI = MovieDbClient.getClient().create(MovieDbAPI.class);
        Call<MovieResponse> call = movieDbAPI.loadMovies(order.getPathName(), page,
                getString(R.string.movie_db_api_key));
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                hideLoader();
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getResults();
                    mMovieAdapter.addMovies(movies);
                    showMovieDataView();
                } else {
                    Log.e(TAG, "Error response: " + response);
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                hideLoader();
                showErrorMessage();
            }
        });

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
            mRecyclerView.setAdapter(mMovieAdapter);
            mRecyclerView.addOnScrollListener(reviewsScrollListener);
            reviewsScrollListener.resetState();
            fetchMovies(MovieOrder.POPULAR, 1);
            return true;
        }

        if (id == R.id.action_order_top_rated) {
            mMovieAdapter.reset();
            mRecyclerView.setAdapter(mMovieAdapter);
            mRecyclerView.addOnScrollListener(reviewsScrollListener);
            reviewsScrollListener.resetState();
            fetchMovies(MovieOrder.TOP_RATED, 1);
            return true;
        }
        if (id == R.id.action_favorites) {
            mMovieAdapter.reset();
            mRecyclerView.setAdapter(mFavoriteMovieAdapter);
            mRecyclerView.removeOnScrollListener(reviewsScrollListener);
            reviewsScrollListener.resetState();
            getSupportLoaderManager().initLoader(ID_FAV_MOVIES_LOADER, null, this);
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


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_FAV_MOVIES_LOADER:
                showLoader();
                return new CursorLoader(this, MoviesProvider.FavMovies.CONTENT_URI,
                        null, null, null, null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        hideLoader();
        mFavoriteMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        hideLoader();
        mFavoriteMovieAdapter.swapCursor(null);
    }
}