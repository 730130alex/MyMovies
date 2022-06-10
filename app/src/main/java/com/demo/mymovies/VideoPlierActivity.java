package com.demo.mymovies;

//import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;



public class VideoPlierActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener
    ,YouTubePlayer.OnFullscreenListener
        , YouTubePlayer.PlayerStateChangeListener

{
    private static final int RECOVERY_REQUEST = 1 ;
    private YouTubePlayerView youTubeView;
    public static final String YOUTUBE_API_KEY = "AIzaSyDU89wWZv4uEe1fGXf9YdnQUejTyUXJCCQ" ;
    public static String API_KEY;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_plier);


        Intent intent=getIntent();
      if(intent!=null && intent.hasExtra("key")) {
            API_KEY = intent.getStringExtra("key");
        }else {
        finish();
      }

        youTubeView = findViewById(R.id.youtube_view);
//        Log.i("MyCheck", API_KEY + "_check-04");
        youTubeView.initialize(YOUTUBE_API_KEY, this );




    }

    @Override
    public void onInitializationSuccess (YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {



            if (!wasRestored) {
//                Toast.makeText(VideoPlierActivity.this, "2 test", Toast.LENGTH_SHORT).show();
                youTubePlayer = player;
                player.setPlayerStateChangeListener(this);
                player.cueVideo(API_KEY);

   }else{
//                Toast.makeText(VideoPlierActivity.this, "3 test", Toast.LENGTH_SHORT).show();
//                player.setFullscreen(true);
                youTubePlayer = player;


            }

                if(!player.isPlaying()) {
                    youTubePlayer.play();
                }

            player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                 player.setFullscreen(b);

//                 Toast.makeText(VideoPlierActivity.this, "Fullscreen test", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onInitializationFailure (YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog( this , RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText( this , error, Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this );
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider () {
        return youTubeView;
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        youTubePlayer.setFullscreen(true);
    }

    @Override
    public void onVideoEnded() {
        finish();


    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onFullscreen(boolean b) {
        youTubePlayer.setFullscreen(b);

    }
}