package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

public class TestpaperInputResultQuickView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    public int qid = 0;
    public String answer_user = "";
    public String answer_correct = "";

    FontTextView tv_testpaper_input_result_quick_index;
    IconTextView tv_testpaper_input_result_quick_icon;

    FontTextView tv_testpaper_input_result_quick_o;
    FontTextView tv_testpaper_input_result_quick_x;

    Values values;

    public TestpaperInputResultQuickView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("tps_id",0);
        answer_user = tcjo_info.get("answer_user","");
        answer_correct = tcjo_info.get("answer_correct","");

        values = (Values) context.getApplicationContext();
        inflate(getContext(),R.layout.view_testpaper_input_result_form, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_testpaper_input_result_quick_index = (FontTextView)findViewById(R.id.tv_testpaper_input_result_quick_index);
        tv_testpaper_input_result_quick_icon = (IconTextView)findViewById(R.id.tv_testpaper_input_result_quick_icon);

        tv_testpaper_input_result_quick_o = (FontTextView)findViewById(R.id.tv_testpaper_input_result_quick_o);
        tv_testpaper_input_result_quick_x = (FontTextView)findViewById(R.id.tv_testpaper_input_result_quick_x);

        if(qid != 0){
            tv_testpaper_input_result_quick_index.setText("" + (index + 1));
            setAnswer(answer_correct.equals(answer_user));
        }
    }

    void setAnswer(boolean b){
        if(b){
            tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_o);
            tv_testpaper_input_result_quick_icon.setTextColor(Color.parseColor("#2ecc71"));
            tv_testpaper_input_result_quick_x.setVisibility(View.GONE);
        }else{
            tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_x);
            tv_testpaper_input_result_quick_icon.setTextColor(Color.parseColor("#e74c3c"));
            tv_testpaper_input_result_quick_x.setPaintFlags(tv_testpaper_input_result_quick_x.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_testpaper_input_result_quick_x.setText(answer_user);
        }
        tv_testpaper_input_result_quick_o.setText(answer_correct);
    }
}

/*
package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Values;

public class TestpaperInputResultQuickView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    public int qid = 0;
    public String answer_user = "";
    public String answer_correct = "";

    FontTextView tv_testpaper_input_result_quick_index;
    IconTextView tv_testpaper_input_result_quick_icon;

    FontTextView tv_testpaper_input_result_quick_o;
    FontTextView tv_testpaper_input_result_quick_x;

    public TestpaperInputResultQuickView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("tps_id",0);
        answer_user = tcjo_info.get("answer_user","");
        answer_correct = tcjo_info.get("answer_correct","");

        int den = (int)(1*con.getResources().getDisplayMetrics().scaledDensity);

        setBackgroundResource(R.drawable.ll_testpaper_answer_result_quick_line);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);

        FrameLayout fl_index = new FrameLayout(con);
        LayoutParams layout_776 = new LayoutParams(50*den,50*den);
        layout_776.weight = 0;
        fl_index.setLayoutParams(layout_776);

        tv_testpaper_input_result_quick_index = new FontTextView(con);
        tv_testpaper_input_result_quick_index.setText("1");
        tv_testpaper_input_result_quick_index.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        LayoutParams layout_721 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        tv_testpaper_input_result_quick_index.setLayoutParams(layout_721);
        fl_index.addView(tv_testpaper_input_result_quick_index);

        tv_testpaper_input_result_quick_icon = new IconTextView(con);
        tv_testpaper_input_result_quick_icon.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_o);
        tv_testpaper_input_result_quick_icon.setTextSize(40);
        LayoutParams layout_84 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        tv_testpaper_input_result_quick_icon.setLayoutParams(layout_84);
        fl_index.addView(tv_testpaper_input_result_quick_icon);

        addView(fl_index);

        LinearLayout linearLayout_780 = new LinearLayout(con);
        linearLayout_780.setBackgroundColor(Color.parseColor("#DDDDDD"));
        LayoutParams layout_780 = new LayoutParams(1*den,LayoutParams.MATCH_PARENT);
        layout_780.weight = 0;
        linearLayout_780.setLayoutParams(layout_780);
        addView(linearLayout_780);

        LinearLayout linearLayout_367 = new LinearLayout(con);
        linearLayout_367.setOrientation(HORIZONTAL);
        linearLayout_367.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        LayoutParams layout_157 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        layout_157.weight = 1;
        linearLayout_367.setLayoutParams(layout_157);

        tv_testpaper_input_result_quick_x = new FontTextView(con);
        tv_testpaper_input_result_quick_x.setText("New Text");
        tv_testpaper_input_result_quick_x.setTextColor(Color.parseColor("#e74c3c"));
        tv_testpaper_input_result_quick_x.setTextSize(16);
        tv_testpaper_input_result_quick_x.setPaintFlags(tv_testpaper_input_result_quick_x.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        LayoutParams layout_515 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layout_515.rightMargin = 10*den;
        tv_testpaper_input_result_quick_x.setLayoutParams(layout_515);
        linearLayout_367.addView(tv_testpaper_input_result_quick_x);

        tv_testpaper_input_result_quick_o = new FontTextView(con);
        tv_testpaper_input_result_quick_o.setText("New Text");
        tv_testpaper_input_result_quick_o.setTextSize(16);
        tv_testpaper_input_result_quick_o.setTypeface(Typeface.DEFAULT_BOLD);
        LayoutParams layout_163 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tv_testpaper_input_result_quick_o.setLayoutParams(layout_163);
        linearLayout_367.addView(tv_testpaper_input_result_quick_o);

        addView(linearLayout_367);

        if(qid != 0){
            tv_testpaper_input_result_quick_index.setText("" + (index + 1));
            setAnswer(answer_correct.equals(answer_user));
        }
    }

    void setAnswer(boolean b){
        if(b){
            tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_o);
            tv_testpaper_input_result_quick_icon.setTextColor(Color.parseColor("#2ecc71"));
            tv_testpaper_input_result_quick_x.setVisibility(View.GONE);
        }else{
            tv_testpaper_input_result_quick_icon.setText(R.string.ic_testpaper_input_result_quick_x);
            tv_testpaper_input_result_quick_icon.setTextColor(Color.parseColor("#e74c3c"));
            tv_testpaper_input_result_quick_x.setText(answer_user);
        }
        tv_testpaper_input_result_quick_o.setText(answer_correct);
    }
}
 */