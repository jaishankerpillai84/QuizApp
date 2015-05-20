package app.com.quiz.android.quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizActivityFragment extends Fragment implements View.OnClickListener {
    List<Question> questions;
    int questionNo;
    Date startTime;
    Timer timer;
    public QuizActivityFragment() {
        questions = new ArrayList<Question>();
        List<String> answerChoice = new ArrayList<String>();
        answerChoice.add("A");
        answerChoice.add("B");
        answerChoice.add("C");
        answerChoice.add("D");
        questions.add(new Question("What is the right choice", answerChoice, "A"));
        questions.add(new Question("What is the second choice", answerChoice, "B"));
        questions.add(new Question("What is the third choice", answerChoice, "C"));
        questionNo = 0;
    }

    public void displayView(View rootView) {
        startTime = new Date();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Date currentTime = new Date();
                            long elapsedTime = currentTime.getTime() - startTime.getTime();
                            SimpleDateFormat simpleDateFormat =
                                    new SimpleDateFormat("mm:ss");
                            final String timeText = simpleDateFormat.format(elapsedTime);
                            TextView timeLabel = (TextView) getActivity().findViewById(R.id.timeLabel);
                            timeLabel.setText(timeText);
                        }
                    });
            }
        }, 0, 1000);
        Intent currentIntent = getActivity().getIntent();
        String quizName = currentIntent.getStringExtra("quizName");
        TextView quizNameLabel = (TextView) rootView.findViewById(R.id.quizName);
        quizNameLabel.setText(quizName);
        TextView questionLabel = (TextView) rootView.findViewById(R.id.quizQuestion);
        Button choice1 = (Button) rootView.findViewById(R.id.choice1);
        Button choice2 = (Button) rootView.findViewById(R.id.choice2);
        Button choice3 = (Button) rootView.findViewById(R.id.choice3);
        Button choice4 = (Button) rootView.findViewById(R.id.choice4);
        Button newQuiz = (Button) rootView.findViewById(R.id.newQuiz);
        if(questionNo < questions.size()) {
            Question currentQuestion = questions.get(questionNo);
            questionLabel.setText(new StringBuilder("").append(questionNo + 1).append(". ").append(currentQuestion.question).toString());
            choice1.setText(currentQuestion.choiceList.get(0));
            choice2.setText(currentQuestion.choiceList.get(1));
            choice3.setText(currentQuestion.choiceList.get(2));
            choice4.setText(currentQuestion.choiceList.get(3));
            choice1.setOnClickListener(this);
            choice2.setOnClickListener(this);
            choice3.setOnClickListener(this);
            choice4.setOnClickListener(this);
            newQuiz.setVisibility(View.GONE);
        } else {
            questionLabel.setText("Quiz is complete");
            choice1.setVisibility(View.GONE);
            choice2.setVisibility(View.GONE);
            choice3.setVisibility(View.GONE);
            choice4.setVisibility(View.GONE);
            newQuiz.setVisibility(View.VISIBLE);
            newQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent selectQuiz = new Intent(getActivity(), QuizSelectActivity.class);
                    startActivity(selectQuiz);
                }
            });
            TextView timeLabel = (TextView) rootView.findViewById(R.id.timeLabel);
            timeLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_take_quiz, container);
        displayView(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        ++questionNo;
        displayView(v.getRootView());
    }
}
