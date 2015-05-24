package app.com.quiz.android.quizapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Timer;

import android.util.Log;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class QuestionWebServiceClient extends AsyncTask<URL, Integer, String> {
    private View view;
    private QuizActivityFragment fragment;
    private WebServiceClient client;

    public QuestionWebServiceClient(View view, QuizActivityFragment fragment) {
        this.view = view;
        this.fragment = fragment;
        this.client = new WebServiceClient();
    }

    @Override
    protected String doInBackground(URL... params) {
        return client.GetWebServiceOutput(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("QuizApp", "JSON returnted is " + s);
        Log.i("QuizApp", "Adding quiz questions");
        fragment.questionDatas = new Gson().fromJson(s, new TypeToken<List<QuestionData>>() {
        }.getType());
        Log.i("QuizApp", "Number of questions is " + fragment.getNumQuestions());
        fragment.startQuiz(view);
        //fragment.startTimer(view);
    }
}

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizActivityFragment extends Fragment implements View.OnClickListener {
    List<QuestionData> questionDatas;
    private String quizName;
    int questionNo;
    Date startTime;
    Timer timer;

    public int getNumQuestions() {
        return questionDatas.size();
    }

    public QuizActivityFragment() {
        quizName = new String("");
        questionDatas = new ArrayList<QuestionData>();
        /*List<String> answerChoice = new ArrayList<String>();
        answerChoice.add("A");
        answerChoice.add("B");
        answerChoice.add("C");
        answerChoice.add("D");
        questionDatas.add(new QuestionData("What is the right choice", answerChoice, "A"));
        questionDatas.add(new QuestionData("What is the second choice", answerChoice, "B"));
        questionDatas.add(new QuestionData("What is the third choice", answerChoice, "C"));*/
        questionNo = 0;
    }

    public void startQuiz(View rootView) {
        TextView quizNameLabel = (TextView) rootView.findViewById(R.id.quizName);
        quizNameLabel.setText(quizName);
        TextView questionLabel = (TextView) rootView.findViewById(R.id.quizQuestion);
        Button choice1 = (Button) rootView.findViewById(R.id.choice1);
        Button choice2 = (Button) rootView.findViewById(R.id.choice2);
        Button choice3 = (Button) rootView.findViewById(R.id.choice3);
        Button choice4 = (Button) rootView.findViewById(R.id.choice4);
        Button newQuiz = (Button) rootView.findViewById(R.id.newQuiz);
        if(questionNo < questionDatas.size()) {
            QuestionData currentQuestionData = questionDatas.get(questionNo);
            questionLabel.setText(new StringBuilder("").append(questionNo + 1).append(". ").append(currentQuestionData.question).toString());
            choice1.setText(currentQuestionData.choices.get(0));
            choice2.setText(currentQuestionData.choices.get(1));
            choice3.setText(currentQuestionData.choices.get(2));
            choice4.setText(currentQuestionData.choices.get(3));
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

    /*public void startTimer(View rootView) {
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
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_take_quiz, container);
        Intent currentIntent = getActivity().getIntent();
        quizName = currentIntent.getStringExtra("quizName");
        URL newUrl = null;
        try {
            String urlString = new StringBuilder("http://10.0.2.2:8080/QuizService/api/quizapp/get_questions?quizname=").append(quizName).toString();
            Log.i("QuizApp", "Reading questions from " + urlString);
            newUrl = new URL(urlString);
            new QuestionWebServiceClient(rootView, this).execute(newUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        ++questionNo;
        startQuiz(v.getRootView());
    }
}
