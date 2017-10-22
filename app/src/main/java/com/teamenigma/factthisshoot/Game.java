package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import classes.Item;
import classes.Category;

/**
 * Created by Cyrus on 12/10/2017.
 */

public class Game extends AppCompatActivity {

    private ImageView imageQuestion, imageHeart1, imageHeart2, imageHeart3, imageCheck, imageCross;
    private Button buttonA, buttonB, buttonC, buttonD;
    private TextView textViewScore, textViewTimer;
    private ProgressBar progressBarHorizonal;
    private int score = 0;
    private int timer = 5; // The Game will start with 10 seconds left.
    private int health = 3;

    private Category category;
    private Item item;
    private Bitmap questionBitmap;
    private ArrayList<String> optionList;

    final Handler handler = new Handler();

    private SoundPool soundPool;
    private int soundCorrect, soundIncorrect;
    private boolean soundLoaded = false;
    private float actualVolume, maxVolume, volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Declare the views.
        imageQuestion = (ImageView) findViewById(R.id.imageQuestion) ;
        imageHeart1 = (ImageView) findViewById(R.id.imageHeart1) ;
        imageHeart2 = (ImageView) findViewById(R.id.imageHeart2) ;
        imageHeart3 = (ImageView) findViewById(R.id.imageHeart3) ;
        imageCheck = (ImageView)findViewById(R.id.imageCheck);
        imageCross = (ImageView)findViewById(R.id.imageCross);
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);
        textViewScore = (TextView)findViewById(R.id.textViewScore);
        textViewTimer = (TextView)findViewById(R.id.textViewTimer);
        progressBarHorizonal = (ProgressBar)findViewById(R.id.progressBarHorizonal);
        progressBarHorizonal.setMax(5);

        imageCheck.setVisibility(View.INVISIBLE);
        imageCross.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        category = (Category)intent.getSerializableExtra("category");
        setQuestion();
        loadSounds();

        getSupportActionBar().setTitle(category.getName());  // provide compatibility to all the versions

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
                    buttonA.setEnabled(false);
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
                    buttonB.setEnabled(false);
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
                    buttonC.setEnabled(false);
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
                    buttonD.setEnabled(false);
                    incorrect();
                }
            }
        });

        startTimer();

    }

    @Override
    public void onBackPressed() {
        // This function is empty to disable the back button.
    }

    /**
     * This function loads the audio files into SoundPool.
     */
    public void loadSounds() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundLoaded = true;
            }
        });
        soundCorrect = soundPool.load(this, R.raw.correct1, 1);
        soundIncorrect = soundPool.load(this, R.raw.incorrect1, 1);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
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

        questionBitmap = BitmapFactory.decodeResource(getResources(), item.getQuestion());

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

        // Re-enables all buttons that were disabled when they were incorrectly clicked on.
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);

        textViewScore.setText(score + "");
        textViewTimer.setText(timer + "");
        progressBarHorizonal.setProgress(timer);

    }

    /**
     * This function gets called every time the user clicks on a correct answer.
     */
    private void correct() {
        score += 100;
        score += timer * 10;
        setQuestion();
        timer = 6;
        checkGame();

        if (soundLoaded) {
            soundPool.play(soundCorrect, volume, volume, 1, 0, 1f);
        }

        // Display the check mark for 250 milliseconds.
        imageCheck.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCheck.setVisibility(View.INVISIBLE);
            }
        }, 250);
    }

    /**
     * This function gets called every time the user clicks on an incorrect answer.
     */
    private void incorrect() {
        score -= 100;
        health--;
        textViewScore.setText(score + "");
        updateHealth();
        checkGame();
        if (soundLoaded) {
            soundPool.play(soundIncorrect, volume, volume, 1, 0, 1f);
        }

        // Display the cross mark for 250 milliseconds.
        imageCross.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCross.setVisibility(View.INVISIBLE);
            }
        }, 250);
    }

    /**
     * This function sets the hearts visible or not depending on the health value of the user.
     */
    private void updateHealth() {
        switch(health){
            case 0:
                imageHeart1.setVisibility(View.INVISIBLE);
                imageHeart2.setVisibility(View.INVISIBLE);
                imageHeart3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                imageHeart1.setVisibility(View.VISIBLE);
                imageHeart2.setVisibility(View.INVISIBLE);
                imageHeart3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                imageHeart1.setVisibility(View.VISIBLE);
                imageHeart2.setVisibility(View.VISIBLE);
                imageHeart3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                imageHeart1.setVisibility(View.VISIBLE);
                imageHeart2.setVisibility(View.VISIBLE);
                imageHeart3.setVisibility(View.VISIBLE);
                break;
        }
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
        progressBarHorizonal.setProgress(timer);
        checkGame();
    }

    /**
     * This function checks if the Game has been lost or not.
     * If game is over, go to class.GameOver while passing Integer.score
     */
    private void checkGame() {
        if(timer == 0 || health == 0) {
            endGame();
        }
    }

    /**
     * This function gets called when the game ends.
     * It brings the user to the GameOver Activity.
     */
    private void endGame() {
        Intent intent = new Intent(Game.this, GameOver.class);
        intent.putExtra("score", score);
        handler.removeCallbacksAndMessages(null);   // This stops the Handler timer from going off in the next activity.
        finish();
        startActivity(intent);
    }

}
