package com.demo.mymovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, FavoriteMovie.class}, version = 9, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase{

    private static final String DB_NAME="movies.db";
    private static MovieDatabase database;
    private static  final Object LOK = new Object();

    public static MovieDatabase getInstance(Context context) {
        synchronized (LOK) {
            if (database == null) {
                database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
        }
        return database;

    }

public abstract  MovieDao movieDao();


}
