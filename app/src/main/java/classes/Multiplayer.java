package classes;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.Log;

import com.teamenigma.factthisshoot.ChooseCategory;
import com.teamenigma.factthisshoot.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rgee on 14/12/2017.
 */

public class Multiplayer extends Game
{
    private int opponentScore = 0;


    public int getOpponentScore() {
        return opponentScore;
    }

    public void addOpponentScore(int opponentScore) {
        this.opponentScore += opponentScore;
    }

    public Multiplayer()
    {
        category = getCategory("Mixed", R.drawable.dogs_beagle);


    }

    public Category getCategory(String categoryName, int imageID) {
        //Log.i("ChooseCategory", "getCategory(" + categoryName + ")");
        Category temp = new Category(categoryName, imageID);



        temp.add(new Item(R.drawable.afghanistan, "Afghanistan", "Brazil", "Bulgaria", "Madagascar"));
        temp.add(new Item(R.drawable.france, "France", "Australia", "Belgium", "Singapore"));

        temp.add(new Item(R.drawable.flowers_calla, "Calla", "Lavender", "Snapdragon", "Dahlia"));
        temp.add(new Item(R.drawable.flowers_viburnum, "Viburnum", "Poinsettia", "Sunflower", "Orchid"));

        temp.add(new Item(R.drawable.sports_basketball, "Basketball", "Football", "Swimming", "Golf"));
        temp.add(new Item(R.drawable.sports_baseball, "Baseball", "Bowling", "Archery", "Darts"));

        temp.add(new Item(R.drawable.planet_earth, "Earth", "Mars", "Saturn", "Jupiter"));
        temp.add(new Item(R.drawable.planet_mars, "Mars", "Uranus", "Venus", "Neptune"));




        temp.shuffleItems();    // This randomizes the Items in the Category.
        return temp;
    }



}
