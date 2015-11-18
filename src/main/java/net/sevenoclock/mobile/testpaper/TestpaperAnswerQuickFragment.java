package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperAnswerQuickFragment extends Fragment {

    private Context con;
    private JSONArray ja_question;

    private ListView lv_testpaper_input_quick_list;

    TestpaperAnswerResultQuickAdapter tarq;

    Values values;

    public static TestpaperAnswerQuickFragment newInstance(JSONArray ja) {
        TestpaperAnswerQuickFragment view = new TestpaperAnswerQuickFragment();
        Bundle args = new Bundle();
        args.putString("array", ja.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        try {
            ja_question = new JSONArray(getArguments().getString("array"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_quiz_input_quick, container, false);

        MainActivity.setTitle("빠른 정답 보기");
        MainActivity.setSubtitle("");

        lv_testpaper_input_quick_list = (ListView) v.findViewById(R.id.lv_quiz_input_quick_list);

        tarq = new TestpaperAnswerResultQuickAdapter();

        new LayoutTask().execute(null, null, null);

        return v;
    }

    class LayoutTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                lv_testpaper_input_quick_list.setAdapter(tarq);
                lv_testpaper_input_quick_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        try {
                            Functions.history_go(con, new QuestionPagerFragment().newInstance(new TryCatchJO(ja_question.getJSONObject(position))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_question.length(); i++) {
                            try {
                                tarq.add(new TryCatchJO(ja_question.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
            }
            return;
        }
    }
}