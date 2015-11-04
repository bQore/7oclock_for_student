package net.sevenoclock.mobile.testpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestpaperInputQuickFragment extends Fragment {

    private Context con;
    private TryCatchJO tcjo_info;

    public static LinearLayout ll_testpaper_input_quick_btns;
    public LinearLayout ll_testpaper_input_quick_btns_inner;
    private ListView lv_testpaper_input_quick_list;
    private LinearLayout ll_testpaper_input_quick_list;
    private Button btn_testpaper_input_quick_submit;

    private TestpaperInputResultQuickAdapter trqa;

    public static EditText et_focused = null;

    TestpaperInputFormView[] tafv;

    private InputMethodManager imm;
    Values values;

    public static TestpaperInputQuickFragment newInstance(TryCatchJO tcjo) {
        TestpaperInputQuickFragment view = new TestpaperInputQuickFragment();
        Bundle args = new Bundle();
        args.putString("object", tcjo.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        try {
            tcjo_info = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_testpaper_input_quick, container, false);

        MainActivity.setTitle("");
        MainActivity.setSubtitle(tcjo_info.get("title", ""));

        ll_testpaper_input_quick_btns = (LinearLayout) v.findViewById(R.id.ll_testpaper_input_quick_btns);
        ll_testpaper_input_quick_btns_inner = (LinearLayout) v.findViewById(R.id.ll_testpaper_input_quick_btns_inner);
        lv_testpaper_input_quick_list = (ListView) v.findViewById(R.id.lv_testpaper_input_quick_list);
        ll_testpaper_input_quick_list = (LinearLayout) v.findViewById(R.id.ll_testpaper_input_quick_list);

        btn_testpaper_input_quick_submit = new Button(con);
        btn_testpaper_input_quick_submit.setText("제출하기");
        btn_testpaper_input_quick_submit.setTextColor(Color.WHITE);
        btn_testpaper_input_quick_submit.setBackgroundColor(Color.parseColor("#2ecc71"));
        btn_testpaper_input_quick_submit.setTextSize(16);
        btn_testpaper_input_quick_submit.setVisibility(View.GONE);
        LinearLayout.LayoutParams layout_527 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_527.bottomMargin = 10;
        layout_527.weight = 0;
        btn_testpaper_input_quick_submit.setLayoutParams(layout_527);

        trqa = new TestpaperInputResultQuickAdapter();
        lv_testpaper_input_quick_list.setAdapter(trqa);

        imm = (InputMethodManager)con.getSystemService(Context.INPUT_METHOD_SERVICE);

        reflesh();

        btn_testpaper_input_quick_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                try {
                    final JSONObject jo_answer = new JSONObject();
                    int len = tafv.length;

                    for (int i = 0; i < len; i++) {
                        if (tafv[i].answer.equals("")) tafv[i].answer = "-";
                        if (tafv[i].answer.startsWith(",")) tafv[i].answer = tafv[i].answer.substring(1);
                        jo_answer.put("" + tafv[i].qid, tafv[i].answer);
                    }

                    new AlertDialog.Builder(con).setTitle("답안제출")
                            .setMessage("답안제출 후 수정이 불가능합니다.\n답안을 제출하시겠습니까?")
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = Functions.DOMAIN + "/mobile/?mode=set_testpaper_submit&uid=" + values.user_id
                                            + "&tpid=" + tcjo_info.get("id", "");

                                    Map<String, Object> params = new HashMap<String, Object>();
                                    params.put("answer", jo_answer.toString());

                                    for(int i=0; i<tafv.length;i++){
                                        if(tafv[i].et_answer != null)
                                            imm.hideSoftInputFromWindow(tafv[i].et_answer.getWindowToken(), 0);
                                    }

                                    values.aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject json, AjaxStatus status) {
                                            if (status.getMessage().equals("OK")) {
                                                reflesh();
                                            }
                                        }
                                    });
                                    return;
                                }
                            }).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ll_testpaper_input_quick_btns_inner.addView(NewBtn("√"));
        ll_testpaper_input_quick_btns_inner.addView(NewBtn("π"));
        ll_testpaper_input_quick_btns_inner.addView(NewBtn("/"));
        ll_testpaper_input_quick_btns_inner.addView(NewBtn("."));
        ll_testpaper_input_quick_btns_inner.addView(NewBtn("+"));
        ll_testpaper_input_quick_btns_inner.addView(NewBtn("-"));

        return v;
    }

    private Button NewBtn(String str){
        Button btn = new Button(con);
        btn.setText(str);
        btn.setBackgroundResource(R.drawable.btn_testpaper_input_btns);
        LinearLayout.LayoutParams layout_113 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_113.rightMargin = 5;
        btn.setLayoutParams(layout_113);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_focused != null) et_focused.append(((Button) v).getText());
            }
        });
        return btn;
    }

    public void reflesh(){
        trqa.reflesh();
        if(ll_testpaper_input_quick_list.getChildCount() > 0) ll_testpaper_input_quick_list.removeAllViews();
        new TestpaperSubmitTask().execute(null, null, null);
    }

    class TestpaperSubmitTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray ja;
        JSONArray ja_submit;

        @Override
        protected void onPreExecute() {
            MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
            btn_testpaper_input_quick_submit.setVisibility(View.GONE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... Void) {
            ja = Functions.GET("get_testpaper_submit&uid="+values.user_id+"&tpid=" + tcjo_info.get("id", ""));
            if(ja == null) return false;
            try {
                ja_submit = ja.getJSONObject(0).getJSONArray("submits");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                lv_testpaper_input_quick_list.setVisibility(View.VISIBLE);
                ll_testpaper_input_quick_list.setVisibility(View.GONE);
                if(ja_submit.length() < 1) {
                    new TestpaperQuestionTask().execute(null, null, null);
                    return;
                }
                lv_testpaper_input_quick_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        try {
                            Functions.history_go(con, new QuestionPagerFragment().newInstance(new TryCatchJO(ja_submit.getJSONObject(position))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < ja_submit.length(); i++) {
                            try {
                                trqa.add(new TryCatchJO(ja_submit.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        MainActivity.setTitle("채점결과");
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                });
            }else{
                new TestpaperQuestionTask().execute(null, null, null);
            }
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
                lv_testpaper_input_quick_list.setVisibility(View.GONE);
                ll_testpaper_input_quick_list.setVisibility(View.VISIBLE);
                MainActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tafv = new TestpaperInputFormView[ja_question.length()];
                        for (int i = 0; i < ja_question.length(); i++) {
                            try{
                                TryCatchJO tcjo_question = new TryCatchJO(ja_question.getJSONObject(i));
                                tafv[i] = new TestpaperInputFormView(con,i,tcjo_question);
                                ll_testpaper_input_quick_list.addView(tafv[i]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        ll_testpaper_input_quick_list.addView(btn_testpaper_input_quick_submit);
                        btn_testpaper_input_quick_submit.setVisibility(View.VISIBLE);
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

    @Override
    public void onStop() {
        super.onStop();
        for(int i=0; i<tafv.length;i++){
            if(tafv[i].et_answer != null)
                imm.hideSoftInputFromWindow(tafv[i].et_answer.getWindowToken(), 0);
        }
    }

}