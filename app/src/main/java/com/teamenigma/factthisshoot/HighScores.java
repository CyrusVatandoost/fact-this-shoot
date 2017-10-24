package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HighScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey",   Context.MODE_PRIVATE);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutHighScores);

        TextView scoreDogs = new TextView(this);
        scoreDogs.setText("Dogs: " + prefs.getInt("hs_dogs", 0));
        linearLayout.addView(scoreDogs);

        TextView scorePlanets = new TextView(this);
        scorePlanets.setText("Planets: " + prefs.getInt("hs_planets", 0));
        linearLayout.addView(scorePlanets);

        TextView scoreFlowers = new TextView(this);
        scoreFlowers.setText("Flowers: " + prefs.getInt("hs_flowers", 0));
        linearLayout.addView(scoreFlowers);
    }
}
