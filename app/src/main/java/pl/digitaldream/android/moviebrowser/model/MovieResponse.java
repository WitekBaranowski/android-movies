package pl.digitaldream.android.moviebrowser.model;

import java.util.List;

import pl.digitaldream.android.moviebrowser.model.Movie;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class MovieResponse {
    private int page;
    private List<Movie> results;
    private Integer totalPages;
    private Integer totalResults;

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

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
