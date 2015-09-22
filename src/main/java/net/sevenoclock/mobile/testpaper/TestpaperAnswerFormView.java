package net.sevenoclock.mobile.testpaper;

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

public class TestpaperAnswerFormView extends LinearLayout {

    private Context con;
    private TryCatchJO tcjo_info;

    public int qid = 0;
    public int items = 0;
    public String answer = "";

    FontTextView tv_testpaper_answer_form_index;
    LinearLayout ll_testpaper_answer_form_view;

    Values values;

    public TestpaperAnswerFormView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("id",0);
        items = tcjo_info.get("items",1);

        values = (Values) context.getApplicationContext();
        inflate(getContext(), R.layout.view_testpaper_answer_form, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_testpaper_answer_form_index = (FontTextView)findViewById(R.id.tv_testpaper_answer_form_index);
        ll_testpaper_answer_form_view = (LinearLayout)findViewById(R.id.ll_testpaper_answer_form_view);

        if(qid != 0){
            tv_testpaper_answer_form_index.setText("" + (index + 1));
            if(items == 1) setSingular();
            else  setPlural();
        }
    }

    void setSingular(){
        final EditText et_answer = new EditText(con);
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 5, 0, 5);
        et_answer.setLayoutParams(lp);
        et_answer.setSingleLine();
        et_answer.setHint("ex) 답");
        et_answer.setBackgroundColor(Color.WHITE);
        ll_testpaper_answer_form_view.addView(et_answer);

        et_answer.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TestpaperAnswerQuickView.et_focused = et_answer;
                    TestpaperAnswerQuickView.ll_testpaper_answer_quick_btns.setVisibility(View.VISIBLE);
                } else {
                    TestpaperAnswerQuickView.et_focused = null;
                    TestpaperAnswerQuickView.ll_testpaper_answer_quick_btns.setVisibility(View.GONE);
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
            btn[i].setBackgroundResource(R.drawable.btn_testpaper_answer_btns);
            btn[i].setText("" + (i + 1));
            ll_testpaper_answer_form_view.addView(btn[i]);

            btn[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i<items; i++){
                        btn[i].setBackgroundResource(R.drawable.btn_testpaper_answer_btns);
                        btn[i].setTextColor(Color.parseColor("#666666"));
                    }
                    ((Button)v).setBackgroundResource(R.drawable.btn_testpaper_answer_btns_clicked);
                    ((Button)v).setTextColor(Color.parseColor("#c0392b"));
                    answer = ((Button)v).getText().toString();
                }
            });
        }
    }
}