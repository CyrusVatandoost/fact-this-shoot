package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    Button buttonPlayAgain;
    Button buttonMenu;
    TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Goes to ChooseCategory. Will be changed to Game soon.
        buttonPlayAgain = (Button)findViewById(R.id.buttonPlayAgain);
        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, ChooseCategory.class);
                startActivity(intent);
                finish();
            }
        });

        // Goes to Menu
        buttonMenu = (Button)findViewById(R.id.buttonMenu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
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
        textViewScore.setText(score + "");

    }
}
