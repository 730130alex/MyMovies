package com.demo.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id==:movieId")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM movies")
    void DeliteAllMovies();

    @Insert()
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Insert()
    void insertFavoriteMovie(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM favorite_movies WHERE id==:movieId")
    FavoriteMovie getFavoriteMovieById(int movieId);

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<FavoriteMovie>> getAllFavoritMovies();

    @Query("DELETE FROM favorite_movies")
    void DeliteAllFavoriteMovies();


}
