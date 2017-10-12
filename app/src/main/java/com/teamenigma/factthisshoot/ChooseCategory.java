package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import classes.Item;

public class ChooseCategory extends AppCompatActivity {

    Button buttonCategory1;
    Item sample = new Item("Is this working?", "Cat", "Dog", "Bird", "Pewdiepie");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        buttonCategory1 = (Button)findViewById(R.id.buttonCategory1);
        buttonCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Game.class);
                i.putExtra("item", sample);
                startActivity(i);
            }
        });

    }
}
