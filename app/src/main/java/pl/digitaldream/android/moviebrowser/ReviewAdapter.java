package pl.digitaldream.android.moviebrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.digitaldream.android.moviebrowser.model.Review;

/**
 * Created by wbaranowski on 07.04.2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> reviews;

    public ReviewAdapter() {
        this.reviews = new ArrayList<>();
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        String author = reviews.get(position).getAuthor();
        String content = reviews.get(position).getContent();
        reviewAdapterViewHolder.mAuthor.setText(author);
        reviewAdapterViewHolder.mContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    void appendReviewsData(List<Review> reviewsData) {
        this.reviews = reviewsData;
        notifyDataSetChanged();
    }


    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView mContent;
        final TextView mAuthor;

        ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
        }
    }
}