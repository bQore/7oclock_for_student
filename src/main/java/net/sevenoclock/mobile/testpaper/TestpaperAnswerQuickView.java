package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestpaperAnswerQuickView extends LinearLayout {

    private Context con;
    private TryCatchJO tcjo_info;

    public static LinearLayout ll_testpaper_answer_quick_btns;
    private LinearLayout ll_testpaper_answer_quick_list;
    private Button btn_testpaper_answer_quick_submit;

    private Button btn_testpaper_answer_quick_key0;
    private Button btn_testpaper_answer_quick_key1;

    public static EditText et_focused = null;

    TestpaperAnswerFormView[] tafv;
    TestpaperResultQuickView[] trqv;

    Values values;

    public TestpaperAnswerQuickView(Context context, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_testpaper_answer_quick, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ll_testpaper_answer_quick_btns = (LinearLayout)findViewById(R.id.ll_testpaper_answer_quick_btns);
        ll_testpaper_answer_quick_list = (LinearLayout)findViewById(R.id.ll_testpaper_answer_quick_list);
        btn_testpaper_answer_quick_submit = (Button)findViewById(R.id.btn_testpaper_answer_quick_submit);

        btn_testpaper_answer_quick_key0 = (Button)findViewById(R.id.btn_testpaper_answer_quick_key0);
        btn_testpaper_answer_quick_key1 = (Button)findViewById(R.id.btn_testpaper_answer_quick_key1);

        setTag(R.string.tag_main_title, "");
        setTag(R.string.tag_main_subtitle, tcjo_info.get("title", ""));

        new TestpaperSubmitTask().execute(null, null, null);

        btn_testpaper_answer_quick_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                try {
                    JSONObject jo_answer = new JSONObject();
                    int len = tafv.length;

                    for (int i = 0; i < len; i++){
                        if(tafv[i].answer.equals("")) return;
                        jo_answer.put("" + tafv[i].qid, tafv[i].answer);
                    }

                    String url = Functions.DOMAIN + "/mobile/?mode=set_testpaper_submit&uid=" + values.user_id
                            + "&tpid=" + tcjo_info.get("id", "");

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("answer", jo_answer.toString());

                    values.aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            if (status.getMessage().equals("OK")) {
                                reflesh();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_testpaper_answer_quick_key0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_focused != null) et_focused.append(btn_testpaper_answer_quick_key0.getText());
            }
        });

        btn_testpaper_answer_quick_key1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_focused != null) et_focused.append(btn_testpaper_answer_quick_key1.getText());
            }
        });
    }

    public void reflesh(){
        if(ll_testpaper_answer_quick_list.getChildCount() > 0) ll_testpaper_answer_quick_list.removeAllViews();
        new TestpaperSubmitTask().execute(null, null, null);
    }

    class TestpaperSubmitTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja_submit;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            btn_testpaper_answer_quick_submit.setVisibility(View.GONE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja_submit = Functions.GET("get_testpaper_submit&uid="+values.user_id+"&tpid=" + tcjo_info.get("id", ""));
            if(ja_submit == null) return false;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                if(ja_submit.length() < 1){
                    new TestpaperQuestionTask().execute(null, null, null);
                    return;
                }
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trqv = new TestpaperResultQuickView[ja_submit.length()];
                        for (int i = 0; i < ja_submit.length(); i++) {
                            try{
                                TryCatchJO tcjo_question = new TryCatchJO(ja_submit.getJSONObject(i));
                                trqv[i] = new TestpaperResultQuickView(con,i,tcjo_question);
                                ll_testpaper_answer_quick_list.addView(trqv[i]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        MainActivity.setTitle("채점결과");
                    }
                });
            }else{
                Toast.makeText(con, "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            MainActivity.ll_main_main_loading.setVisibility(View.GONE);
            return;
        }
    }

    class TestpaperQuestionTask extends AsyncTask<Void, Void, Boolean> {
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
                                ll_testpaper_answer_quick_list.addView(tafv[i]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        btn_testpaper_answer_quick_submit.setVisibility(View.VISIBLE);
                        MainActivity.setTitle("빠른 답안 입력");
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