package net.sevenoclock.mobile.main;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;

public class MainSearchBoxListQuestionView extends LinearLayout {

    private AQuery aq;
    private Context con;

    private ImageView iv_main_search_box_list_question_img;

    public MainSearchBoxListQuestionView(Context context, TryCatchJO jo) {
        super(context);

        aq = new AQuery(context);
        con = context;

        setLayout();

        aq.id(iv_main_search_box_list_question_img).image(Functions.DOMAIN + jo.get("src", ""));
    }

    private void setLayout(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = con.getResources().getDisplayMetrics();

        int book_height = (int) (90 * displayMetrics.density);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, book_height*2-100);
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_main_search_box_list_question, null), lp);

        iv_main_search_box_list_question_img = (ImageView)findViewById(R.id.iv_main_search_box_list_question_img);
    }
}