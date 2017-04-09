package pl.digitaldream.android.moviebrowser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by wbaranowski on 26.03.2017.
 */

public class Movie implements Parcelable {

    public static final String MOVIE_DETAILS_DATA = "android.moviebrowser.intent.MovieDetails";

    private int id;
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    private String overview;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("vote_average")
    private Double voteAverage;

    public Movie(int id, String title, String posterPath, String overview, Date releaseDate, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        id = in.readInt();
        title = in.readString();
        voteAverage = in.readDouble();
        long tmpDate = in.readLong();
        releaseDate = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeLong(releaseDate != null ? releaseDate.getTime() : -1);
    }
}
