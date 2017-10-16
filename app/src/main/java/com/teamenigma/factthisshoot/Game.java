package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    TextView scoreTextView;
    int score = 0;

    Item item;
    Bitmap questionBitmap;
    String answer;
    ArrayList<String> optionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageQuestion = (ImageView) findViewById(R.id.imageQuestion) ;
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);

        Intent intent = getIntent();
        category = (Category)intent.getSerializableExtra("category");
        setQuestion();

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonA.getText().toString())) {
                    buttonA.setBackgroundColor(Color.GREEN);
                    correct();
                }
                else {
                    buttonA.setBackgroundColor(Color.RED);
                }
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonB.getText().toString())) {
                    buttonB.setBackgroundColor(Color.GREEN);
                    correct();
                }
                else {
                    buttonB.setBackgroundColor(Color.RED);
                }
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonC.getText().toString())) {
                    buttonC.setBackgroundColor(Color.GREEN);
                    correct();
                }
                else {
                    buttonC.setBackgroundColor(Color.RED);
                }
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonD.getText().toString())) {
                    buttonD.setBackgroundColor(Color.GREEN);
                    correct();
                }
                else {
                    buttonD.setBackgroundColor(Color.RED);
                }
            }
        });


    }

    public void setQuestion() {

        // If the Category still has Items, get one Item else end Game.
        if(category.canGet())
            item = category.getItem();
        else
            finish();

        questionBitmap = getBitmap("beagle"); // the Bitmap gets assigned here
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

        imageQuestion.setImageBitmap(questionBitmap);
        buttonA.setText(optionList.get(0));
        buttonB.setText(optionList.get(1));
        buttonC.setText(optionList.get(2));
        buttonD.setText(optionList.get(3));

        scoreTextView.setText(score + "");
    }

    public void correct() {
        score++;    // increments score
        setQuestion();
    }

    public void incorrect() {

    }

    /**
     * This function gets the Bitmap from /resources from the String name
     * @param name
     * @return
     */
    public Bitmap getBitmap(String name) {
        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable", this.getPackageName());
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), resourceId);
        return icon;
    }

}
