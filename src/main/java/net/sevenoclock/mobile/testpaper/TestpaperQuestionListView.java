package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionFragmentView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperQuestionListView extends LinearLayout {

    private Context con;
    private int tpid_id;

    private FontTextView tv_testpaper_question_list_title;
    private FontTextView tv_testpaper_question_list_school;
    private FontTextView tv_testpaper_question_list_count;
    private FontTextView tv_testpaper_question_list_teacher;

    private LinearLayout ll_testpaper_question_list_left;
    private LinearLayout ll_testpaper_question_list_right;

    Values values;

    public TestpaperQuestionListView(Context context, int tpid_id, String title, String school_name, String user) {
        super(context);
        this.con = context;
        this.tpid_id = tpid_id;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_testpaper_question_list, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        tv_testpaper_question_list_title = (FontTextView)findViewById(R.id.tv_testpaper_question_list_title);
        tv_testpaper_question_list_school = (FontTextView)findViewById(R.id.tv_testpaper_question_list_school);
        tv_testpaper_question_list_count = (FontTextView)findViewById(R.id.tv_testpaper_question_list_count);
        tv_testpaper_question_list_teacher = (FontTextView)findViewById(R.id.tv_testpaper_question_list_teacher);

        ll_testpaper_question_list_left = (LinearLayout)findViewById(R.id.ll_testpaper_question_list_left);
        ll_testpaper_question_list_right = (LinearLayout)findViewById(R.id.ll_testpaper_question_list_right);

        tv_testpaper_question_list_title.setText(title);
        tv_testpaper_question_list_school.setText(school_name);
        tv_testpaper_question_list_teacher.setText(user+" 선생님");

        setTag(R.string.tag_main_title, title);
        setTag(R.string.tag_main_subtitle, "총 0개의 문제가 있습니다.");

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

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET("get_testpaper_question_list&tpid=" + tpid_id);
            if(ja_question == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_question.length(); i++) {
                            TestpaperQuestionView tqv = null;
                            try {
                                TryCatchJO tcjo = new TryCatchJO(ja_question.getJSONObject(i));
                                tqv = new TestpaperQuestionView(con, i, tcjo);
                                tqv.setTag(R.string.tag_testpaper_question_list_unit, tcjo.get("unit_title", "0"));
                                tqv.setTag(R.string.tag_testpaper_question_list_src, tcjo.get("src_url", ""));
                                tqv.setTag(R.string.tag_testpaper_question_list_explain, tcjo.get("explain_url", ""));
                                tqv.setTag(R.string.tag_testpaper_question_list_video, tcjo.get("video", ""));

                                tqv.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Functions.history_go(con, new QuestionFragmentView(con
                                                , v.getTag(R.string.tag_testpaper_question_list_unit).toString()
                                                , v.getTag(R.string.tag_testpaper_question_list_src).toString()
                                                , v.getTag(R.string.tag_testpaper_question_list_explain).toString()
                                                , v.getTag(R.string.tag_testpaper_question_list_video).toString()));
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int count_left = ll_testpaper_question_list_left.getChildCount();
                            int count_right = ll_testpaper_question_list_right.getChildCount();

                            if (count_left > count_right) ll_testpaper_question_list_right.addView(tqv);
                            else ll_testpaper_question_list_left.addView(tqv);
                        }
                        tv_testpaper_question_list_count.setText("총 " + ja_question.length() + "문제");
                        MainActivity.setSubtitle("총 "+ja_question.length()+"개의 문제가 있습니다.");
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }
}