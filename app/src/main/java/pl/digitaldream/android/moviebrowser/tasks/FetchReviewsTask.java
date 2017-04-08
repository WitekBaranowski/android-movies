package pl.digitaldream.android.moviebrowser.tasks;

import android.os.AsyncTask;
import android.os.Handler;

import pl.digitaldream.android.moviebrowser.MovieDetailsActivity;
import pl.digitaldream.android.moviebrowser.MoviesDownloader;
import pl.digitaldream.android.moviebrowser.model.ReviewResponse;

/**
 * Created by wbaranowski on 07.04.2017.
 */
public class FetchReviewsTask extends AsyncTask<Integer, Void, ReviewResponse> {

    private MovieDetailsActivity movieDetailsActivity;
    private Callback callback;

    public FetchReviewsTask(MovieDetailsActivity movieDetailsActivity, Callback callback) {
        this.movieDetailsActivity = movieDetailsActivity;
        this.callback = callback;
    }

    public FetchReviewsTask(MovieDetailsActivity movieDetailsActivity) {
        this.movieDetailsActivity = movieDetailsActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ReviewResponse doInBackground(Integer... params) {
        if (params.length != 2) {
            return null;
        }
        return MoviesDownloader.getInstance().fetchReviews(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(ReviewResponse response) {
        super.onPostExecute(response);
        movieDetailsActivity.loadNewReviews(response);
        if(callback != null){
            callback.call();
        }

    }


}
