package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import classes.Category;
import classes.Item;

/**
 * Created by Cyrus on 12/10/2017.
 */

public class Game extends AppCompatActivity {

    Category category;

    TextView textQuestion;
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;

    Item item;
    String question;
    String answer;
    String optionA;
    String optionB;
    String optionC;
    String optionD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        textQuestion = (TextView)findViewById(R.id.textQuestion);
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);

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
        if(category.canGet())
            item = category.getItem();
        else
            finish();

        question = item.getQuestion();
        answer = item.getAnswer();
        optionA = item.getOption1();
        optionB = item.getOption2();
        optionC = item.getOption3();
        optionD = item.getAnswer();

        buttonA.setBackgroundResource(android.R.drawable.btn_default);
        buttonB.setBackgroundResource(android.R.drawable.btn_default);
        buttonC.setBackgroundResource(android.R.drawable.btn_default);
        buttonD.setBackgroundResource(android.R.drawable.btn_default);

        textQuestion.setText(question);
        buttonA.setText(optionA);
        buttonB.setText(optionB);
        buttonC.setText(optionC);
        buttonD.setText(optionD);
    }

    public void correct() {
        setQuestion();
    }

}
