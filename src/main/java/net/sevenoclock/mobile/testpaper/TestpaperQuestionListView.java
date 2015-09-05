package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.RefreshScrollView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperQuestionListView extends LinearLayout {

    private Context con;
    private int tpid_id;

    private LinearLayout ll_testpaper_question_list_left;
    private LinearLayout ll_testpaper_question_list_right;
    private RefreshScrollView sv_testpaper_question_list_scrollview;

    private int element_count = 0;
    private boolean semaphore = true;

    Values values;

    public TestpaperQuestionListView(Context context, int tpid_id) {
        super(context);
        this.con = context;
        this.tpid_id = tpid_id;

        values = (Values) context.getApplicationContext();

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_testpaper_question_list, null), lp);

        ll_testpaper_question_list_left = (LinearLayout)findViewById(R.id.ll_testpaper_question_list_left);
        ll_testpaper_question_list_right = (LinearLayout)findViewById(R.id.ll_testpaper_question_list_right);
        sv_testpaper_question_list_scrollview = (RefreshScrollView) findViewById(R.id.sv_testpaper_question_list_scrollview);

        sv_testpaper_question_list_scrollview.setOnScrollViewListener( new RefreshScrollView.OnScrollViewListener() {
            public void onScrollChanged( RefreshScrollView v, int l, int t, int oldl, int oldt ) {
                View view = (View) v.getChildAt(getChildCount() - 1);
                int diff = view.getBottom()-(v.getHeight()+v.getScrollY());
                if( diff == 0 )
                {
                    int view_count = ll_testpaper_question_list_left.getChildCount()+ll_testpaper_question_list_right.getChildCount();
                    if(view_count >= element_count && semaphore) readMore();
                }
            }
        } );

        new AddQuestionTask().execute(null, null, null);
    }

    public void reflesh(){
        ll_testpaper_question_list_left.removeAllViews();
        ll_testpaper_question_list_right.removeAllViews();
        element_count = 0;
        new AddQuestionTask().execute(null, null, null);
    }

    public void readMore(){
        new AddQuestionTask().execute(null, null, null);
    }

    class AddQuestionTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_question;

        @Override
        protected void onPreExecute() {
            semaphore = false;
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET("get_testpaper_question_list&tpid=" + tpid_id + "&limit="+element_count+":"+(element_count+10));
            if(ja_question == null) return false;
            element_count += 10;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        for (i = 0; i < ja_question.length(); i++) {
                            TestpaperQuestionView tqv = null;
                            try {
                                tqv = new TestpaperQuestionView(con, element_count+i-10, new TryCatchJO(ja_question.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int count_left = ll_testpaper_question_list_left.getChildCount();
                            int count_right = ll_testpaper_question_list_right.getChildCount();

                            if (count_left > count_right) ll_testpaper_question_list_right.addView(tqv);
                            else ll_testpaper_question_list_left.addView(tqv);
                        }
                    }
                }, 0);
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            semaphore = true;
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}