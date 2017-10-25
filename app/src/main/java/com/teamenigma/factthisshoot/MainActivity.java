package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.util.concurrent.TimeUnit;

import classes.GoogleApiClientSingleton;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AdView mAdView;
    private GoogleApiClient mGoogleApiClient;

    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private boolean mExplicitSignOut = false;
    private boolean mInSignInFlow = false;  // set to true when you're in the middle of the sign in flow to know you should not attempt to connect in onStart()

    final int REQUEST_ACHIEVEMENTS = 9004;
    final int REQUEST_LEADERBOARD = 9002;
    final int REQUEST_ALL_LEADERBOARDS = 9003;
    final int RC_RESOLVE = 9001;

    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
            GoogleApiClientSingleton.getInstance(mGoogleApiClient); // attaching the GoogleApiClient to the Singleton class
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled, launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-1026861718675475/3907447580");   // ad-id
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice("AE1B587BD46BE1BA09CB8C9CBFDF2C16");
        }
        mAdView.loadAd(adRequest.build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

        ImageView imageStart = (ImageView)findViewById(R.id.imageStart);
        imageStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChooseCategory.class);
                startActivity(i);
            }
        });

        Button buttonHighScores = (Button)findViewById(R.id.buttonLocalHighScores);
        buttonHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HighScores.class);
                startActivity(i);
            }
        });

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();
        GoogleApiClientSingleton.getInstance(mGoogleApiClient);

        // Sign in button
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start the asynchronous sign in flow
                mSignInClicked = true;
                mGoogleApiClient.connect();
/*                if(mGoogleApiClient.isConnected())
                    loadAchievements();*/
            }
        });

        // Sign out button
        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sign out.
                mSignInClicked = false;
                Games.signOut(mGoogleApiClient);
                // user explicitly signed out, so turn off auto sign in
                mExplicitSignOut = true;
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Games.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                // show sign-in button, hide the sign-out button
                findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.buttonOnlineHighScores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getBaseContext().getString(R.string.leaderboard_top_scorers_dogs)), 10);
            }
        });

        findViewById(R.id.buttonHelloWorld).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Games.Achievements.unlock(mGoogleApiClient, getBaseContext().getString(R.string.achievement_hello_world));
            }
        });

        // View all achievements button
        findViewById(R.id.buttonAchievements).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
            }
        });

    }

    public void loadAchievements()  {

        boolean fullLoad = false;  // set to 'true' to reload all achievements (ignoring cache)
        long waitTime = 60;    // seconds to wait for achievements to load before timing out

        // load achievements
        PendingResult p = Games.Achievements.load(mGoogleApiClient, fullLoad);
        Achievements.LoadAchievementsResult r = (Achievements.LoadAchievementsResult)p.await(waitTime, TimeUnit.SECONDS );
        int status = r.getStatus().getStatusCode();
        if ( status != GamesStatusCodes.STATUS_OK )  {
            r.release();
            return;           // Error Occured
        }

        // cache the loaded achievements
        AchievementBuffer buf = r.getAchievements();
        int bufSize = buf.getCount();
        for (int i = 0; i < bufSize; i++)  {
            Achievement ach = buf.get(i);

            // here you now have access to the achievement's data
            String id = ach.getAchievementId();  // the achievement ID string
            boolean unlocked = ach.getState() == Achievement.STATE_UNLOCKED;  // is unlocked
            boolean incremental = ach.getType() == Achievement.TYPE_INCREMENTAL;  // is incremental
            int steps = 0;
            if (incremental)
                steps = ach.getCurrentSteps();  // current incremental steps
        }
        buf.close();
        r.release();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // show sign-out button, hide the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

        // (your code here: update UI, enable functionality that depends on sign in, etc)
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
