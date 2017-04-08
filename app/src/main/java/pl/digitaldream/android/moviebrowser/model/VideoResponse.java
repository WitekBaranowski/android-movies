package pl.digitaldream.android.moviebrowser.model;

import java.util.List;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class VideoResponse {
    private int page;
    private List<Video> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
