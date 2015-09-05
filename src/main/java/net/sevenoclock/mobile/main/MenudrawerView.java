package net.sevenoclock.mobile.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class MenudrawerView extends LinearLayout {

    private AQuery aq;
    private Context con;

    private ImageView iv_main_menudrawer_profilepic;
    private FontTextView tv_main_menudrawer_username;
    private FontTextView tv_main_menudrawer_schoolname;

    private LinearLayout ll_main_menudrawer_list;

    private LinearLayout ll_main_menudrawer_tablist_setting;
    private LinearLayout ll_main_menudrawer_tablist_logout;

    Values values;

    public MenudrawerView(Context context) {
        super(context);
        values = (Values) context.getApplicationContext();
        con = context;
        aq = new AQuery(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_main_menudrawer, null), lp);

        iv_main_menudrawer_profilepic = (ImageView)findViewById(R.id.iv_main_menudrawer_profilepic);
        tv_main_menudrawer_username = (FontTextView)findViewById(R.id.tv_main_menudrawer_username);
        tv_main_menudrawer_schoolname = (FontTextView)findViewById(R.id.tv_main_menudrawer_schoolname);

        ll_main_menudrawer_list = (LinearLayout)findViewById(R.id.ll_main_menudrawer_list);

        ll_main_menudrawer_tablist_setting = (LinearLayout)findViewById(R.id.ll_main_menudrawer_tablist_setting);
        ll_main_menudrawer_tablist_logout = (LinearLayout)findViewById(R.id.ll_main_menudrawer_tablist_logout);

        ll_main_menudrawer_tablist_setting.setOnClickListener((OnClickListener)con);
        ll_main_menudrawer_tablist_logout.setOnClickListener((OnClickListener)con);

        aq.id(iv_main_menudrawer_profilepic).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        aq.id(tv_main_menudrawer_username).text(values.user_info.get("first_name", "다시 로그인해주세요."));
        aq.id(tv_main_menudrawer_schoolname).text(String.format("%s %s학년 %s반"
                , values.user_info.get("school_name", "-")
                , values.user_info.get("school_year", "")
                , values.user_info.get("school_room", "")));

        ll_main_menudrawer_list.addView(new MenudrawerListView(context, R.string.ic_main_menudrawer_list_testpaper, "출제문제지"));
        ll_main_menudrawer_list.addView(new MenudrawerListView(context, R.string.ic_main_menudrawer_list_inventory, "내 보관함"));
        ll_main_menudrawer_list.addView(new MenudrawerListView(context, R.string.ic_main_menudrawer_list_search, "문제검색"));
    }
}
