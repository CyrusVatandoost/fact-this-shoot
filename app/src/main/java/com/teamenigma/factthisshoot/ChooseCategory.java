package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import classes.BitmapBytesConverter;
import classes.Category;
import classes.DatabaseHelper;
import classes.Item;

/**
 * Created by Cyrus on 12/10/2017.
 */

public class ChooseCategory extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        dbHelper = new DatabaseHelper(this);
        setupDatabase();
        createCategoryList();
    }

    private void displayCategory(String categoryName){
        Cursor data = dbHelper.getCategoryData(categoryName);
        while(data.moveToNext())
            Log.d("ITEM", data.getInt(0) + " " + data.getString(1) + " " + data.getString(3));//Print in console for debugging
    }

    public Category getCategory(String categoryName) {
        Category temp = new Category(categoryName);
        Cursor data = dbHelper.getCategoryData(categoryName); // Get all of the data within the specified category
        /*
        Iterate through every tuple in the data.
        For every tuple (ID | Name | Image | Category), a question/item will be made where the tuple's Name is the correct answer.
        The other three choices that are wrong will be randomly selected.
         */
        while(data.moveToNext()) {
            Log.d("ITEM", data.getInt(0) + " " + data.getString(1) + " " + data.getInt(2) + " " + data.getString(3));//Print in console for debugging

            int pictureID = data.getInt(2); //Retrieve the image ID of the answer
            String answer = data.getString(1);//Retrieve the correct answer

            String[] wrongAnswers = new String[3];//The list of the answers that are wrong.

            List<String> answers = new ArrayList<>(); // List of ALL of the answers. The purpose of this list is to avoid repetition of selecting the same answer.

            answers.add(answer); //Add the correct answer into the list.

            // Retrieve the other three random wrong answers
            for (int i = 0 ; i < 3; i++) {
                String wrongAnswer = dbHelper.getWrongAnswerID(answers, categoryName);//Retrieve the wrong answer
                answers.add(wrongAnswer);//Add the wrong answer to the list of answers to avoid selecting it again
                wrongAnswers[i] = wrongAnswer;//Add the name to the list of wrong answers
            }

            temp.add(new Item(pictureID, answer, wrongAnswers[0], wrongAnswers[1], wrongAnswers[2])); //Create new Item and add to the category.
        }
        temp.shuffleItems();    // This randomizes the Items in the Category.
        return temp;
    }

    /**
     * This function adds makes Button(s) for all class.Category for it to be placed in the linear layout of class.ChooseCategory.
     */
    private void createCategoryList() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutCategory);
        ArrayList<Category> categoryList = new ArrayList<>();

        // Add the categories here.
        categoryList.add(getCategory("Dogs"));
        categoryList.add(getCategory("Planets"));
        categoryList.add(getCategory("Flowers"));

        for(final Category c : categoryList) {

            displayCategory(c.getName());

            Button button = new Button(new ContextThemeWrapper(this, R.style.CategoryButton), null, 0);
            button.setText(c.getName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), Game.class);
                    i.putExtra("category", c);
                    startActivity(i);
                }
            });
            linearLayout.addView(button);
        }
    }

    private void setupDatabase() {
        //Insert Dogs
        dbHelper.insertData("Beagle", R.drawable.dogs_beagle,  "Dogs");
        dbHelper.insertData("Bulldog", R.drawable.dogs_bulldog,  "Dogs");
        dbHelper.insertData("Chowchow", R.drawable.dogs_chowchow,  "Dogs");
        dbHelper.insertData("Daschund", R.drawable.dogs_daschund,  "Dogs");
        dbHelper.insertData("Doberman Pinscher", R.drawable.dogs_dobermanpinscher,  "Dogs");
        dbHelper.insertData("German Shepherd", R.drawable.dogs_germanshepherd,  "Dogs");
        dbHelper.insertData("Golden Retriever", R.drawable.dogs_goldenretriever,  "Dogs");
        dbHelper.insertData("Great Dane",R.drawable.dogs_greatdane,  "Dogs");
        dbHelper.insertData("Labrador", R.drawable.dogs_labrador,  "Dogs");
        dbHelper.insertData("Pomeranian",R.drawable.dogs_pomeranian,  "Dogs");
        dbHelper.insertData("Poodle", R.drawable.dogs_poodle,  "Dogs");
        dbHelper.insertData("Pug", R.drawable.dogs_pug, "Dogs");
        dbHelper.insertData("Rottweiler", R.drawable.dogs_rottweiler,  "Dogs");
        dbHelper.insertData("Siberian Husky", R.drawable.dogs_siberianhusky,  "Dogs");

        //Insert Planets
        dbHelper.insertData("Earth", R.drawable.planet_earth, "Planets");
        dbHelper.insertData("Jupiter", R.drawable.planet_jupiter, "Planets");
        dbHelper.insertData("Mars", R.drawable.planet_mars, "Planets");
        dbHelper.insertData("Mercury", R.drawable.planet_mercury, "Planets");
        dbHelper.insertData("Neptune", R.drawable.planet_neptune, "Planets");
        dbHelper.insertData("Saturn", R.drawable.planet_saturn, "Planets");
        dbHelper.insertData("Uranus", R.drawable.planet_uranus, "Planets");
        dbHelper.insertData("Venus", R.drawable.planet_venus, "Planets");

        //Insert Flowers
        dbHelper.insertData("Cannabis", R.drawable.flowers_cannabis, "Flowers");
        dbHelper.insertData("Daffodil", R.drawable.flowers_daffodil, "Flowers");
        dbHelper.insertData("Hibiscus", R.drawable.flowers_hibiscus, "Flowers");
        dbHelper.insertData("Hyacinth", R.drawable.flowers_hyacinth, "Flowers");
        dbHelper.insertData("Lavender", R.drawable.flowers_lavender, "Flowers");
        dbHelper.insertData("Lilac", R.drawable.flowers_lilac, "Flowers");
        dbHelper.insertData("Lily", R.drawable.flowers_lily, "Flowers");
        dbHelper.insertData("Orchid", R.drawable.flowers_orchid, "Flowers");
        dbHelper.insertData("Rose", R.drawable.flowers_rose, "Flowers");
        dbHelper.insertData("Sunflower", R.drawable.flowers_sunflower, "Flowers");
    }
}
