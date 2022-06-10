package com.demo.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorite_movies" )
public class FavoriteMovie extends Movie {
    public FavoriteMovie(int unicId, int id, int voteCount, String title, String originalTitle, String overview, String posterPath, String BigPosterPath, String backdropPath, double voteAverage, String releaseDate) {
        super(unicId, id, voteCount, title, originalTitle, overview, posterPath, BigPosterPath, backdropPath, voteAverage, releaseDate);
    }

    @Ignore
    public  FavoriteMovie (Movie movie){
        super(movie.getUnicId(), movie.getId(), movie.getVoteCount(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverview(), movie.getPosterPath(), movie.getBigPosterPath(), movie.getBackdropPath(), movie.getVoteAverage()  , movie.getReleaseDate());


    }
}
