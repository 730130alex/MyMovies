package com.demo.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.mymovies.adapters.MovieAdapter;
import com.demo.mymovies.data.MainViewModel;
import com.demo.mymovies.data.Movie;
import com.demo.mymovies.utils.JSONUtils;
import com.demo.mymovies.utils.NetworksUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private RecyclerView recyclerViewPoster;
    private MovieAdapter adapter;
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    private ProgressBar progressBar;

    private  static String lang;


//    private Switch switchSort;

    private MainViewModel viewModel;

    private static final  int LOADER_ID=133;
    private LoaderManager loaderManager;

    private  static int page=1;
    private static  int methodOfSort;
    private static boolean isLoading=false;
    private  SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.itemFavorite:
                Intent intentToFavorite=new Intent(this, FavoriteActivity.class);
                startActivity(intentToFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int GetColumtCount(){
        DisplayMetrics displayMetrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels/ displayMetrics.density);
        return width/185>2  ? width/185 : 2;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaderManager = LoaderManager.getInstance(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        progressBar = findViewById(R.id.progressBarLoading);
//для удаления фаворитов
//        viewModel.deliteAllFavoriteMovies();

        lang = Locale.getDefault().getLanguage();

       preferences = PreferenceManager.getDefaultSharedPreferences(this);


        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopularity = findViewById(R.id.textViewPopularity);
//        switchSort=findViewById(R.id.switchSort);
        adapter = new MovieAdapter();
        recyclerViewPoster = findViewById(R.id.recyclerViewPoster);
        recyclerViewPoster.setLayoutManager(new GridLayoutManager(this, GetColumtCount()));
        recyclerViewPoster.setAdapter(adapter);

//      switchSort.setChecked(true);
//       switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                page=1;

        if (preferences.getInt("methodOfSort", NetworksUtils.TOP_RATED) == NetworksUtils.TOP_RATED) {
            setMethodOfSorrt(true);
    }else{
            setMethodOfSorrt(false);
        }
//        setMethodOfSorrt(isChecked);
//
//            }
//        });
//        switchSort.setChecked(false);
        
        adapter.setOnPostrerClickListener(new MovieAdapter.OnPostrerClickListener() {
            @Override
            public void onPosterClick(int position) {

                Movie movie=adapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", movie.getId());
               startActivity(intent);

            }
        });
        
        adapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {

                if(!isLoading) {
                    downloadDate(methodOfSort, page);


//                    Toast.makeText(MainActivity.this, "Конец списка", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LiveData<List<Movie>> moviesFromLiveDate = viewModel.getMovies();
            moviesFromLiveDate.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {

               adapter.setMovies(movies);

            }
        });







    }



private void setMethodOfSorrt(boolean isTopRated){
    page=1;
     if(isTopRated){
        textViewTopRated.setTextColor(getResources().getColor(R.color.purple_200));
        textViewPopularity.setTextColor(getResources().getColor(R.color.white));
        methodOfSort=NetworksUtils.TOP_RATED;

    }else {
        methodOfSort=NetworksUtils.POPULARITY;
        textViewTopRated.setTextColor(getResources().getColor(R.color.white));
        textViewPopularity.setTextColor(getResources().getColor(R.color.purple_200));

    }
    preferences.edit().putInt("methodOfSort", methodOfSort).apply();

    downloadDate(methodOfSort, page);
}


    public void onClickSetPopularity(View view) {

        setMethodOfSorrt(false);
//        switchSort.setChecked(false);




    }

    public void onClickSetTopRated(View view) {

        setMethodOfSorrt(true);
//        switchSort.setChecked(true);
    }

    private void downloadDate(int methodOfSort, int page){

//        JSONObject json=NetworksUtils.getJSONfromNetwoork(methodOfSort, page);
//        ArrayList<Movie> movies = JSONUtils.getMoviesFromJASON(json);
//
//        if(movies!=null && !movies.isEmpty()){
//            viewModel.deliteAllMovies();
//            for (Movie movie: movies){
//                viewModel.insertMovie(movie);
//            }
//
//        }

        URL url=NetworksUtils.buildURL(methodOfSort, page, lang);
        Bundle bundle=new Bundle();
        bundle.putString("url", url.toString() );
        loaderManager.restartLoader(LOADER_ID, bundle, this);


    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworksUtils.JSONLoader jsonLoader=new NetworksUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworksUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {

                progressBar.setVisibility(View.VISIBLE);
                isLoading=true;
            }
            });

        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject json) {

        ArrayList<Movie> movies = JSONUtils.getMoviesFromJASON(json);

        if(movies!=null && !movies.isEmpty()){
            if(page==1) {
                viewModel.deliteAllMovies();
                adapter.clear();
            }
            for (Movie movie: movies){
                viewModel.insertMovie(movie);
//                Log.i("check", movie.getOriginalTitle() );
            }

            adapter.addMovies(movies);
            page++;
            isLoading=false;

            progressBar.setVisibility(View.INVISIBLE);

        }

        loaderManager.destroyLoader(LOADER_ID);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}