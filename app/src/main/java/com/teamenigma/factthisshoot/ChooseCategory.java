package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import classes.Category;
import classes.Item;

public class ChooseCategory extends AppCompatActivity {

    Button buttonCategory1;

    Category category;

    public void createCategory() {
        category = new Category();
        category.add(new Item("Is this working?", "Red", "House", "Dog", "Original"));
        category.add(new Item("What is the question?", "Red", "Hollow", "Dog", "Original"));
        category.add(new Item("What is the question again?", "Red", "Hollow", "Dog", "Original"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        buttonCategory1 = (Button)findViewById(R.id.buttonCategory1);
        buttonCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Game.class);
                createCategory();
                i.putExtra("category", category);
                startActivity(i);
            }
        });

    }
}
