package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.Intent;
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
    Category category;

    public void createCategory(String categoryName) {
        category = new Category();
        Cursor data = dbHelper.getCategoryData(categoryName); // Get all of the data within the specified category
        /*
        Iterate through every tuple in the data.
        For every tuple (ID | Name | Image | Category), a question/item will be made where the tuple's Name is the correct answer.
        The other three choices that are wrong will be randomly selected.
         */
        while(data.moveToNext()) {
            Log.d("ITEM", data.getInt(0) + " " + data.getString(1) + " " + data.getInt(2) + " " + data.getString(3));//Print in console for debugging
            int picture = data.getInt(2);//Retrieve the image of the answer as a byte array
            String answer = data.getString(1);//Retrieve the correct answer
            int answerID = data.getInt(0);//Retrieve the ID of the answer.
            String[] wrongAnswers = new String[3];//The list of the answers that are wrong.
            List<Integer> answerIDs = new ArrayList(); // List of ALL of the answers' IDs. The purpose of this list is to avoid repetition of selecting the same answer.
            answerIDs.add(answerID); //Add the ID of the correct answer into the list.
            // Retrieve the other three random wrong answers
            for (int i = 0 ; i < 3; i++) {
                int wrongAnswerID = dbHelper.getWrongAnswerID(answerIDs, categoryName);//Retrieve the ID of the wrong answer
                answerIDs.add(wrongAnswerID);//Add the ID of the wrong answer to the list of answers to avoid selecting it again
                wrongAnswers[i] = dbHelper.getName(wrongAnswerID);//Add the name to the list of wrong answers
            }
            category.add(new Item(picture, answer, wrongAnswers[0], wrongAnswers[1], wrongAnswers[2])); //Create new Item and add to the category.
        }
        category.shuffleItems();

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
            //byte[] picture = data.getBlob(2);//Retrieve the image of the answer as a byte array
            int pictureID = data.getInt(2);
            String answer = data.getString(1);//Retrieve the correct answer
            int answerID = data.getInt(0);//Retrieve the ID of the answer.
            String[] wrongAnswers = new String[3];//The list of the answers that are wrong.
            List<Integer> answerIDs = new ArrayList(); // List of ALL of the answers' IDs. The purpose of this list is to avoid repetition of selecting the same answer.
            answerIDs.add(answerID); //Add the ID of the correct answer into the list.
            // Retrieve the other three random wrong answers
            for (int i = 0 ; i < 3; i++) {
                int wrongAnswerID = dbHelper.getWrongAnswerID(answerIDs, categoryName);//Retrieve the ID of the wrong answer
                answerIDs.add(wrongAnswerID);//Add the ID of the wrong answer to the list of answers to avoid selecting it again
                wrongAnswers[i] = dbHelper.getName(wrongAnswerID);//Add the name to the list of wrong answers
            }

            temp.add(new Item(pictureID, answer, wrongAnswers[0], wrongAnswers[1], wrongAnswers[2])); //Create new Item and add to the category.
        }
        temp.shuffleItems();
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        dbHelper = new DatabaseHelper(this);
        setupDatabase();
        createCategoryList();
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
        dbHelper.insertData("Beagle", R.drawable.beagle,  "Dogs");
        dbHelper.insertData("Bulldog", R.drawable.bulldog,  "Dogs");
        dbHelper.insertData("Chowchow", R.drawable.chowchow,  "Dogs");
        dbHelper.insertData("Daschund", R.drawable.daschund,  "Dogs");
        dbHelper.insertData("Doberman Pinscher", R.drawable.doberman_pinscher,  "Dogs");
        dbHelper.insertData("German Shepherd", R.drawable.german_shepherd,  "Dogs");
        dbHelper.insertData("Golden Retriever", R.drawable.golden_retriever,  "Dogs");
        dbHelper.insertData("Great Dane",R.drawable.great_dane,  "Dogs");
        dbHelper.insertData("Labrador", R.drawable.labrador,  "Dogs");
        dbHelper.insertData("Pomeranian",R.drawable.pomeranian,  "Dogs");
        dbHelper.insertData("Poodle", R.drawable.poodle,  "Dogs");
        dbHelper.insertData("Pug", R.drawable.pug,  "Dogs");
        dbHelper.insertData("Rottweiler", R.drawable.rottweiler,  "Dogs");
        dbHelper.insertData("Siberian Husky", R.drawable.siberian_husky,  "Dogs");

        //Insert Planets
        dbHelper.insertData("Earth",    R.drawable.earth, "Planets");
        dbHelper.insertData("Jupiter",  R.drawable.jupiter, "Planets");
        dbHelper.insertData("Mars",     R.drawable.mars, "Planets");
        dbHelper.insertData("Mercury", R.drawable.mercury, "Planets");
        dbHelper.insertData("Neptune", R.drawable.neptune, "Planets");
        dbHelper.insertData("Saturn", R.drawable.saturn, "Planets");
        dbHelper.insertData("Uranus", R.drawable.uranus, "Planets");
        dbHelper.insertData("Venus", R.drawable.venus, "Planets");

        //Insert Flowers
        dbHelper.insertData("Cannabis", R.drawable.cannabis, "Flowers");
        dbHelper.insertData("Daffodil", R.drawable.daffodil, "Flowers");
        dbHelper.insertData("Hibiscus", R.drawable.hibiscus, "Flowers");
        dbHelper.insertData("Hyacinth", R.drawable.hyacinth, "Flowers");
        dbHelper.insertData("Lavender", R.drawable.lavender, "Flowers");
        dbHelper.insertData("Lilac", R.drawable.lilac, "Flowers");
        dbHelper.insertData("Lily", R.drawable.lily, "Flowers");
        dbHelper.insertData("Orchid", R.drawable.orchids, "Flowers");
        dbHelper.insertData("Rose", R.drawable.roses, "Flowers");
        dbHelper.insertData("Sunflower", R.drawable.sunflower, "Flowers");

    }
}
