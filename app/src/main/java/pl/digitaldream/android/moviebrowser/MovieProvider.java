package pl.digitaldream.android.moviebrowser;

import pl.digitaldream.android.moviebrowser.model.Movie;

/**
 * The interface that receives onClick messages.
 */
public interface MovieProvider {
    Movie provideMovie(int adapterPosition);
}
