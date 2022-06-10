package com.demo.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mymovies.adapters.ReviewAdapter;
import com.demo.mymovies.adapters.TrailerAdapter;
import com.demo.mymovies.data.FavoriteMovie;
import com.demo.mymovies.data.MainViewModel;
import com.demo.mymovies.data.Movie;
import com.demo.mymovies.data.Review;
import com.demo.mymovies.data.Trailer;
import com.demo.mymovies.utils.JSONUtils;
import com.demo.mymovies.utils.NetworksUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import static com.demo.mymovies.R.string.delite_from_favorite;

public class DetailActivity extends AppCompatActivity {

    private ScrollView scrollViewInfo;

    private ImageView imageViewBigPoster;
    private TextView  textViewTitle;
    private TextView textViewOriganalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;

    private RecyclerView recyclerViewReview;
    private RecyclerView recyclerViewTrailer;

    private com.demo.mymovies.adapters.ReviewAdapter ReviewAdapter;
    private com.demo.mymovies.adapters.TrailerAdapter TrailerAdapter;

    private  static String lang;


    private ImageView imageViewAddToFavorite;

    private MainViewModel viewModel;

    private int id;

    private Movie movie;
    private  FavoriteMovie favoriteMovie;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewBigPoster=findViewById(R.id.imageViewbBigPoster);
        textViewTitle=findViewById(R.id.textViewTitle);
        textViewOriganalTitle=findViewById(R.id.textViewOriginalTitle);
        textViewRating=findViewById(R.id.textViewRating);
        textViewReleaseDate=findViewById(R.id.textViewReleaseDate);
        textViewOverview=findViewById(R.id.textViewOverview);
        imageViewAddToFavorite=findViewById(R.id.imageViewAddToFavorite);
        scrollViewInfo=findViewById(R.id.scroleViewInfo);
        lang= Locale.getDefault().getLanguage();



        Intent intent=getIntent();
        if(intent!=null && intent.hasExtra("id")){
            id=intent.getIntExtra("id",-1);
        }else {
            finish();
        }

        viewModel= androidx.lifecycle.ViewModelProviders.of(this).get(MainViewModel.class);
        if(viewModel.getMovieById(id)==null){
            movie = viewModel.getFavoriteMovie(id);
        }else {
            movie = viewModel.getMovieById(id);
        }
        Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.film).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriganalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(Double.toString(movie.getVoteCount()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());


//          viewModel.deliteAllFavoriteMovies();
       setFavorite();

        recyclerViewReview=findViewById(R.id.RecyclerViewReniews);
        recyclerViewReview.setLayoutManager(new GridLayoutManager(this, 1));
        ReviewAdapter=new ReviewAdapter();
        recyclerViewReview.setAdapter(ReviewAdapter);


        recyclerViewTrailer=findViewById(R.id.RecyclerViewTrailers);
        recyclerViewTrailer.setLayoutManager(new GridLayoutManager(this, 1));
        TrailerAdapter=new TrailerAdapter();
        recyclerViewTrailer.setAdapter(TrailerAdapter);
        TrailerAdapter.setOnTrailerClickListener(new com.demo.mymovies.adapters.TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void OnTrailerClick(String url) {
   //             Toast.makeText(DetailActivity.this, url, Toast.LENGTH_SHORT).show();
                
//                Intent intentToTrailer=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intentToTrailer);

                Intent intentToPlier = new Intent(DetailActivity.this, VideoPlierActivity.class);
                intentToPlier.putExtra("key", url);
                startActivity(intentToPlier);


            }
        });

        JSONObject jsonObjectReviews= NetworksUtils.getJSONforReviews(id, lang);
        ArrayList<Review> reviews=JSONUtils.getReviewsFromJason(jsonObjectReviews);
        ReviewAdapter.setReviews(reviews);

        JSONObject jsonObjectTrailers= NetworksUtils.getJSONforVideos(id, lang);
        ArrayList<Trailer> trailers=JSONUtils.getVideosFromJason(jsonObjectTrailers);
        TrailerAdapter.setTrailwers(trailers);
        scrollViewInfo.smoothScrollTo(0, 0);





    }

    public void onClickCangeFavorite(View view) {

        if (favoriteMovie == null) {

             viewModel.insertFavoriteMovie(new FavoriteMovie(movie));
        Toast.makeText(this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deliteFavoriteMovie(favoriteMovie);
            Toast.makeText(this, delite_from_favorite, Toast.LENGTH_SHORT).show();

        }
            setFavorite();
    }

    private void setFavorite (){
        favoriteMovie = viewModel.getFavoriteMovie(id);
            if(favoriteMovie==null){
                imageViewAddToFavorite.setImageResource(R.drawable.from_favorite);
            }else {
                imageViewAddToFavorite.setImageResource(R.drawable.to_favorite);
            }


    }





}