package app.com.quiz.android.quizapp;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class SelectWebServiceClient extends AsyncTask<URL, Integer, String> {
    private View view;
    private WebServiceClient client;

    public SelectWebServiceClient(View view) {
        this.view = view;
        client = new WebServiceClient();
    }

    @Override
    protected String doInBackground(URL... params) {
        return client.GetWebServiceOutput(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.v("Webservice", "Adding quiz list");
        List<String> quizList = new Gson().fromJson(s, new TypeToken<List<String>>() {
        }.getType());
        ListAdapter quizSelectAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.list_select_item, quizList);
        ListView quizSelectView = (ListView) view.findViewById(R.id.quizSelectList);
        quizSelectView.setAdapter(quizSelectAdapter);
    }
}

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizSelectActivityFragment extends Fragment {
    private List<String> quizList;
    ListAdapter quizSelectAdapter = null;

    public QuizSelectActivityFragment() {
        quizList = new ArrayList<String>();
        /*quizList.add("Sports");
        quizList.add("General Knowledge");
        quizList.add("Current Affairs");
        quizList.add("Politics");*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz_select, container, false);
        quizSelectAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.list_select_item, quizList);
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
        URL newUrl = null;
        try {
            newUrl = new URL("http://10.0.2.2:8080/QuizService/api/quizapp/get_quiz_list");
            new SelectWebServiceClient(rootView).execute(newUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
