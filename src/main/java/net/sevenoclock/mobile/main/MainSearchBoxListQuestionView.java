package net.sevenoclock.mobile.main;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class MainSearchBoxListQuestionView extends LinearLayout {
    public MainSearchBoxListQuestionView(Context context, TryCatchJO jo) {
        super(context);
        try{
            Values valeus = (Values)context.getApplicationContext();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, valeus.book_height * 2 - 100);
            lp.setMargins(7, 7, 7, 7);
            setLayoutParams(lp);
            setOrientation(VERTICAL);

            ImageView iv_main_search_box_list_question_img = new ImageView(context);

            valeus.aq.id(iv_main_search_box_list_question_img).image(Functions.DOMAIN + jo.get("src", ""));
            iv_main_search_box_list_question_img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            iv_main_search_box_list_question_img.setScaleType(ImageView.ScaleType.FIT_START);
            iv_main_search_box_list_question_img.setBackgroundResource(R.drawable.ll_main_search_box_list_question);

            addView(iv_main_search_box_list_question_img);
        }catch (Exception e){
            Log.i("TestpaperError", e.getMessage());
        }
    }
}