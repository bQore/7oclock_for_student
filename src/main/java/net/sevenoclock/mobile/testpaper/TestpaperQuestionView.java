package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;

public class TestpaperQuestionView extends LinearLayout {

    private AQuery aq;
    private Context con;

    private FontTextView tv_testpaper_question_list_question_number;
    private ImageView iv_testpaper_question_list_question_img;

    public TestpaperQuestionView(Context context, int index, TryCatchJO jo) {
        super(context);

        aq = new AQuery(context);
        con = context;

        setLayout();

        Bitmap image_bitmap = aq.getCachedImage(Functions.DOMAIN + jo.get("src", ""), 300);

        tv_testpaper_question_list_question_number.setText((index +1)+".");
        aq.id(iv_testpaper_question_list_question_img).image(image_bitmap);
    }

    private void setLayout(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = con.getResources().getDisplayMetrics();

        int book_height = (int) (90 * displayMetrics.density);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, book_height*2-80);
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_testpaper_question_list_question, null), lp);

        tv_testpaper_question_list_question_number = (FontTextView)findViewById(R.id.tv_testpaper_question_list_question_number);
        iv_testpaper_question_list_question_img = (ImageView)findViewById(R.id.iv_testpaper_question_list_question_img);
    }
}