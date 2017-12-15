package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
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

public class Game extends classes.Game {

    private ImageView imageQuestion, imageHeart1, imageHeart2, imageHeart3, imageCheck, imageCross;
    private Button buttonA, buttonB, buttonC, buttonD;
    private ImageButton imageButtonMute;
    private TextView textViewScore, textAddedScore;
    private ProgressBar progressBarHorizontal;

    protected Category category;
    private Item item;
    protected Bitmap questionBitmap;
    private ArrayList<String> optionList;

    private int score = 0;
    private int timer = 5; // The Game will start with 10 seconds left.
    private int health = 3;

    private SoundPool soundPool;
    private int soundCorrect, soundIncorrect;
    private boolean soundLoaded = false;
    private float actualVolume, maxVolume, volume;

    private CountDownTimer countDownTimer;

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
        textViewScore = (TextView)findViewById(R.id.textViewScore);
        textAddedScore = (TextView)findViewById(R.id.textAddedScore);
        progressBarHorizontal = (ProgressBar)findViewById(R.id.progressBarHorizonal);
        progressBarHorizontal.setMax(5);

        // Hide the Check and Cross graphics.
        imageCheck.setVisibility(View.INVISIBLE);
        imageCross.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        category = (Category)intent.getSerializableExtra("category");
        loadSounds();

        //getSupportActionBar().setTitle(category.getName());  // provide compatibility to all the versions

        imageQuestion.setImageBitmap(questionBitmap);

        buttonA = (Button)findViewById(R.id.buttonA);
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

        buttonB = (Button)findViewById(R.id.buttonB);
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

        buttonC = (Button)findViewById(R.id.buttonC);
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

        buttonD = (Button)findViewById(R.id.buttonD);
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

        imageButtonMute = (ImageButton)findViewById(R.id.imageButtonMute);
        imageButtonMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mute();
            }
        });

        countDownTimer = new CountDownTimer(5000, 10) {
            public void onTick(long millisUntilFinished) {
                progressBarHorizontal.setProgress( (int) millisUntilFinished / 1000);
                timer = (int) millisUntilFinished / 1000;
            }
            public void onFinish() {
                incorrect();
            }
        };

        textAddedScore.setVisibility(View.INVISIBLE);
        setQuestion();

    }

    @Override
    public void onBackPressed() {
        // This function is empty to disable the back button.
    }

    public void mute() {
        if(volume == 0) {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            volume = actualVolume / maxVolume;
            imageButtonMute.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }
        else {
            volume = 0;
            imageButtonMute.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
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
        if(category.canGet()) {
            item = category.getItem();

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

            // Clear the colors that were applied to the buttons.
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
            progressBarHorizontal.setProgress(timer);

            countDownTimer.start();
        }
        // If there are no more items left.
        else {
            countDownTimer.cancel();
            endGame();
        }

    }

    /**
     * This function gets called every time the user clicks on a correct answer.
     */
    private void correct() {
        countDownTimer.cancel();

        addScore(100 + (timer * 10));
        timer = 5;

        if (soundLoaded)
            soundPool.play(soundCorrect, volume, volume, 1, 0, 1f);

        // Display the check mark for 250 milliseconds.
        imageCheck.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCheck.setVisibility(View.INVISIBLE);
            }
        }, 250);

        setQuestion();
    }

    /**
     * This function gets called every time the user clicks on an incorrect answer.
     */
    private void incorrect() {
        countDownTimer.cancel();

        addScore(-100);
        health--;
        //updateHealth();
        timer = 5;

        if (soundLoaded)
            soundPool.play(soundIncorrect, volume, volume, 1, 0, 1f);

        // Display the cross mark for 250 milliseconds.
        imageCross.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCross.setVisibility(View.INVISIBLE);
            }
        }, 250);

        if(checkGame(health))
            setQuestion();
        else
            endGame();

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
     * This function adds points to the score.
     * It also displays how many points got added/subtracted for a second.
     * @param num
     */
    private void addScore(int num) {
        score += num;
        textAddedScore.setVisibility(View.VISIBLE);

        if(num > 0)
            textAddedScore.setText("+" + num);
        else if(num == 0)
            textAddedScore.setText("0");
        else if(num < 0)
            textAddedScore.setText(num + "");

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                textAddedScore.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private boolean checkGame(int health) {
        if(health > 0)
            return true;
        return false;
    }

    /**
     * This function gets called when the game ends.
     * It brings the user to the GameOver Activity.
     */
    private void endGame() {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(category.getName().equalsIgnoreCase("dogs")) {
            if(prefs.getInt("hs_dogs", 0) < score) {
                editor.putInt("hs_dogs", score);
            }
        }
        else if(category.getName().equalsIgnoreCase("planets")) {
            if(prefs.getInt("hs_planets", 0) < score)
                editor.putInt("hs_planets", score);
        }
        else if(category.getName().equalsIgnoreCase("flowers")) {
            if(prefs.getInt("hs_flowers", 0) < score)
                editor.putInt("hs_flowers", score);
        }
        else if(category.getName().equalsIgnoreCase("sports")) {
            if(prefs.getInt("hs_sports", 0) < score)
                editor.putInt("hs_sports", score);
        }
        else if(category.getName().equalsIgnoreCase("flags")) {
            if(prefs.getInt("hs_flags", 0) < score)
                editor.putInt("hs_flags", score);
        }
        editor.commit();

        countDownTimer.cancel();
        Intent intent = new Intent(Game.this, GameOver.class);
        intent.putExtra("score", score);
        intent.putExtra("category", category.getName());
        finish();
        startActivity(intent);
    }

}
