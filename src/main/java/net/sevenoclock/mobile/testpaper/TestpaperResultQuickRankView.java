package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONArray;
import org.json.JSONException;

public class TestpaperResultQuickRankView extends LinearLayout {

    private Context con;
    private TryCatchJO tcjo_myrank;
    private JSONArray ja_ranks;

    private LinearLayout ll_testpaper_result_rank_u1;
    private ImageView iv_testpaper_result_rank_pic1;
    private FontTextView tv_testpaper_result_rank_name1;

    private LinearLayout ll_testpaper_result_rank_u2;
    private ImageView iv_testpaper_result_rank_pic2;
    private FontTextView tv_testpaper_result_rank_name2;

    private LinearLayout ll_testpaper_result_rank_u3;
    private ImageView iv_testpaper_result_rank_pic3;
    private FontTextView tv_testpaper_result_rank_name3;

    private FontTextView tv_testpaper_result_rank_myrank;
    private ImageView iv_testpaper_result_rank_mypic;
    private FontTextView tv_testpaper_result_rank_myname;
    private FontTextView tv_testpaper_result_rank_myscore;

    Values values;

    public TestpaperResultQuickRankView(Context context, JSONArray ja) throws JSONException {
        super(context);
        this.con = context;
        this.tcjo_myrank = new TryCatchJO(ja.getJSONObject(0));
        this.ja_ranks = tcjo_myrank.getJSONArray("scores");

        values = (Values) context.getApplicationContext();
        inflate(getContext(), R.layout.view_testpaper_result_rank, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tv_testpaper_result_rank_myrank = (FontTextView)findViewById(R.id.tv_testpaper_result_rank_myrank);
        iv_testpaper_result_rank_mypic = (ImageView)findViewById(R.id.iv_testpaper_result_rank_mypic);
        tv_testpaper_result_rank_myname = (FontTextView)findViewById(R.id.tv_testpaper_result_rank_myname);
        tv_testpaper_result_rank_myscore = (FontTextView)findViewById(R.id.tv_testpaper_result_rank_myscore);

        ll_testpaper_result_rank_u1 = (LinearLayout)findViewById(R.id.ll_testpaper_result_rank_u1);
        iv_testpaper_result_rank_pic1 = (ImageView)findViewById(R.id.iv_testpaper_result_rank_pic1);
        tv_testpaper_result_rank_name1 = (FontTextView)findViewById(R.id.tv_testpaper_result_rank_name1);

        ll_testpaper_result_rank_u2 = (LinearLayout)findViewById(R.id.ll_testpaper_result_rank_u2);
        iv_testpaper_result_rank_pic2 = (ImageView)findViewById(R.id.iv_testpaper_result_rank_pic2);
        tv_testpaper_result_rank_name2 = (FontTextView)findViewById(R.id.tv_testpaper_result_rank_name2);

        ll_testpaper_result_rank_u3 = (LinearLayout)findViewById(R.id.ll_testpaper_result_rank_u3);
        iv_testpaper_result_rank_pic3 = (ImageView)findViewById(R.id.iv_testpaper_result_rank_pic3);
        tv_testpaper_result_rank_name3 = (FontTextView)findViewById(R.id.tv_testpaper_result_rank_name3);

        tv_testpaper_result_rank_myrank.setText(tcjo_myrank.get("rank", ""));
        values.aq.id(iv_testpaper_result_rank_mypic).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        tv_testpaper_result_rank_myname.setText(values.user_info.get("first_name", ""));
        tv_testpaper_result_rank_myscore.setText(String.format("%.1f점", tcjo_myrank.get("score", 0.0)));

        if(ja_ranks.length() > 0){
            TryCatchJO tcjo = new TryCatchJO(ja_ranks.getJSONObject(0));
            values.aq.id(iv_testpaper_result_rank_pic1).image(Functions.borderRadius(Functions.DOMAIN + tcjo.get("src", ""), 1000));
            tv_testpaper_result_rank_name1.setText(tcjo.get("username", ""));
            ll_testpaper_result_rank_u1.setVisibility(View.VISIBLE);
            if(ja_ranks.length() > 1){
                tcjo = new TryCatchJO(ja_ranks.getJSONObject(1));
                values.aq.id(iv_testpaper_result_rank_pic2).image(Functions.borderRadius(Functions.DOMAIN + tcjo.get("src", ""), 1000));
                tv_testpaper_result_rank_name2.setText(tcjo.get("username", ""));
                ll_testpaper_result_rank_u2.setVisibility(View.VISIBLE);
                if(ja_ranks.length() > 2){
                    tcjo = new TryCatchJO(ja_ranks.getJSONObject(2));
                    values.aq.id(iv_testpaper_result_rank_pic3).image(Functions.borderRadius(Functions.DOMAIN + tcjo.get("src", ""), 1000));
                    tv_testpaper_result_rank_name3.setText(tcjo.get("username", ""));
                    ll_testpaper_result_rank_u3.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}