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

public class TestpaperResultQuickView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    public int qid = 0;
    public String answer_user = "";
    public String answer_correct = "";

    FontTextView tv_testpaper_result_quick_index;
    IconTextView tv_testpaper_result_quick_icon;

    FontTextView tv_testpaper_result_quick_o;
    FontTextView tv_testpaper_result_quick_x;

    Values values;

    public TestpaperResultQuickView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("tps_id",0);
        answer_user = tcjo_info.get("answer_user","");
        answer_correct = tcjo_info.get("answer_correct","");

        values = (Values) context.getApplicationContext();
        inflate(getContext(), R.layout.view_testpaper_result_form, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_testpaper_result_quick_index = (FontTextView)findViewById(R.id.tv_testpaper_result_quick_index);
        tv_testpaper_result_quick_icon = (IconTextView)findViewById(R.id.tv_testpaper_result_quick_icon);

        tv_testpaper_result_quick_o = (FontTextView)findViewById(R.id.tv_testpaper_result_quick_o);
        tv_testpaper_result_quick_x = (FontTextView)findViewById(R.id.tv_testpaper_result_quick_x);

        if(qid != 0){
            tv_testpaper_result_quick_index.setText("" + (index + 1));
            setAnswer(answer_correct.equals(answer_user));
        }
    }

    void setAnswer(boolean b){
        if(b){
            tv_testpaper_result_quick_icon.setText(R.string.ic_testpaper_result_quick_o);
            tv_testpaper_result_quick_icon.setTextColor(Color.parseColor("#2ecc71"));
            tv_testpaper_result_quick_x.setVisibility(View.GONE);
        }else{
            tv_testpaper_result_quick_icon.setText(R.string.ic_testpaper_result_quick_x);
            tv_testpaper_result_quick_icon.setTextColor(Color.parseColor("#e74c3c"));
            tv_testpaper_result_quick_x.setPaintFlags(tv_testpaper_result_quick_x.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_testpaper_result_quick_x.setText(answer_user);
        }
        tv_testpaper_result_quick_o.setText(answer_correct);
    }
}