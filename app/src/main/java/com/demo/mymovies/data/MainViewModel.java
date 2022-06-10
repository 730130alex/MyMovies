package com.demo.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {
    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database=MovieDatabase.getInstance(getApplication());
        movies=database.movieDao().getAllMovies();
        favoriteMovies=database.movieDao().getAllFavoritMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<FavoriteMovie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Movie getMovieById(int id){
        try {
            return new GetMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;


    }

    private  static  class  GetMovieTask extends AsyncTask<Integer, Void, Movie>{

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers!=null&&integers.length!=0){
                return database.movieDao().getMovieById(integers[0]);

            }
            return  null;
        }
    }
    public void deliteAllMovies() {

            new DeliteAllMovieTask().execute();
    }

    private  static  class  DeliteAllMovieTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... integers) {
            database.movieDao().DeliteAllMovies();
            return null;
        }
    }


    public  void insertMovie(Movie movie){

        new InsertMovieTask().execute(movie);
    }

    private  static  class  InsertMovieTask extends AsyncTask<Movie, Void, Void>{

        @Override
        protected Void doInBackground(Movie... integers) {
            if(integers!=null && integers.length!=0){
                database.movieDao().insertMovie(integers[0]);
            }
            return null;
        }
    }

    public  void insertFavoriteMovie(FavoriteMovie favoriteMovie){


        new InsertFavoriteMovieTask().execute(favoriteMovie);


    }

    private  static  class  InsertFavoriteMovieTask extends AsyncTask<FavoriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            if (favoriteMovies != null && favoriteMovies.length != 0) {
                database.movieDao().insertFavoriteMovie(favoriteMovies[0]);

            }
                return null;

        }
    }

    public  void deliteFavoriteMovie(FavoriteMovie movie){
            new DeliteFavoriteMovieTask().execute(movie);
    }

    private  static  class  DeliteFavoriteMovieTask extends AsyncTask<FavoriteMovie, Void, Void>{

        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if(movies!=null&&movies.length!=0){
                database.movieDao().deleteFavoriteMovie(movies[0]);

            }
            return  null;
        }
    }

    public  FavoriteMovie getFavoriteMovie(int id){
        try {
            return new getFavoriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }


    private  static  class getFavoriteMovieByIdTask extends AsyncTask<Integer, Void, FavoriteMovie>{

        @Override
        protected FavoriteMovie doInBackground(Integer... movies) {
            if(movies!=null&&movies.length!=0){
                return database.movieDao().getFavoriteMovieById(movies[0]);


            }
            return  null;
        }
    }

    public void deliteAllFavoriteMovies() {

        new DeliteAllFavoriteMovieTask().execute();
    }

    private  static  class  DeliteAllFavoriteMovieTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... integers) {
            database.movieDao().DeliteAllFavoriteMovies();
            return null;
        }
    }


}
