package app.com.quiz.android.quizapp;

/**
 * Created by jaypillai on 5/17/15.
 */
import java.util.List;

public class Question {
    public String question;

    public Question(String question, List<String> choiceList, String answer) {
        this.question = question;
        this.choiceList = choiceList;
        this.answer = answer;
    }

    public List<String> choiceList;
    public String answer;
}
