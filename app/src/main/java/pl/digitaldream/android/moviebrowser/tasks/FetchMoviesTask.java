package pl.digitaldream.android.moviebrowser.tasks;

import android.os.AsyncTask;

import pl.digitaldream.android.moviebrowser.MainActivity;
import pl.digitaldream.android.moviebrowser.MovieOrder;
import pl.digitaldream.android.moviebrowser.MoviesDownloader;
import pl.digitaldream.android.moviebrowser.model.MovieResponse;

/**
 * Created by wbaranowski on 07.04.2017.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, MovieResponse> {

    private MainActivity mainActivity;

    public FetchMoviesTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.showLoader();
    }

    @Override
    protected MovieResponse doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        return MoviesDownloader.getInstance().fetchMovies(MovieOrder.valueOf(params[0]), params[1]);
    }

    @Override
    protected void onPostExecute(MovieResponse response) {
        mainActivity.hideLoader();
        if (response != null) {
            mainActivity.showMovieDataView();
            mainActivity.handleResponse(response);
        } else {
            mainActivity.showErrorMessage();
        }
    }
}
