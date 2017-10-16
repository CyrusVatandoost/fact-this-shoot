package classes;

import java.io.Serializable;

/**
 * Created by Cyrus on 5 Oct 2017.
 */

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Item implements Serializable {

    String question;
    String answer;
    String option1, option2, option3;

    public Item(String question, String answer, String option1, String option2, String option3) {
        this.question = question;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    public boolean answer(String answer) {
        if(this.answer == answer)
            return true;
        return false;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

}
