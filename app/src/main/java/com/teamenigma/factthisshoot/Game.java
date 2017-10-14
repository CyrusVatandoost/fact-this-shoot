package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import classes.Item;

public class Game extends AppCompatActivity {

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

        Intent intent = getIntent();
        item = (Item)intent.getSerializableExtra("item");
        question = item.getQuestion();
        answer = item.getAnswer();
        optionA = item.getOption1();
        optionB = item.getOption2();
        optionC = item.getOption3();
        optionD = item.getAnswer();

        textQuestion = (TextView)findViewById(R.id.textQuestion);
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);

        textQuestion.setText(question);
        buttonA.setText(optionA);
        buttonB.setText(optionB);
        buttonC.setText(optionC);
        buttonD.setText(optionD);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonA.getText().toString())) {
                    buttonA.setBackgroundColor(Color.GREEN);
                    finish();
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
                    finish();
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
                    finish();
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
                    finish();
                }
                else {
                    buttonD.setBackgroundColor(Color.RED);
                }
            }
        });

    }

}
