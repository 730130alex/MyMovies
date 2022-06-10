package com.demo.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.R;
import com.demo.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieViewHolder>{

    private List<Movie> movies;
    private OnPostrerClickListener onPostrerClickListener;
    private OnReachEndListener onReachEndListener;



    public interface OnPostrerClickListener {
        void onPosterClick(int position);
    }

    public interface OnReachEndListener{
        void onReachEnd();
    }

    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnPostrerClickListener(OnPostrerClickListener onPostrerClickListener) {
        this.onPostrerClickListener = onPostrerClickListener;
    }

    public MovieAdapter() {
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(movies.size()>=20 && position>movies.size()-4 && onReachEndListener!=null){
            onReachEndListener.onReachEnd();
        }

        Movie movie=movies.get(position);
        Picasso.get().load(movie.getBigPosterPath()).into(holder.imageViewSmallPoster);


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewSmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster=itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onPostrerClickListener!=null){
                        onPostrerClickListener.onPosterClick(getAdapterPosition());

                    }

                }
            });

        }
    }

    public List<Movie> getMovies() {

        return movies;
    }

    public void addMovies( List<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();

    }

    public void setMovies( List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
