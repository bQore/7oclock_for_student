package net.sevenoclock.mobile.testpaper;

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
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.io.File;

public class TestpaperQuestionView extends LinearLayout {

    public TryCatchJO tcjo;

    public TestpaperQuestionView(final Context context, int index, TryCatchJO jo) {
        super(context);
        try{
            tcjo = jo;
            final Values values = (Values)context.getApplicationContext();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, values.book_height * 2 - 100);
            lp.setMargins(7, 7, 7, 7);
            setLayoutParams(lp);
            setOrientation(VERTICAL);

            FontTextView tv_testpaper_question_list_question_number = new FontTextView(context);
            final ImageView iv_testpaper_question_list_question_img = new ImageView(context);

            tv_testpaper_question_list_question_number.setText((index + 1) + ".");
            tv_testpaper_question_list_question_number.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            iv_testpaper_question_list_question_img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            iv_testpaper_question_list_question_img.setScaleType(ImageView.ScaleType.FIT_START);
            iv_testpaper_question_list_question_img.setBackgroundResource(R.drawable.ll_testpaper_question_list_question);

            values.aq.id(iv_testpaper_question_list_question_img).image(Functions.DOMAIN + tcjo.get("src", ""));

            addView(tv_testpaper_question_list_question_number);
            addView(iv_testpaper_question_list_question_img);
        }catch (Exception e){
            Log.i("TestpaperError", ""+e.getMessage());
        }
    }
}