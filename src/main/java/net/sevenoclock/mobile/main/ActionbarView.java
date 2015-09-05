package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.IconTextView;

public class ActionbarView extends FrameLayout {

    private Context con;

    public ActionbarView(Context context) {
        super(context);
        con = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_main_actionbar, null));

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
