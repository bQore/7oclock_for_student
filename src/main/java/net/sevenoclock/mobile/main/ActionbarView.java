package net.sevenoclock.mobile.main;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import net.sevenoclock.mobile.R;

public class ActionbarView extends View {

    private View view;

    public ActionbarView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_main_actionbar, null);

        ImageView iv_main_actionbar_menubtn = (ImageView)view.findViewById(R.id.iv_main_actionbar_menubtn);
        iv_main_actionbar_menubtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.menuDrawer.openMenu();
            }
        });
    }

    public View getView(){
        return view;
    }
}
