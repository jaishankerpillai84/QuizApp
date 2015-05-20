package app.com.quiz.android.quizapp;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizSelectActivityFragment extends Fragment {
    private List<String> quizList;

    public QuizSelectActivityFragment() {
        quizList = new ArrayList<String>();
        quizList.add("Sports");
        quizList.add("General Knowledge");
        quizList.add("Current Affairs");
        quizList.add("Politics");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz_select, container, false);
        ListAdapter quizSelectAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.list_select_item, quizList);
        ListView quizSelectView = (ListView) rootView.findViewById(R.id.quizSelectList);
        quizSelectView.setAdapter(quizSelectAdapter);
        quizSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String quizName = (String) parent.getItemAtPosition(position);
                Intent playQuiz = new Intent(getActivity().getApplicationContext(), QuizActivity.class);
                playQuiz.putExtra("quizName", quizName);
                startActivity(playQuiz);
            }
        });
        return rootView;
    }
}
