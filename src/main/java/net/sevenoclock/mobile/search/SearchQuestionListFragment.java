package net.sevenoclock.mobile.search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.RefreshScrollView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class SearchQuestionListFragment extends Fragment {

    private Context con;
    private int unit_id;

    private LinearLayout ll_search_question_list_left;
    private LinearLayout ll_search_question_list_right;
    private RefreshScrollView sv_search_question_list_scrollview;

    private int element_count = 0;
    private boolean semaphore = true;

    Values values;

    public static SearchQuestionListFragment newInstance(int unit_id, String title) {
        SearchQuestionListFragment view = new SearchQuestionListFragment();
        Bundle args = new Bundle();
        args.putInt("id", unit_id);
        args.putString("title", title);
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        this.unit_id = getArguments().getInt("id");

        View v = inflater.inflate(R.layout.fragment_search_question_list, container, false);

        MainActivity.setTitle(getArguments().getString("title"));
        MainActivity.setSubtitle("");

        ll_search_question_list_left = (LinearLayout) v.findViewById(R.id.ll_search_question_list_left);
        ll_search_question_list_right = (LinearLayout) v.findViewById(R.id.ll_search_question_list_right);
        sv_search_question_list_scrollview = (RefreshScrollView) v.findViewById(R.id.sv_search_question_list_scrollview);


        sv_search_question_list_scrollview.setOnScrollViewListener(new RefreshScrollView.OnScrollViewListener() {
            public void onScrollChanged(RefreshScrollView v, int l, int t, int oldl, int oldt) {
                View view = (View) v.getChildAt(v.getChildCount() - 1);
                int diff = view.getBottom() - (v.getHeight() + v.getScrollY());
                if (diff == 0) {
                    int view_count = ll_search_question_list_left.getChildCount() + ll_search_question_list_right.getChildCount();
                    if (view_count >= element_count && semaphore) readMore();
                }
            }
        });

        new AddQuestionTask().execute(null, null, null);

        return v;
    }

    public void reflesh(){
        ll_search_question_list_left.removeAllViews();
        ll_search_question_list_right.removeAllViews();
        new AddQuestionTask().execute(null, null, null);
    }

    public void readMore(){
        new AddQuestionTask().execute(null, null, null);
    }

    class AddQuestionTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_question;
        int count_left;
        int count_right;

        @Override
        protected void onPreExecute() {
            semaphore = false;
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET("get_question_list&u5=" + unit_id + "&limit="+element_count+":"+(element_count+10));
            if(ja_question == null) return false;
            element_count += 10;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_question.length(); i++) {
                            SearchQuestionView iqv = null;
                            try {
                                TryCatchJO tcjo = new TryCatchJO(ja_question.getJSONObject(i));
                                iqv = new SearchQuestionView(con, tcjo);

                                iqv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new QuestionPagerFragment().newInstance(((SearchQuestionView)v).tcjo));
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            count_left = ll_search_question_list_left.getChildCount();
                            count_right = ll_search_question_list_right.getChildCount();

                            if (count_left > count_right) ll_search_question_list_right.addView(iqv);
                            else ll_search_question_list_left.addView(iqv);
                        }
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
            semaphore = true;
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}