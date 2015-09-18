package net.sevenoclock.mobile.testpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class TestpaperAnswerQuickInsertView extends LinearLayout {

    private Context con;
    private TryCatchJO tcjo_info;

    private LinearLayout ll_testpaper_answer_quick_insert_list;
    private Button btn_testpaper_answer_quick_insert_submit;

    TestpaperAnswerFormView[] tafv;

    Values values;

    public TestpaperAnswerQuickInsertView(Context context, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_testpaper_answer_quick_insert, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ll_testpaper_answer_quick_insert_list = (LinearLayout)findViewById(R.id.ll_testpaper_answer_quick_insert_list);
        btn_testpaper_answer_quick_insert_submit = (Button)findViewById(R.id.btn_testpaper_answer_quick_insert_submit);

        setTag(R.string.tag_main_title, "빠른 답안 입력");
        setTag(R.string.tag_main_subtitle, tcjo_info.get("title", ""));

        new AddQuestionTask().execute(null, null, null);

        btn_testpaper_answer_quick_insert_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                String answer = "";
                int len = tafv.length;
                for(int i=0; i<len; i++) answer += tafv[i].answer+",";
                Log.i("@@@@@@@@", answer);
            }
        });
    }

    class AddQuestionTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_question;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_question = Functions.GET("get_testpaper_question_list&tpid=" + tcjo_info.get("id", ""));
            if(ja_question == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tafv = new TestpaperAnswerFormView[ja_question.length()];
                        for (int i = 0; i < ja_question.length(); i++) {
                            try{
                                TryCatchJO tcjo_question = new TryCatchJO(ja_question.getJSONObject(i));
                                tafv[i] = new TestpaperAnswerFormView(con,i,tcjo_question);
                                ll_testpaper_answer_quick_insert_list.addView(tafv[i]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
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