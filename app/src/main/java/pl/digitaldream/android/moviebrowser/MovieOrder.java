package pl.digitaldream.android.moviebrowser;

/**
 * Created by wbaranowski on 27.03.2017.
 */

public enum MovieOrder {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVORITE(null);

    private String pathName;

    MovieOrder(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }

}
