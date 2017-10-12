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

        // When user clicks on the correct button
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonA.setBackgroundColor(Color.RED);
            }
        });

        // When user clicks on the correct button
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonB.setBackgroundColor(Color.RED);
            }
        });

        // When user clicks on the correct button
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonC.setBackgroundColor(Color.RED);
            }
        });

        // When user clicks on the correct button
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonD.setBackgroundColor(Color.GREEN);
                finish();
            }
        });

    }

}
