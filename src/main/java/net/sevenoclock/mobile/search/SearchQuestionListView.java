package net.sevenoclock.mobile.search;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.RefreshScrollView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class SearchQuestionListView extends LinearLayout {

    private Context con;
    private int unit_id;

    private LinearLayout ll_search_question_list_left;
    private LinearLayout ll_search_question_list_right;
    private RefreshScrollView sv_search_question_list_scrollview;

    private int element_count = 0;
    private boolean semaphore = true;

    Values values;

    public SearchQuestionListView(Context context, int unit_id, String title) {
        super(context);
        this.con = context;
        this.unit_id = unit_id;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_search_question_list, null), lp);

        ll_search_question_list_left = (LinearLayout)findViewById(R.id.ll_search_question_list_left);
        ll_search_question_list_right = (LinearLayout)findViewById(R.id.ll_search_question_list_right);
        sv_search_question_list_scrollview = (RefreshScrollView)findViewById(R.id.sv_search_question_list_scrollview);

        setTag(R.string.tag_main_title, title);
        setTag(R.string.tag_main_subtitle, "");

        sv_search_question_list_scrollview.setOnScrollViewListener(new RefreshScrollView.OnScrollViewListener() {
            public void onScrollChanged(RefreshScrollView v, int l, int t, int oldl, int oldt) {
                View view = (View) v.getChildAt(getChildCount() - 1);
                int diff = view.getBottom() - (v.getHeight() + v.getScrollY());
                if (diff == 0) {
                    int view_count = ll_search_question_list_left.getChildCount() + ll_search_question_list_right.getChildCount();
                    if (view_count >= element_count && semaphore) readMore();
                }
            }
        });

        new AddQuestionTask().execute(null, null, null);
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

                                iqv.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new QuestionFragmentView(con,((SearchQuestionView)v).tcjo));
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