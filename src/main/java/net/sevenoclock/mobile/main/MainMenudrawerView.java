package net.sevenoclock.mobile.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class MainMenudrawerView extends LinearLayout {
    private Context con;

    private LinearLayout ll_main_menudrawer_profile;
    private ImageView iv_main_menudrawer_profilepic;
    private FontTextView tv_main_menudrawer_username;
    private FontTextView tv_main_menudrawer_unionlevel;

    private LinearLayout ll_main_menudrawer_list;

    private LinearLayout ll_main_menudrawer_tablist_setting;
    private LinearLayout ll_main_menudrawer_tablist_logout;

    Values values;

    public MainMenudrawerView(Context context) {
        super(context);
        values = (Values) context.getApplicationContext();
        con = context;

        View v = (View) inflate(con,R.layout.ic_newsfeed, null);

        inflate(getContext(), R.layout.view_main_menudrawer, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ll_main_menudrawer_profile = (LinearLayout)findViewById(R.id.ll_main_menudrawer_profile);
        iv_main_menudrawer_profilepic = (ImageView)findViewById(R.id.iv_main_menudrawer_profilepic);
        tv_main_menudrawer_username = (FontTextView)findViewById(R.id.tv_main_menudrawer_username);
        tv_main_menudrawer_unionlevel = (FontTextView)findViewById(R.id.tv_main_menudrawer_unionlevel);

        ll_main_menudrawer_list = (LinearLayout)findViewById(R.id.ll_main_menudrawer_list);

        ll_main_menudrawer_tablist_setting = (LinearLayout)findViewById(R.id.ll_main_menudrawer_tablist_setting);
        ll_main_menudrawer_tablist_logout = (LinearLayout)findViewById(R.id.ll_main_menudrawer_tablist_logout);

        ll_main_menudrawer_profile.setOnClickListener((OnClickListener)con);
        ll_main_menudrawer_tablist_setting.setOnClickListener((OnClickListener)con);
        ll_main_menudrawer_tablist_logout.setOnClickListener((OnClickListener)con);

        values.aq.id(iv_main_menudrawer_profilepic).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        values.aq.id(tv_main_menudrawer_username).text(values.user_info.get("first_name", "다시 로그인해주세요."));
        values.aq.id(tv_main_menudrawer_unionlevel).text(String.format("%s %s", values.union_info.get("title", "-"), values.union_info.get("level_title", "-")));

        ll_main_menudrawer_list.addView(new MainMenudrawerListView(context, R.string.ic_main_menudrawer_list_dashboard, "홈"));
        ll_main_menudrawer_list.addView(new MainMenudrawerListView(context, R.string.ic_main_menudrawer_list_testpaper, "출제문제지"));
        ll_main_menudrawer_list.addView(new MainMenudrawerListView(context, R.string.ic_main_menudrawer_list_qna, "실시간질문"));
        ll_main_menudrawer_list.addView(new MainMenudrawerListView(context, R.string.ic_main_menudrawer_list_inventory, "내 보관함"));
        ll_main_menudrawer_list.addView(new MainMenudrawerListView(context, v, "알림"));
//        ll_main_menudrawer_list.addView(new MainMenudrawerListView(context, R.string.ic_bell, "알림"));
    }
}
