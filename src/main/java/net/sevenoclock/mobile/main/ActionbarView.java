package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.settings.Functions;

public class ActionbarView extends FrameLayout {

    private Context con;

    public ActionbarView(Context context) {
        super(context);
        con = context;

        inflate(getContext(), R.layout.view_main_actionbar, this);

        FontTextView tv_main_actionbar_logo = (FontTextView)findViewById(R.id.tv_main_actionbar_logo);
        tv_main_actionbar_logo.setOnClickListener(new OnClickListener() {
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
