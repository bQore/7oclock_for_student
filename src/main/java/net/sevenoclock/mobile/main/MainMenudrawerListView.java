package net.sevenoclock.mobile.main;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.settings.Values;

public class MainMenudrawerListView extends LinearLayout {

    private IconTextView tv_main_menudrawer_list_icon;
    private FontTextView tv_main_menudrawer_list_title;
    private LinearLayout rl_icon;

    Values values;

    public MainMenudrawerListView(Context context, int str_icon, String title) {
        super(context);
        values = (Values) context.getApplicationContext();

        inflate(getContext(), R.layout.view_main_menudrawer_list, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setTag("ll_main_menudrawer_" + str_icon);
        setOnClickListener((OnClickListener)context);

        tv_main_menudrawer_list_icon = (IconTextView)findViewById(R.id.tv_main_menudrawer_list_icon);
        tv_main_menudrawer_list_title = (FontTextView)findViewById(R.id.tv_main_menudrawer_list_title);

        tv_main_menudrawer_list_icon.setText(str_icon);
        tv_main_menudrawer_list_title.setText(title);
    }

    public MainMenudrawerListView(Context con, View v, String title){
        super(con);
        values = (Values) con.getApplicationContext();

        inflate(getContext(), R.layout.view_main_menudrawer_list, this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setTag("ll_main_menudrawer_333");
        setOnClickListener((OnClickListener) con);

        rl_icon = (LinearLayout) findViewById(R.id.rl_icon);
        tv_main_menudrawer_list_icon = (IconTextView)findViewById(R.id.tv_main_menudrawer_list_icon);
        tv_main_menudrawer_list_title = (FontTextView)findViewById(R.id.tv_main_menudrawer_list_title);

        rl_icon.addView(v);
        rl_icon.setVisibility(View.VISIBLE);
        TextView count = (TextView)v.findViewById(R.id.tv_newsfeed_count);
        count.setText(""+MainActivity.news_count);
        count.setVisibility(View.GONE);

        tv_main_menudrawer_list_icon.setVisibility(View.GONE);
        tv_main_menudrawer_list_title.setText(title);
    }
}
