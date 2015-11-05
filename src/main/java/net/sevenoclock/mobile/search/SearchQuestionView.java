package net.sevenoclock.mobile.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.io.File;

public class SearchQuestionView extends LinearLayout {

    TryCatchJO tcjo;

    public SearchQuestionView(Context context, TryCatchJO jo) {
        super(context);
        try{
            tcjo = jo;
            final Values valeus = (Values)context.getApplicationContext();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, valeus.book_height * 2 - 100);
            lp.setMargins(7, 7, 7, 7);
            setLayoutParams(lp);
            setOrientation(VERTICAL);

            final ImageView iv_search_question_list_question_img = new ImageView(context);

            iv_search_question_list_question_img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            iv_search_question_list_question_img.setScaleType(ImageView.ScaleType.FIT_START);
            iv_search_question_list_question_img.setBackgroundResource(R.drawable.ll_search_question_list_question);

            valeus.aq.id(iv_search_question_list_question_img).image(Functions.DOMAIN + jo.get("src", ""));

            addView(iv_search_question_list_question_img);
        }catch (Exception e){
            Log.i("SearchQuestionError", ""+e.getMessage());
        }
    }
}