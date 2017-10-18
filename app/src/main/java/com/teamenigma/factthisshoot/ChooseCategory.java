package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    Button buttonCategory1;
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

            Log.d("ITEM", data.getInt(0) + " " + data.getString(1) + " " + data.getBlob(2) + " " + data.getString(3));//Print in console for debugging
            byte[] picture = data.getBlob(2);//Retrieve the image of the answer as a byte array
            String answer = data.getString(1);//Retrieve the correct answer
            int answerID = data.getInt(0);//Retrieve the ID of the answer.
            String[] wrongAnswers = new String[3];//The list of the answers that are wrong.

            // List of ALL of the answers' IDs. The purpose of this list is to avoid repetition of selecting the same answer.
            List<Integer> answerIDs = new ArrayList<Integer>();

            answerIDs.add(answerID);//Add the ID of the correct answer into the list.

            // Retrieve the other three random wrong answers
            for (int i = 0 ; i < 3; i++) {
                int wrongAnswerID = dbHelper.getWrongAnswerID(answerIDs, categoryName);//Retrieve the ID of the wrong answer
                answerIDs.add(wrongAnswerID);//Add the ID of the wrong answer to the list of answers to avoid selecting it again
                wrongAnswers[i] = dbHelper.getName(wrongAnswerID);//Add the name to the list of wrong answers
            }

            //Create new Item and add to the category.
            category.add(new Item(picture, answer, wrongAnswers[0], wrongAnswers[1], wrongAnswers[2]));
        }

        category.shuffleItems();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        dbHelper = new DatabaseHelper(this);
        setupDatabase();
        buttonCategory1 = (Button)findViewById(R.id.buttonCategory1);
        buttonCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Game.class);
                createCategory("Dogs");
                i.putExtra("category", category);
                startActivity(i);
            }
        });
    }

    private void setupDatabase() {
        dbHelper.insertData("Beagle", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.beagle)),  "Dogs");
        dbHelper.insertData("Bulldog", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.bulldog)),  "Dogs");
        dbHelper.insertData("Chowchow", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.chowchow)),  "Dogs");
        dbHelper.insertData("Daschund", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.daschund)),  "Dogs");
        dbHelper.insertData("Doberman Pinscher", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.doberman_pinscher)),  "Dogs");
        dbHelper.insertData("German Shepherd", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.german_shepherd)),  "Dogs");
        dbHelper.insertData("Golden Retriever", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.golden_retriever)),  "Dogs");
        dbHelper.insertData("Great Dane", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.great_dane)),  "Dogs");
        dbHelper.insertData("Labrador", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.labrador)),  "Dogs");
        dbHelper.insertData("Pomeranian", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.pomeranian)),  "Dogs");
        dbHelper.insertData("Poodle", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.poodle)),  "Dogs");
        dbHelper.insertData("Pug", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.pug)),  "Dogs");
        dbHelper.insertData("Rottweiler", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.rottweiler)),  "Dogs");
        dbHelper.insertData("Siberian Husky", BitmapBytesConverter.getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.siberian_husky)),  "Dogs");
    }
}
