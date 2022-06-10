package com.demo.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class NetworksUtils {
    //https://api.themoviedb.org/3/discover/movie?api_key=42db5c3f902be0e1248a79f70b4cb86f&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate
    private  static  final String BASE_URL="https://api.themoviedb.org/3/discover/movie";

    private  static  final String BASE_URL_VIDEOS="https://api.themoviedb.org/3/movie/%s/videos";
    private  static  final String BASE_URL_REVIEWS="https://api.themoviedb.org/3/movie/%s/reviews";

    private  static  final String PARAMS_API_KEY="api_key";
    private  static  final String PARAMS_LEANGUAGE="language";
    private  static  final String PARAMS_SORT_BY="sort_by";
    private  static  final String PARAMS_PAGE="page";
    private  static  final String PARAMS_MIN_VOTE_COUNT="vote_count.gte";

    private static final String API_KEY="42db5c3f902be0e1248a79f70b4cb86f";
//    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY="popularity.desc";
    private static final String SORT_BY_TOP_RATED="vote_average.desc";
    private static final String MIN_VOTE_COUNT_VALUE="1000";

    public  static  final int POPULARITY=0;
    public  static  final int TOP_RATED=1;



//    public static  final String BASE_POSTER_URL ="https://image.tmdb.org/t/p/";
//    public  static final String SMALL_POSTER_SIZE ="w185";
//    public  static final String BIG_POSTER_SIZE ="w780";





    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{
        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;


        public interface OnStartLoadingListener{
            void onStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }
        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            if(onStartLoadingListener!=null ){
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {

            if(bundle==null){return null;}
            URL url = null;

            String urlAsString = bundle.getString("url");
            try {
               url=new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            JSONObject result=null;

            if(url==null){
                return result;
            }
            HttpsURLConnection connection=null;

            try {
                connection=(HttpsURLConnection) url.openConnection();
                InputStream inputStream=connection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream) ;
                BufferedReader reader=new BufferedReader(inputStreamReader);
                String line=reader.readLine();
                StringBuilder stringBuilder=new StringBuilder();
                while (line!=null){
                    stringBuilder.append(line);
                    line=reader.readLine();
                }
                result=new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }

            return    result;


        }
    }





    public static URL buildURLToVodeos(int id, String lang ){
        URL result=null;
        Uri uri=Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LEANGUAGE, lang)
                .build();
        try {
            result= new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  result;


    }

    public static JSONObject getJSONforVideos(int id, String lang){
        JSONObject result=null;
        URL url = buildURLToVodeos(id, lang);
        try {
            result= new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;



    }

    public static URL buildURLToReviews(int id, String lang){
        URL result=null;
        Uri uri=Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LEANGUAGE, lang)
                .build();
        try {
            result= new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  result;


    }

    public static JSONObject getJSONforReviews (int id, String lang){
        JSONObject result=null;
        URL url = buildURLToReviews(id, lang);
        try {
            result= new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;



    }




    public static URL buildURL(int sortBy, int page, String lang ){
        URL result=null;
        String methodOfSort;
        if(sortBy==POPULARITY){
            methodOfSort=SORT_BY_POPULARITY;
        }else {
            methodOfSort=SORT_BY_TOP_RATED;
        }
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LEANGUAGE, lang)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        try {
            result= new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  result;


    }
    public static JSONObject getJSONfromNetwoork (int sortBy, int page, String lang){
        JSONObject result=null;
        URL url = buildURL(sortBy, page, lang);
        try {
            result= new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;



    }

           private  static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(URL... urls) {

            JSONObject result=null;

            if(urls==null||urls.length==0){
                return result;
            }
            HttpsURLConnection connection=null;

            try {
                connection=(HttpsURLConnection) urls[0].openConnection();
                InputStream inputStream=connection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream) ;
                BufferedReader reader=new BufferedReader(inputStreamReader);
                String line=reader.readLine();
                StringBuilder stringBuilder=new StringBuilder();
                while (line!=null){
                    stringBuilder.append(line);
                    line=reader.readLine();
                }
                result=new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }

            return    result;


        }
    }




}
