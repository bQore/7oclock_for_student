package net.sevenoclock.mobile.quiz;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

public class QuizInputQuickFormView extends LinearLayout {

    private Context con;
    private TryCatchJO tcjo_info;

    public int qid = 0;
    public int items = 0;
    public int answer_len = 0;
    public String answer = "";

    FontTextView tv_quiz_input_form_index;
    LinearLayout ll_quiz_input_form_view;

    public EditText et_answer;

    Values values;

    public QuizInputQuickFormView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("id",0);
        items = tcjo_info.get("items",1);
        String answer_mobile = tcjo_info.get("answer_mobile","");
        answer_len = answer_mobile.length();

        values = (Values) context.getApplicationContext();
        inflate(getContext(), R.layout.view_quiz_input_form, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_quiz_input_form_index = (FontTextView)findViewById(R.id.tv_quiz_input_form_index);
        ll_quiz_input_form_view = (LinearLayout)findViewById(R.id.ll_quiz_input_form_view);

        if(qid != 0){
            tv_quiz_input_form_index.setText("" + (index + 1));
            if(items == 1) setSingular();
            else  setPlural();
        }
    }

    void setSingular(){
        et_answer = new EditText(con);
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 5, 0, 5);
        et_answer.setLayoutParams(lp);
        et_answer.setSingleLine();
        et_answer.setHint("ex) 답");
        et_answer.setBackgroundColor(Color.WHITE);
        ll_quiz_input_form_view.addView(et_answer);

        et_answer.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    QuizInputQuickFragment.et_focused = et_answer;
                    QuizInputQuickFragment.ll_quiz_input_quick_btns.setVisibility(View.VISIBLE);
                } else {
                    QuizInputQuickFragment.et_focused = null;
                    QuizInputQuickFragment.ll_quiz_input_quick_btns.setVisibility(View.GONE);
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

    void setPlural(){
        final Button[] btn = new Button[items];
        for(int i = 0; i<items; i++){
            btn[i] = new Button(con);
            LinearLayout.LayoutParams btn_lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            btn_lp.weight = 1;
            btn_lp.setMargins(0,0,5,0);
            btn[i].setLayoutParams(btn_lp);
            btn[i].setTextColor(Color.parseColor("#666666"));
            btn[i].setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
            btn[i].setText("" + (i + 1));
            btn[i].setTag(false);
            ll_quiz_input_form_view.addView(btn[i]);

            btn[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button this_btn = ((Button)v);
                    if(answer_len < 2){
                        for(int i=0; i<items; i++){
                            btn[i].setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
                            btn[i].setTextColor(Color.parseColor("#666666"));
                        }
                        this_btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns_clicked);
                        this_btn.setTextColor(Color.parseColor("#c0392b"));
                        answer = this_btn.getText().toString();
                    }else{
                        if(this_btn.getTag().equals(false)){
                            this_btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns_clicked);
                            this_btn.setTextColor(Color.parseColor("#c0392b"));
                            answer += ","+this_btn.getText().toString();
                            this_btn.setTag(true);
                        }else{
                            this_btn.setBackgroundResource(R.drawable.btn_quiz_text_input_btns);
                            this_btn.setTextColor(Color.parseColor("#666666"));
                            answer = answer.replace(this_btn.getText().toString(),"");
                            this_btn.setTag(false);
                        }
                    }

                }
            });
        }
    }
}