package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import classes.Item;
import classes.Category;

/**
 * Created by Cyrus on 12/10/2017.
 */

public class Game extends AppCompatActivity {

    Category category;

    ImageView imageQuestion;
    Button buttonA, buttonB, buttonC, buttonD;
    TextView textViewScore, textViewTimer, textViewHealth;
    int score = 0;
    int timer = 10; // The Game will start with 10 seconds left.
    int health = 3;

    Item item;
    Bitmap questionBitmap;
    String answer;
    ArrayList<String> optionList;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageQuestion = (ImageView) findViewById(R.id.imageQuestion) ;
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);
        textViewScore = (TextView)findViewById(R.id.textViewScore);
        textViewTimer = (TextView)findViewById(R.id.textViewTimer);
        textViewHealth = (TextView)findViewById(R.id.textViewHealth);

        Intent intent = getIntent();
        category = (Category)intent.getSerializableExtra("category");
        setQuestion();

        imageQuestion.setImageBitmap(questionBitmap);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonA.getText().toString())) {
                    buttonA.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonA.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    incorrect();
                }
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonB.getText().toString())) {
                    buttonB.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonB.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    incorrect();
                }
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonC.getText().toString())) {
                    buttonC.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonC.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    incorrect();
                }
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonD.getText().toString())) {
                    buttonD.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonD.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    incorrect();
                }
            }
        });

        startTimer();

    }

    /**
     * This function sets the variables for the new question.
     */
    private void setQuestion() {

        // If the Category still has Items, get one Item else end Game.
        if(category.canGet())
            item = category.getItem();
        else
            endGame();

        questionBitmap = item.getQuestion();
        answer = item.getAnswer();

        // clear existing list, add options, and shuffle
        optionList = new ArrayList<>();
        optionList.add(item.getOption1());
        optionList.add(item.getOption2());
        optionList.add(item.getOption3());
        optionList.add(item.getAnswer());
        Collections.shuffle(optionList);

        buttonA.setBackgroundResource(android.R.drawable.btn_default);
        buttonB.setBackgroundResource(android.R.drawable.btn_default);
        buttonC.setBackgroundResource(android.R.drawable.btn_default);
        buttonD.setBackgroundResource(android.R.drawable.btn_default);

        buttonA.getBackground().clearColorFilter();
        buttonB.getBackground().clearColorFilter();
        buttonC.getBackground().clearColorFilter();
        buttonD.getBackground().clearColorFilter();

        imageQuestion.setImageBitmap(questionBitmap);
        buttonA.setText(optionList.get(0));
        buttonB.setText(optionList.get(1));
        buttonC.setText(optionList.get(2));
        buttonD.setText(optionList.get(3));

        textViewScore.setText(score + "");
        textViewTimer.setText(timer + "");

    }

    /**
     * This function gets called every time the user clicks on a correct answer.
     */
    private void correct() {
        score++;    // increments score
        setQuestion();
        timer += 2; // 2 seconds gets added to timer if correct.
        checkGame();
    }

    /**
     * This function gets called every time the user clicks on an incorrect answer.
     */
    private void incorrect() {
        score--;
        timer--;
        health--;
        textViewScore.setText(score + "");
        textViewHealth.setText(health + "");
        checkGame();
    }

    /**
     * This function starts the timer which updates the timer every second.
     */
    private void startTimer() {
        final int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                timer--;
                updateTimer();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    /**
     * This function gets called every second.
     */
    private void updateTimer() {
        textViewTimer.setText(timer + "");
        checkGame();
    }

    /**
     * This function checks if the Game has been lost or not.
     * If game is over, go to class.GameOver while passing Integer.score
     */
    private void checkGame() {
        if(timer == 0 || score < 0) {
            endGame();
        }
    }

    private void endGame() {
        Intent intent = new Intent(Game.this, GameOver.class);
        intent.putExtra("score", score);
        handler.removeCallbacksAndMessages(null);
        finish();
        startActivity(intent);
    }

}
