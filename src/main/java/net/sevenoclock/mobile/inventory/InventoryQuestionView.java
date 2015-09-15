package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class InventoryQuestionView extends LinearLayout {

    public InventoryQuestionView(Context context, TryCatchJO jo) {
        super(context);
        try{
            Values valeus = (Values)context.getApplicationContext();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, valeus.book_height * 2 - 100);
            lp.setMargins(7, 7, 7, 7);
            setLayoutParams(lp);
            setOrientation(VERTICAL);

            ImageView iv_inventory_question_list_question_img = new ImageView(context);

            valeus.aq.id(iv_inventory_question_list_question_img).image(Functions.DOMAIN + jo.get("src", ""));
            iv_inventory_question_list_question_img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            iv_inventory_question_list_question_img.setScaleType(ImageView.ScaleType.FIT_START);
            iv_inventory_question_list_question_img.setBackgroundResource(R.drawable.ll_inventory_question_list_question);

            addView(iv_inventory_question_list_question_img);
        }catch (Exception e){
            Log.i("SearchQuestionError", e.getMessage());
        }
    }
}