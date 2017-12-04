package classes;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Cyrus on 4 Dec 2017.
 */

public class Game extends AppCompatActivity {

    protected Category category;
    protected Item item;
    protected Bitmap bitmapQuestion;
    protected ArrayList<String> optionList;
    // score for player
    protected int score = 0;
    // timer for each question
    protected CountDownTimer countDownTimer;
    /*
     timer length for each question
     the Game will start with 10 seconds left
     */
    protected int timer = 5;

}
