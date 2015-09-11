package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.Vibrator;
import android.renderscript.Font;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;
import net.sevenoclock.mobile.settings.Functions;

public class ActionbarSearchView extends FrameLayout {

    private Context con;

    public IconTextView tv_main_actionbar_search_backbtn;
    public EditText et_main_actionbar_search_form;
    public FontTextView tv_main_actionbar_search_searchbtn;

    public ActionbarSearchView(Context context) {
        super(context);
        con = context;

        inflate(getContext(), R.layout.view_main_actionbar_search, this);

        tv_main_actionbar_search_backbtn = (IconTextView)findViewById(R.id.tv_main_actionbar_search_backbtn);
        tv_main_actionbar_search_backbtn.setOnClickListener((OnClickListener)context);

        et_main_actionbar_search_form = (EditText)findViewById(R.id.et_main_actionbar_search_form);

        tv_main_actionbar_search_searchbtn = (FontTextView)findViewById(R.id.tv_main_actionbar_search_searchbtn);
    }
}
