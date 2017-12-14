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
    protected int ownScore = 0;

    // timer for each question
    protected CountDownTimer countDownTimer;
    /*
     timer length for each question
     the Game will start with 10 seconds left
     */
    protected int timer = 5;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Item getItem() {
        if(category.canGet())
        {
            item = category.getItem();
            return (item);
        }

        return null;
    }

    public ArrayList<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(ArrayList<String> optionList) {
        this.optionList = optionList;
    }

    public int getOwnScore() {
        return ownScore;
    }

    public void addOwnScore(int ownScore) {
        this.ownScore += ownScore;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
