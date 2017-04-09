package pl.digitaldream.android.moviebrowser.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import pl.digitaldream.android.moviebrowser.R;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class ImagesDownloader {
    private static final String TAG = ImagesDownloader.class.getSimpleName();

    private static final String MOVIE_THUMBNAIL_URL = "http://image.tmdb.org/t/p/w185";

    private static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi";
    private static final String YOUTUBE_THUMBNAIL_MEDIUM_QUALITY_PATH = "mqdefault.jpg";


    private static ImagesDownloader instance;

    private ImagesDownloader(){

    }

    public static ImagesDownloader getInstance(){
        if(instance == null){
            instance = new ImagesDownloader();
        }
        return instance;
    }

    public void fetchImageFromMovieDb(Context context, String imageRelativePath, ImageView target) {
        Uri moviePosterUri  = Uri.parse(MOVIE_THUMBNAIL_URL+imageRelativePath);
        Log.d(TAG, "Downloading movie poster "+moviePosterUri);
        Picasso.with(context)
                .load(moviePosterUri)
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(target);
    }

    public void fetchImageFromYoutube(Context context, String ytVideoId, ImageView target) {
        Uri ytThumbnailUrl = Uri.parse(YOUTUBE_THUMBNAIL_URL).buildUpon()
                .appendPath(ytVideoId)
                .appendPath(YOUTUBE_THUMBNAIL_MEDIUM_QUALITY_PATH)
                .build();
        Log.d(TAG, "Downloading video thumbnail "+ytThumbnailUrl);
        Picasso.with(context)
                .load(ytThumbnailUrl)
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(target);
    }
}
