package pl.digitaldream.android.moviebrowser.tasks;

import android.os.AsyncTask;

import java.util.Collections;
import java.util.List;

import pl.digitaldream.android.moviebrowser.MovieDetailsActivity;
import pl.digitaldream.android.moviebrowser.MoviesDownloader;
import pl.digitaldream.android.moviebrowser.model.Video;

/**
 * Created by wbaranowski on 07.04.2017.
 */
public class FetchVideosTask extends AsyncTask<Integer, Void, List<Video>> {

    private MovieDetailsActivity movieDetailsActivity;

    public FetchVideosTask(MovieDetailsActivity movieDetailsActivity) {
        this.movieDetailsActivity = movieDetailsActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Video> doInBackground(Integer... params) {
        if (params.length == 0) {
            return Collections.emptyList();
        }
        return MoviesDownloader.getInstance().fetchVideos(params[0]);
    }

    @Override
    protected void onPostExecute(List<Video> videos) {
        super.onPostExecute(videos);
        movieDetailsActivity.initVideos(videos);
    }


}
