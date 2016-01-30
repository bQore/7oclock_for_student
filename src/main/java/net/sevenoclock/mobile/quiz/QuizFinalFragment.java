package net.sevenoclock.mobile.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.question.QuestionPagerFragment;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizFinalFragment extends Fragment {

    private Context con;
    private Values values;

    private TryCatchJO tcjo;
    private int tpid = 0;

    private ListView lv_quiz_final_list;
    private Button btn_quiz_input_quick_submit;

    public QuizFinalListAdapter qfla;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values)container.getContext().getApplicationContext();

        tpid = getArguments().getInt("tpid",-1);
        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("tcjo")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_quiz_final, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lv_quiz_final_list = (ListView) view.findViewById(R.id.lv_quiz_final_list);

        btn_quiz_input_quick_submit = new Button(con);
        btn_quiz_input_quick_submit.setText("제출하기");
        btn_quiz_input_quick_submit.setTextColor(Color.WHITE);
        btn_quiz_input_quick_submit.setBackgroundColor(Color.parseColor("#2ecc71"));
        btn_quiz_input_quick_submit.setTextSize(16);
        btn_quiz_input_quick_submit.setVisibility(View.GONE);
        AbsListView.LayoutParams layout_527 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT);
        btn_quiz_input_quick_submit.setLayoutParams(layout_527);
        btn_quiz_input_quick_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                try {
                    final JSONObject jo_answer = new JSONObject();
                    int len = qfla.getCount();

                    for (int i = 0; i < len; i++) {
                        String answer = qfla.getItem(i)[1];
                        String qid = qfla.getItem(i)[2];
                        if (answer.startsWith(",")) answer = answer.substring(1);
                        Log.i(qid, answer);
                        jo_answer.put(qid, answer);
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
                                    Log.i("@@info","tpid:"+tpid+" obj:"+tcjo.toString());
                                    if(tpid == -1){
                                        Functions.history_back_delete(con);
                                        Functions.history_go(con, new QuestionPagerFragment().newInstance(tcjo));
                                    }else {
                                        String url = Functions.DOMAIN + "/mobile/?mode=set_testpaper_submit&uid=" + values.user_id
                                                + "&tpid=" + tpid;

                                        Map<String, Object> params = new HashMap<String, Object>();
                                        params.put("answer", jo_answer.toString());

                                        values.aq.ajax(url, params, String.class, new AjaxCallback<String>() {
                                            @Override
                                            public void callback(String url, String html, AjaxStatus status) {
                                                Log.i("@", status.getMessage());
                                                if (status.getMessage().equals("OK")) {
                                                    Functions.history_back_delete(con);
                                                    Functions.history_go(con, new QuizInputQuickFragment().newInstance(tcjo));
                                                }else{
                                                    Functions.history_back_delete(con);
                                                    Functions.history_go(con, new QuestionPagerFragment().newInstance(tcjo));
                                                }
                                            }
                                        });
                                        return;
                                    }
                                }
                            }).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        lv_quiz_final_list.addFooterView(btn_quiz_input_quick_submit);
        btn_quiz_input_quick_submit.setVisibility(View.VISIBLE);

        qfla = new QuizFinalListAdapter();
        lv_quiz_final_list.setAdapter(qfla);

        return view;
    }

    public static QuizFinalFragment newInstance(int tpid, String tcjo) {
        QuizFinalFragment view = new QuizFinalFragment();
        Bundle args = new Bundle();
        args.putInt("tpid", tpid);
        args.putString("tcjo",tcjo);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
