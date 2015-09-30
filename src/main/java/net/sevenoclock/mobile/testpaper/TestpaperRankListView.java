package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestpaperRankListView extends LinearLayout {

    private Context con;
    public TryCatchJO tcjo_rank;

    private FontTextView tv_testpaper_rank_list_index;
    private ImageView iv_testpaper_rank_list_pic;
    private ImageView iv_testpaper_rank_list_crown;
    private FontTextView tv_testpaper_rank_list_name;
    private FontTextView tv_testpaper_rank_list_score;

    Values values;

    public TestpaperRankListView(Context context, JSONObject jo, Boolean is_shown_score) {
        super(context);
        this.con = context;

        try {

            values = (Values) context.getApplicationContext();
            inflate(getContext(), R.layout.view_testpaper_rank_list, this);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            tv_testpaper_rank_list_index = (FontTextView) findViewById(R.id.tv_testpaper_rank_list_index);
            iv_testpaper_rank_list_pic = (ImageView) findViewById(R.id.iv_testpaper_rank_list_pic);
            iv_testpaper_rank_list_crown = (ImageView) findViewById(R.id.iv_testpaper_rank_list_crown);
            tv_testpaper_rank_list_name = (FontTextView) findViewById(R.id.tv_testpaper_rank_list_name);
            tv_testpaper_rank_list_score = (FontTextView) findViewById(R.id.tv_testpaper_rank_list_score);

            setTag(R.string.tag_main_title, "");
            setTag(R.string.tag_main_subtitle, "");

            if (jo == null) {
                setNull();
                return;
            }

            this.tcjo_rank = new TryCatchJO(jo);

            tv_testpaper_rank_list_index.setText(tcjo_rank.get("rank", ""));
            values.aq.id(iv_testpaper_rank_list_pic).image(Functions.borderRadius(Functions.DOMAIN + tcjo_rank.get("src", ""), 1000));
            tv_testpaper_rank_list_name.setText(tcjo_rank.get("username", ""));
            tv_testpaper_rank_list_score.setText(String.format("%.1f점", tcjo_rank.get("score", 0.0)));

            if (tv_testpaper_rank_list_index.getText().equals("1")) {
                tv_testpaper_rank_list_index.setBackgroundColor(Color.parseColor("#f1c40f"));
                iv_testpaper_rank_list_crown.setVisibility(View.VISIBLE);
            } else if (tv_testpaper_rank_list_index.getText().equals("0")) {
                setNull();
            }
            if (!is_shown_score) tv_testpaper_rank_list_score.setText("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void setNull(){
        tv_testpaper_rank_list_index.setText("-");
        values.aq.id(iv_testpaper_rank_list_pic).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        tv_testpaper_rank_list_name.setText(values.user_info.get("first_name", ""));
        tv_testpaper_rank_list_score.setTextSize(14);
        tv_testpaper_rank_list_score.setText("미제출");
    }
}