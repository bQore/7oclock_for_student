package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class MainActionbarView extends FrameLayout {

    private Context con;
    Values values;

    public MainActionbarView(Context context) {
        super(context);
        values = (Values) context.getApplicationContext();
        con = context;

        inflate(getContext(), R.layout.view_main_actionbar, this);

        ImageView iv_main_actionbar_unionbtn = (ImageView) findViewById(R.id.iv_main_actionbar_unionbtn);
        iv_main_actionbar_unionbtn.setOnClickListener((OnClickListener)context);

        values.aq.id(iv_main_actionbar_unionbtn).image(Functions.borderRadius(Functions.DOMAIN + values.union_info.get("icon",""), 1000));

        ImageView iv_main_actionbar_logo = (ImageView)findViewById(R.id.iv_main_actionbar_logo);
        iv_main_actionbar_logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go_home(con);
            }
        });

        IconTextView tv_main_actionbar_menubtn = (IconTextView)findViewById(R.id.tv_main_actionbar_menubtn);
        tv_main_actionbar_menubtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                MainActivity.menuDrawer.openMenu();
            }
        });
    }
}
