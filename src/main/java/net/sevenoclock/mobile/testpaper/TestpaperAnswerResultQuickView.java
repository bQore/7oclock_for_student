package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

public class TestpaperAnswerResultQuickView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    public int qid = 0;
    public String answer = "";

    public TestpaperAnswerResultQuickView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("id",0);
        answer = tcjo_info.get("answer_mobile","");

        int den = (int)(1*con.getResources().getDisplayMetrics().scaledDensity);

        setBackgroundResource(R.drawable.ll_testpaper_answer_result_quick_line);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);

        FontTextView tv_testpaper_answer_result_quick_index = new FontTextView(con);
        tv_testpaper_answer_result_quick_index.setText("1");
        tv_testpaper_answer_result_quick_index.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        LayoutParams layout_703 = new LayoutParams(50*den,50*den);
        layout_703.weight = 0;
        tv_testpaper_answer_result_quick_index.setLayoutParams(layout_703);
        addView(tv_testpaper_answer_result_quick_index);

        LinearLayout linearLayout_780 = new LinearLayout(con);
        linearLayout_780.setBackgroundColor(Color.parseColor("#DDDDDD"));
        LayoutParams layout_780 = new LayoutParams(1*den,LayoutParams.MATCH_PARENT);
        layout_780.weight = 0;
        linearLayout_780.setLayoutParams(layout_780);
        addView(linearLayout_780);

        FontTextView tv_testpaper_answer_result_quick_answer = new FontTextView(con);
        tv_testpaper_answer_result_quick_answer.setText("New Text");
        tv_testpaper_answer_result_quick_answer.setTextSize(16);
        tv_testpaper_answer_result_quick_answer.setTypeface(Typeface.DEFAULT_BOLD);
        tv_testpaper_answer_result_quick_answer.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        LayoutParams layout_360 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        tv_testpaper_answer_result_quick_answer.setLayoutParams(layout_360);
        addView(tv_testpaper_answer_result_quick_answer);

        if(qid != 0){
            tv_testpaper_answer_result_quick_index.setText("" + (index + 1));
            tv_testpaper_answer_result_quick_answer.setText(answer);
        }
    }
}