package com.demo.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.R;
import com.demo.mymovies.data.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.reviewViewHolder> {


    ArrayList<Review> reviews;

    @NonNull
    @Override
    public reviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new reviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewViewHolder holder, int position) {
        Review review=reviews.get(position);
        holder.textViewContent.setText(review.getContent());
        holder.textViewAuthor.setText(review.getAuthor());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class reviewViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthor;
        private  TextView textViewContent;

        public reviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor=itemView.findViewById(R.id.textViewAuthor);
            textViewContent=itemView.findViewById(R.id.textViewContent);

        }
    };

}
