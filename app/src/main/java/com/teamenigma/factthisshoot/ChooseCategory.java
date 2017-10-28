package com.teamenigma.factthisshoot;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import classes.Category;
import classes.CategoryViewAdapter;
import classes.DatabaseHelper;
import classes.GoogleApiClientSingleton;
import classes.Item;

/**
 * Created by Cyrus on 12/10/2017.
 */

public class ChooseCategory extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView categoriesListView;
    CategoryViewAdapter categoriesListViewAdapter;
    ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        categoriesListView = (ListView) findViewById(R.id.categoryListView);
        dbHelper = new DatabaseHelper(this);
        setupDatabase();
        createCategoryList();
    }

    private void displayCategory(String categoryName){
        Cursor data = dbHelper.getCategoryData(categoryName);
        while(data.moveToNext())
            Log.d("ITEM", data.getInt(0) + " " + data.getString(1) + " " + data.getString(3)); //Print in console for debugging
    }

    public Category getCategory(String categoryName, int imageID) {
        Category temp = new Category(categoryName, imageID);
        Cursor data = dbHelper.getCategoryData(categoryName); // Get all of the data within the specified category
        /*
        Iterate through every tuple in the data.
        For every tuple (ID | Name | Image | Category), a question/item will be made where the tuple's Name is the correct answer.
        The other three choices that are wrong will be randomly selected.
         */

            while(data.moveToNext()) {

                //Log.d("ITEM", data.getInt(0) + " " + data.getString(1) + " " + data.getInt(2) + " " + data.getString(3));//Print in console for debugging

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

                temp.add(new Item(pictureID, answer, wrongAnswers[0], wrongAnswers[1], wrongAnswers[2])); //Create new Item and ad to the category.
            }

        temp.shuffleItems();    // This randomizes the Items in the Category.
        return temp;
    }

    /**
     * This function adds makes Button(s) for all class.Category for it to be placed in the linear layout of class.ChooseCategory.
     */
    private void createCategoryList() {
        categoryList = new ArrayList<>();
        // Add the categories here.
        categoryList.add(getCategory("Dogs", R.drawable.dogs_beagle));
        categoryList.add(getCategory("Planets", R.drawable.planet_earth));
        categoryList.add(getCategory("Flowers", R.drawable.flowers_sunflower));
        categoryList.add(getCategory("Sports", R.drawable.sports_bowling));
        categoryList.add(getCategory("Flags", R.drawable.philippines));

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        categoriesListViewAdapter = new CategoryViewAdapter(getApplicationContext(), categoryList, prefs);
        categoriesListView.setAdapter(categoriesListViewAdapter);
        categoriesListViewAdapter.notifyDataSetChanged();
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
        dbHelper.insertData("Australian Shepherd", R.drawable.dogs_australianshepherd,  "Dogs");
        dbHelper.insertData("Border Collie", R.drawable.dogs_bordercollie,  "Dogs");
        dbHelper.insertData("Maltese", R.drawable.dogs_maltese,  "Dogs");
        dbHelper.insertData("Shih Tzu", R.drawable.dogs_shihtzu,  "Dogs");
        dbHelper.insertData("Vizsla", R.drawable.dogs_vizsla,  "Dogs");
        dbHelper.insertData("Yorkshire Terrier", R.drawable.dogs_yorkshireterrier,  "Dogs");

        //Insert Planets
        dbHelper.insertData("Earth", R.drawable.planet_earth, "Planets");
        dbHelper.insertData("Jupiter", R.drawable.planet_jupiter, "Planets");
        dbHelper.insertData("Mars", R.drawable.planet_mars, "Planets");
        dbHelper.insertData("Mercury", R.drawable.planet_mercury, "Planets");
        dbHelper.insertData("Neptune", R.drawable.planet_neptune, "Planets");
        dbHelper.insertData("Saturn", R.drawable.planet_saturn, "Planets");
        dbHelper.insertData("Uranus", R.drawable.planet_uranus, "Planets");
        dbHelper.insertData("Venus", R.drawable.planet_venus, "Planets");
        dbHelper.insertData("51 Pegasi b", R.drawable.planet_51pegasib, "Planets");
        dbHelper.insertData("55 Cancri", R.drawable.planet_55cancri, "Planets");
        dbHelper.insertData("70 Virginis b", R.drawable.planet_70virginisb, "Planets");
        dbHelper.insertData("COROT - 7b", R.drawable.planet_corot7b, "Planets");
        dbHelper.insertData("Epsilon Eridani", R.drawable.planet_epsiloneridani, "Planets");
        dbHelper.insertData("Gliese 581 d", R.drawable.planet_gliese581d, "Planets");
        dbHelper.insertData("Gliese 436 b", R.drawable.planet_glise436b, "Planets");
        dbHelper.insertData("HD 189733b", R.drawable.planet_hd189733b, "Planets");
        dbHelper.insertData("HD 209458b", R.drawable.planet_hd209458b, "Planets");
        dbHelper.insertData("Kepler 10b", R.drawable.planet_kepler10b, "Planets");
        dbHelper.insertData("π Arae c", R.drawable.planet_piaraec, "Planets");
        dbHelper.insertData("Ursae Majoris b", R.drawable.planet_ursaemajorisb, "Planets");


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
        dbHelper.insertData("Calla", R.drawable.flowers_calla, "Flowers");
        dbHelper.insertData("Carnation", R.drawable.flowers_carnations, "Flowers");
        dbHelper.insertData("Chamelaucium", R.drawable.flowers_chamelaucium, "Flowers");
        dbHelper.insertData("Chrysanthemum", R.drawable.flowers_chrysanthemum, "Flowers");
        dbHelper.insertData("Curly Willow", R.drawable.flowers_curlywillow, "Flowers");
        dbHelper.insertData("Dahlia", R.drawable.flowers_dahlia, "Flowers");
        dbHelper.insertData("Poinsettia", R.drawable.flowers_poinsettia, "Flowers");
        dbHelper.insertData("Snapdragon", R.drawable.flowers_snapdragon, "Flowers");
        dbHelper.insertData("Statice", R.drawable.flowers_statice, "Flowers");
        dbHelper.insertData("Viburnum", R.drawable.flowers_viburnum, "Flowers");


        //Insert Sports
        dbHelper.insertData("American Football", R.drawable.sports_americanfootball, "Sports");
        dbHelper.insertData("Baseball", R.drawable.sports_baseball, "Sports");
        dbHelper.insertData("Basketball", R.drawable.sports_basketball, "Sports");
        dbHelper.insertData("Bowling", R.drawable.sports_bowling, "Sports");
        dbHelper.insertData("Boxing", R.drawable.sports_boxing, "Sports");
        dbHelper.insertData("Cue Sports", R.drawable.sports_cuesports, "Sports");
        dbHelper.insertData("Cycling", R.drawable.sports_cycling, "Sports");
        dbHelper.insertData("Darts", R.drawable.sports_darts, "Sports");
        dbHelper.insertData("Football", R.drawable.sports_football, "Sports");
        dbHelper.insertData("Ice Hockey", R.drawable.sports_icehockey, "Sports");
        dbHelper.insertData("Lacrosse", R.drawable.sports_lacrosse, "Sports");
        dbHelper.insertData("Swimming", R.drawable.sports_swimming, "Sports");
        dbHelper.insertData("Tennis", R.drawable.sports_tennis, "Sports");
        dbHelper.insertData("Archery", R.drawable.sports_archery, "Sports");
        dbHelper.insertData("Golf", R.drawable.sports_golf, "Sports");
        dbHelper.insertData("Gymnastics", R.drawable.sports_gymnastics, "Sports");
        dbHelper.insertData("Muay Thai", R.drawable.sports_muaythai, "Sports");
        dbHelper.insertData("Taekwondo", R.drawable.sports_taekwondo, "Sports");
        dbHelper.insertData("Track and Field", R.drawable.sports_trackandfield, "Sports");
        dbHelper.insertData("Wrestling", R.drawable.sports_wrestling, "Sports");

        //Insert Flags
        dbHelper.insertData("Afghanistan", R.drawable.afghanistan, "Flags");
        dbHelper.insertData("Argentina", R.drawable.argentina, "Flags");
        dbHelper.insertData("Australia", R.drawable.australia, "Flags");
        dbHelper.insertData("Bangladesh", R.drawable.bangladesh, "Flags");
        dbHelper.insertData("Belgium", R.drawable.belgium, "Flags");
        dbHelper.insertData("Brazil", R.drawable.brazil, "Flags");
        dbHelper.insertData("Bulgaria", R.drawable.bulgaria, "Flags");
        dbHelper.insertData("France", R.drawable.france, "Flags");
        dbHelper.insertData("Madagascar", R.drawable.madagascar, "Flags");
        dbHelper.insertData("Netherlands", R.drawable.netherlands, "Flags");
        dbHelper.insertData("New Zealand", R.drawable.newzealand, "Flags");
        dbHelper.insertData("Nigeria", R.drawable.nigeria, "Flags");
        dbHelper.insertData("Philippines", R.drawable.philippines, "Flags");
        dbHelper.insertData("Singapore", R.drawable.singapore, "Flags");
        dbHelper.insertData("South Africa", R.drawable.southafrica, "Flags");
        dbHelper.insertData("Spain", R.drawable.spain, "Flags");
        dbHelper.insertData("Thailand", R.drawable.thailand, "Flags");
        dbHelper.insertData("Uganda", R.drawable.uganda, "Flags");
        dbHelper.insertData("Vanuatu", R.drawable.vanuatu, "Flags");
        dbHelper.insertData("Zimbabwe", R.drawable.zimbabwe, "Flags");



    }
}