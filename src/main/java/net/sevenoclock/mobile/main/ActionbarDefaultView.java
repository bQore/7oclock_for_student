package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.settings.Functions;

public class ActionbarDefaultView extends FrameLayout {

    private Context con;

    public ActionbarDefaultView(Context context) {
        super(context);
        con = context;

        inflate(getContext(), R.layout.view_main_actionbar_default, this);

        IconTextView tv_main_actionbar_searchbtn = (IconTextView)findViewById(R.id.tv_main_actionbar_searchbtn);
        tv_main_actionbar_searchbtn.setOnClickListener((OnClickListener)context);

        ImageView iv_main_actionbar_default_logo = (ImageView)findViewById(R.id.iv_main_actionbar_default_logo);
        iv_main_actionbar_default_logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.history_go_home(con);
            }
        });

        IconTextView tv_main_actionbar_menubtn = (IconTextView)findViewById(R.id.tv_main_actionbar_default_menubtn);
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
