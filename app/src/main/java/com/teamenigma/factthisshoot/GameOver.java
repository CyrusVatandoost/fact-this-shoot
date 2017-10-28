package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import classes.GoogleApiClientSingleton;

public class GameOver extends AppCompatActivity {

    private GoogleApiClientSingleton singleton = GoogleApiClientSingleton.getInstance(null);
    private TextView textViewScore;
    final int REQUEST_LEADERBOARD = 9002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Goes to ChooseCategory. Will be changed to Game soon.
        findViewById(R.id.buttonPlayAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, ChooseCategory.class);
                startActivity(intent);
                finish();
            }
        });

        // Goes to Menu
        findViewById(R.id.buttonMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textViewScore = (TextView)findViewById(R.id.textViewScore);

        // Gets the score from the last activity (Game).
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        String category = intent.getStringExtra("category");
        textViewScore.setText(score + "");

        findViewById(R.id.buttonLocalHighScores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, HighScores.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.buttonOnlineHighScores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(singleton.getGoogleApiClient().isConnected())
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(singleton.getGoogleApiClient(), getBaseContext().getString(R.string.leaderboard_top_scorers_dogs)), REQUEST_LEADERBOARD);
            }
        });

        if(singleton.getGoogleApiClient().isConnected()) {
            if(category.equalsIgnoreCase("dogs")) {
                Games.Leaderboards.submitScore(singleton.getGoogleApiClient(), getBaseContext().getString(R.string.leaderboard_top_scorers_dogs), score);
                Log.i("GoogleApiClient", "Submitting high score for dogs: " + score);
            }
            else if(category.equalsIgnoreCase("planets")) {
                Games.Leaderboards.submitScore(singleton.getGoogleApiClient(), getBaseContext().getString(R.string.leaderboard_top_scorers_planets), score);
                Log.i("GoogleApiClient", "Submitting high score for planets: " + score);
            }
            else if(category.equalsIgnoreCase("flowers")) {
                Games.Leaderboards.submitScore(singleton.getGoogleApiClient(), getBaseContext().getString(R.string.leaderboard_top_scorers_flowers), score);
                Log.i("GoogleApiClient", "Submitting high score for flowers: " + score);
            }
            else if(category.equalsIgnoreCase("sports")) {
                Games.Leaderboards.submitScore(singleton.getGoogleApiClient(), getBaseContext().getString(R.string.leaderboard_top_scorers_sports), score);
                Log.i("GoogleApiClient", "Submitting high score for sports: " + score);
            }
        }

    }
}
