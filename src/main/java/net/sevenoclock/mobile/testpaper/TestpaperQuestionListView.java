package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.RefreshScrollView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperQuestionListView extends LinearLayout {

    private Context con;
    private TryCatchJO tcjo;

    private FontTextView tv_testpaper_question_list_title;
    private FontTextView tv_testpaper_question_list_school;
    private FontTextView tv_testpaper_question_list_teacher;

    private LinearLayout ll_testpaper_question_list_left;
    private LinearLayout ll_testpaper_question_list_right;
    private RefreshScrollView sv_testpaper_question_list_scrollview;

    private int element_count = 0;
    private boolean semaphore = true;

    Values values;

    public TestpaperQuestionListView(Context context, TryCatchJO tcjo) {
        super(context);
        this.con = context;
        this.tcjo = tcjo;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_testpaper_question_list, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        tv_testpaper_question_list_title = (FontTextView)findViewById(R.id.tv_testpaper_question_list_title);
        tv_testpaper_question_list_school = (FontTextView)findViewById(R.id.tv_testpaper_question_list_school);
        tv_testpaper_question_list_teacher = (FontTextView)findViewById(R.id.tv_testpaper_question_list_teacher);

        ll_testpaper_question_list_left = (LinearLayout)findViewById(R.id.ll_testpaper_question_list_left);
        ll_testpaper_question_list_right = (LinearLayout)findViewById(R.id.ll_testpaper_question_list_right);
        sv_testpaper_question_list_scrollview = (RefreshScrollView)findViewById(R.id.sv_testpaper_question_list_scrollview);

        tv_testpaper_question_list_title.setText(tcjo.get("title", ""));
        tv_testpaper_question_list_school.setText(tcjo.get("school_name",""));
        tv_testpaper_question_list_teacher.setText(tcjo.get("user","")+" 선생님");

        setTag(R.string.tag_main_title, tcjo.get("title", ""));
        setTag(R.string.tag_main_subtitle, "총 0개 표시 중");

        sv_testpaper_question_list_scrollview.setOnScrollViewListener(new RefreshScrollView.OnScrollViewListener() {
            public void onScrollChanged(RefreshScrollView v, int l, int t, int oldl, int oldt) {
                View view = (View) v.getChildAt(getChildCount() - 1);
                int diff = view.getBottom() - (v.getHeight() + v.getScrollY());
                if (diff == 0) {
                    int view_count = ll_testpaper_question_list_left.getChildCount() + ll_testpaper_question_list_right.getChildCount();
                    if (view_count >= element_count && semaphore) readMore();
                }
            }
        } );

        new AddQuestionTask().execute(null, null, null);
    }

    public void reflesh(){
        ll_testpaper_question_list_left.removeAllViews();
        ll_testpaper_question_list_right.removeAllViews();
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
            ja_question = Functions.GET("get_testpaper_question_list&tpid=" + tcjo.get("id", "") + "&limit="+element_count+":"+(element_count+10));
            if(ja_question == null) return false;
            element_count += 10;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        for (i = 0; i < ja_question.length(); i++) {
                            TestpaperQuestionView tqv = null;
                            try {
                                TryCatchJO tcjo = new TryCatchJO(ja_question.getJSONObject(i));
                                tqv = new TestpaperQuestionView(con, element_count+i-10, tcjo);

                                tqv.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new QuestionFragmentView(con,((TestpaperQuestionView)v).tcjo));
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            count_left = ll_testpaper_question_list_left.getChildCount();
                            count_right = ll_testpaper_question_list_right.getChildCount();

                            if (count_left > count_right) ll_testpaper_question_list_right.addView(tqv);
                            else ll_testpaper_question_list_left.addView(tqv);
                        }
                        count_left = ll_testpaper_question_list_left.getChildCount();
                        count_right = ll_testpaper_question_list_right.getChildCount();
                        MainActivity.setSubtitle("총 " + (count_left+count_right) + "개 표시 중");
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            semaphore = true;
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}