package net.sevenoclock.mobile.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class QuizTextFragment extends Fragment {

    private Context con;
    private TryCatchJO tcjo;

    public int qid = 0;
    public int index = 0;
    public String str_url = "";
    public int items = 0;
    public int answer_len = 0;
    public String answer = "";

    private ImageView iv_quiz_text_img;
    private FontTextView tv_quize_text_input_index;
    private LinearLayout ll_quize_text_form_input_view;

    public EditText et_answer;

    private Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values)container.getContext().getApplicationContext();

        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("object")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_quiz_text, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_quiz_text_img = (ImageView)view.findViewById(R.id.iv_quiz_text_img);
        tv_quize_text_input_index = (FontTextView) view.findViewById(R.id.tv_quize_text_input_index);
        ll_quize_text_form_input_view = (LinearLayout) view.findViewById(R.id.ll_quize_text_form_input_view);

        MainActivity.ll_main_main_loading.setVisibility(View.GONE);

        qid = tcjo.get("id",0);
        index = getArguments().getInt("index");
        str_url = tcjo.get("src","");
        items = tcjo.get("items",0);
        String answer_mobile = tcjo.get("answer_mobile","");
        answer_len = answer_mobile.length();

        values.aq = new AQuery(getActivity(), view);

        try{
            values.aq.id(iv_quiz_text_img).image(Functions.DOMAIN + str_url);
        }catch (Exception e){
            Toast.makeText(container.getContext(), "이미지 로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }

        if(qid != 0){
            tv_quize_text_input_index.setText(""+index);
            if(items == 1) setSingular();
            else  setPlural();
        }

        Button btn_quiz_text_error = (Button)view.findViewById(R.id.btn_quiz_text_error);
        btn_quiz_text_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"eaeao@naver.com","storm0812@hanmail.net","tellme0218@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[모두를위한수학]오류신고합니다!");
                email.putExtra(Intent.EXTRA_TEXT, "제목 : "+qid+"번 문제 오류신고합니다!\n\n내용 : ");
                email.setType("text/plain");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                getActivity().startActivity(email);
            }
        });

        return view;
    }

    public static QuizTextFragment newInstance(int index, TryCatchJO tcjo) {
        QuizTextFragment view = new QuizTextFragment();
        Bundle args = new Bundle();
        args.putString("object", tcjo.toString());
        args.putInt("index", index);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    void setSingular(){
        et_answer = new EditText(con);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 5, 0, 5);
        et_answer.setLayoutParams(lp);
        et_answer.setSingleLine();
        et_answer.setHint("ex) 답");
        et_answer.setBackgroundColor(Color.WHITE);
        ll_quize_text_form_input_view.addView(et_answer);

        et_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    QuizPagerFragment.et_focused = et_answer;
                    QuizPagerFragment.ll_quiz_text_input_quick_btns.setVisibility(View.VISIBLE);
                } else {
                    QuizPagerFragment.et_focused = null;
                    QuizPagerFragment.ll_quiz_text_input_quick_btns.setVisibility(View.GONE);
                }

            }
        });

        et_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterLongEnough()) {
                    answer = et_answer.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            private boolean filterLongEnough() {
                return et_answer.getText().toString().length() > 0;
            }
        });
    }

    void setPlural() {
        final Button[] btn = new Button[items];
        for (int i = 0; i < items; i++) {
            btn[i] = new Button(con);
            LinearLayout.LayoutParams btn_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            btn_lp.weight = 1;
            btn_lp.setMargins(0, 0, 5, 0);
            btn[i].setLayoutParams(btn_lp);
            btn[i].setTextColor(Color.parseColor("#666666"));
            btn[i].setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
            btn[i].setText("" + (i + 1));
            btn[i].setTag(false);
            ll_quize_text_form_input_view.addView(btn[i]);

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button this_btn = ((Button) v);
                    if (answer_len < 2) {
                        for (int i = 0; i < items; i++) {
                            btn[i].setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
                            btn[i].setTextColor(Color.parseColor("#666666"));
                        }
                        this_btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns_clicked);
                        this_btn.setTextColor(Color.parseColor("#c0392b"));
                        answer = this_btn.getText().toString();
                    } else {
                        if (this_btn.getTag().equals(false)) {
                            this_btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns_clicked);
                            this_btn.setTextColor(Color.parseColor("#c0392b"));
                            answer += "," + this_btn.getText().toString();
                            this_btn.setTag(true);
                        } else {
                            this_btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
                            this_btn.setTextColor(Color.parseColor("#666666"));
                            answer = answer.replace(this_btn.getText().toString(), "");
                            this_btn.setTag(false);
                        }
                    }

                }
            });
        }
    }
}
