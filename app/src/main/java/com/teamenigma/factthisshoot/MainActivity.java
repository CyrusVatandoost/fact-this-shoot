package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import classes.BitmapBytesConverter;
import classes.Database.DatabaseHelper;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button buttonSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChooseCategory.class);
                startActivity(i);
            }
        });

        /*
        Settings Activity
         */
        /*
        buttonSettings = (Button)findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Settings.class);

                startActivity(i);
            }
        });
        */



    }


}
