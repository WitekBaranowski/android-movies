package pl.digitaldream.android.moviebrowser;

import java.util.List;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class MovieDbResponse {
    private int page;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
