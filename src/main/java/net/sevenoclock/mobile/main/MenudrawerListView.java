package net.sevenoclock.mobile.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.settings.Values;

public class MenudrawerListView extends LinearLayout {

    private IconTextView tv_main_menudrawer_list_icon;
    private FontTextView tv_main_menudrawer_list_title;

    Values values;

    public MenudrawerListView(Context context, int str_icon, String title) {
        super(context);
        values = (Values) context.getApplicationContext();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_main_menudrawer_list, null), lp);
        setTag("ll_main_menudrawer_" + str_icon);
        setOnClickListener((OnClickListener)context);

        tv_main_menudrawer_list_icon = (IconTextView)findViewById(R.id.tv_main_menudrawer_list_icon);
        tv_main_menudrawer_list_title = (FontTextView)findViewById(R.id.tv_main_menudrawer_list_title);

        tv_main_menudrawer_list_icon.setText(str_icon);
        tv_main_menudrawer_list_title.setText(title);

    }
}
