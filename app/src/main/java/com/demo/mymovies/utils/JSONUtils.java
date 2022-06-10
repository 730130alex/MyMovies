package com.demo.mymovies.utils;

import android.provider.MediaStore;

import com.demo.mymovies.data.Movie;
import com.demo.mymovies.data.Review;
import com.demo.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static  final String KEY_RESULTS="results";
//для отзівов
    private static  final String KEY_AUTHOR="author";
    private static  final String KEY_CONTENT="content";

    //для видео

    private static  final String KEY_OF_VIDEO="key";
    private static  final String KEY_OF_NAME="name";
    private static  final String BASE_YUTUBE_URL="https://www.youtube.com/watch?v=";

//вся информация о фитльме
    private static  final String KEY_ID="id";
    private static  final String KEY_VOTE_COUNT="vote_count";
    private static  final String KEY_TITLE="title";
    private static  final String KEY_ORIGINAL_TITLE="original_title";
    private static  final String KEY_OVERVIEW="overview";
    private static  final String KEY_POSTER_PATH="poster_path";
    private static  final String KEY_BACKDROP_PATH="backdrop_path";
    private static  final String KEY_VOTE_AVERAGE="vote_average";
    private static  final String KEY_RELEASE_DATE="release_date";


    public static  final String BASE_POSTER_URL ="https://image.tmdb.org/t/p/";
    public  static final String SMALL_POSTER_SIZE ="w185";
    public  static final String BIG_POSTER_SIZE ="w780";

    public  static ArrayList<Review> getReviewsFromJason(JSONObject jasonObject){
        ArrayList<Review> resuilt= new ArrayList<>();
        if(jasonObject==null){
            return resuilt;
        }
        try {
            JSONArray jsonArray = jasonObject.getJSONArray(KEY_RESULTS);
            for(int i=0; i<jsonArray.length();i++){
                JSONObject objectReview=jsonArray.getJSONObject(i);
                String author=objectReview.getString(KEY_AUTHOR);
                String content=objectReview.getString(KEY_CONTENT);

                resuilt.add(new Review(author,content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  resuilt;

    }

    public  static ArrayList<Trailer> getVideosFromJason(JSONObject jasonObject){
        ArrayList<Trailer> resuilt= new ArrayList<>();
        if(jasonObject==null){
            return resuilt;
        }
        try {
            JSONArray jsonArray = jasonObject.getJSONArray(KEY_RESULTS);
            for(int i=0; i<jsonArray.length();i++){
                JSONObject objectTrailer=jsonArray.getJSONObject(i);
                String key=objectTrailer.getString(KEY_OF_VIDEO);
//                String key=BASE_YUTUBE_URL+objectTrailer.getString(KEY_OF_VIDEO);

                String name=objectTrailer.getString(KEY_OF_NAME);

                resuilt.add(new Trailer(key, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  resuilt;

    }



    public  static ArrayList<Movie> getMoviesFromJASON (JSONObject jasonObject){
        ArrayList<Movie> resuilt= new ArrayList<>();
        if(jasonObject==null){
            return resuilt;
        }
        try {
            JSONArray jsonArray = jasonObject.getJSONArray(KEY_RESULTS);
            for(int i=0; i<jsonArray.length();i++){
                JSONObject objectMovie=jsonArray.getJSONObject(i);
                int id=objectMovie.getInt(KEY_ID);
                int voteCount=objectMovie.getInt(KEY_VOTE_COUNT);
                String title=objectMovie.getString(KEY_TITLE);
                String originalTitle=objectMovie.getString(KEY_ORIGINAL_TITLE);
                String overview=objectMovie.getString(KEY_OVERVIEW);
                String posterPath= BASE_POSTER_URL +SMALL_POSTER_SIZE+objectMovie.getString(KEY_POSTER_PATH);
                String BigPosterPath=BASE_POSTER_URL+BIG_POSTER_SIZE+objectMovie.getString(KEY_POSTER_PATH);
                String backdropPath=objectMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage=objectMovie.optDouble(KEY_VOTE_AVERAGE);
                String releaseDate=objectMovie.getString(KEY_RELEASE_DATE);
                resuilt.add(new Movie(id,voteCount,title,originalTitle,overview,posterPath,BigPosterPath, backdropPath,voteAverage,releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  resuilt;

    }

}
