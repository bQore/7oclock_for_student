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

public class TestpaperAnswerResultQuickView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_info;

    public int qid = 0;
    public String answer = "";

    FontTextView tv_testpaper_answer_result_quick_index;
    FontTextView tv_testpaper_answer_result_quick_answer;

    Values values;

    public TestpaperAnswerResultQuickView(Context context, int index, TryCatchJO jo) {
        super(context);
        this.con = context;
        this.tcjo_info = jo;

        qid = tcjo_info.get("id",0);
        answer = tcjo_info.get("answer_mobile","");

        values = (Values) context.getApplicationContext();
        inflate(getContext(),R.layout.view_testpaper_answer_result_form, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_testpaper_answer_result_quick_index = (FontTextView)findViewById(R.id.tv_testpaper_answer_result_quick_index);

        tv_testpaper_answer_result_quick_answer = (FontTextView)findViewById(R.id.tv_testpaper_answer_result_quick_answer);

        if(qid != 0){
            tv_testpaper_answer_result_quick_index.setText("" + (index + 1));
            tv_testpaper_answer_result_quick_answer.setText(answer);
        }
    }
}